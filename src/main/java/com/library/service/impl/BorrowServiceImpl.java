package com.library.service.impl;

import com.library.dto.BorrowDTO;
import com.library.dto.CreateBorrowRequest;
import com.library.entity.BorrowReceipt;
import com.library.entity.BorrowReceiptBook;
import com.library.entity.User;
import com.library.entity.Book;
import com.library.repository.BorrowReceiptRepository;
import com.library.repository.UserRepository;
import com.library.repository.BookRepository;
import com.library.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class BorrowServiceImpl implements BorrowService {
    private final BorrowReceiptRepository borrowReceiptRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    // Logic constants
    private static final String STATUS_BORROWED = "BORROWED";
    private static final String STATUS_RETURNED = "RETURNED";
    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_OVERDUE = "OVERDUE";
    private static final int MAX_BORROW_DAYS = 30;
    private static final int MAX_BOOKS_PER_BORROW = 5;

    @Override
    public List<BorrowDTO> getAllBorrows() {
        return borrowReceiptRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public BorrowDTO createBorrow(CreateBorrowRequest request) {
        BorrowReceipt receipt = new BorrowReceipt();
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        receipt.setUser(user);
        receipt.setBorrowDate(request.getBorrowDate());
        receipt.setDueDate(request.getDueDate());
        receipt.setStatus(STATUS_BORROWED);

        if (request.getBooks() == null || request.getBooks().isEmpty()) {
            throw new IllegalArgumentException("Danh sách sách mượn (books) không được để trống!");
        }

        // Kiểm tra trùng mã sách
        java.util.Set<Long> bookIdSet = new java.util.HashSet<>();
        for (CreateBorrowRequest.BookQuantityRequest bq : request.getBooks()) {
            if (!bookIdSet.add(bq.getBookId())) {
                throw new RuntimeException("Trùng mã sách: " + bq.getBookId());
            }
        }

        List<BorrowReceiptBook> receiptBooks = new ArrayList<>();
        for (CreateBorrowRequest.BookQuantityRequest bq : request.getBooks()) {
            Book bookToBorrow = bookRepository.findById(bq.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + bq.getBookId()));
            int quantityToBorrow = bq.getQuantity() != null ? bq.getQuantity() : 1;

            // Kiểm tra số lượng còn lại
            if (bookToBorrow.getAvailable() < quantityToBorrow) {
                throw new RuntimeException("Sách với ID " + bookToBorrow.getId() + " không đủ số lượng để mượn.");
            }

            // Cập nhật số lượng
            bookToBorrow.setAvailable(bookToBorrow.getAvailable() - quantityToBorrow);
            bookToBorrow.setBorrowed(bookToBorrow.getBorrowed() + quantityToBorrow);
            bookRepository.save(bookToBorrow);

            BorrowReceiptBook brb = new BorrowReceiptBook();
            brb.setBorrowReceipt(receipt);
            brb.setBook(bookToBorrow);
            brb.setQuantity(quantityToBorrow);
            receiptBooks.add(brb);
        }
        receipt.getBorrowReceiptBooks().addAll(receiptBooks);

        BorrowReceipt saved = borrowReceiptRepository.save(receipt);
        return toDTO(saved);
    }

    @Override
    public BorrowDTO updateBorrow(Long id, CreateBorrowRequest request) {
        BorrowReceipt receipt = borrowReceiptRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Borrow receipt not found"));
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        receipt.setUser(user);
        receipt.setBorrowDate(request.getBorrowDate());
        receipt.setDueDate(request.getDueDate());

        // Xử lý danh sách sách mượn
        if (request.getBooks() != null && !request.getBooks().isEmpty()) {
            // Kiểm tra trùng mã sách
            java.util.Set<Long> bookIdSet = new java.util.HashSet<>();
            for (CreateBorrowRequest.BookQuantityRequest bq : request.getBooks()) {
                if (!bookIdSet.add(bq.getBookId())) {
                    throw new RuntimeException("Trùng mã sách: " + bq.getBookId());
                }
            }
            // Xóa danh sách cũ
            receipt.getBorrowReceiptBooks().clear();
            List<BorrowReceiptBook> receiptBooks = new ArrayList<>();
            for (CreateBorrowRequest.BookQuantityRequest bq : request.getBooks()) {
                Book bookToBorrow = bookRepository.findById(bq.getBookId())
                    .orElseThrow(() -> new RuntimeException("Book not found with ID: " + bq.getBookId()));
                int quantityToBorrow = bq.getQuantity() != null ? bq.getQuantity() : 1;

                // Kiểm tra số lượng còn lại
                if (bookToBorrow.getAvailable() < quantityToBorrow) {
                    throw new RuntimeException("Sách với ID " + bookToBorrow.getId() + " không đủ số lượng để mượn.");
                }

                // Cập nhật số lượng
                bookToBorrow.setAvailable(bookToBorrow.getAvailable() - quantityToBorrow);
                bookToBorrow.setBorrowed(bookToBorrow.getBorrowed() + quantityToBorrow);
                bookRepository.save(bookToBorrow);

                BorrowReceiptBook brb = new BorrowReceiptBook();
                brb.setBorrowReceipt(receipt);
                brb.setBook(bookToBorrow);
                brb.setQuantity(quantityToBorrow);
                receiptBooks.add(brb);
            }
            receipt.getBorrowReceiptBooks().addAll(receiptBooks);
        }
        BorrowReceipt saved = borrowReceiptRepository.save(receipt);
        return toDTO(saved);
    }

    @Override
    public void deleteBorrow(Long id) {
        BorrowReceipt receipt = borrowReceiptRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Borrow receipt not found"));
        borrowReceiptRepository.delete(receipt);
    }

    @Override
    public void deleteBorrows(List<Long> ids) {
        borrowReceiptRepository.deleteAllById(ids);
    }

    @Override
    public void exportBorrowsToExcel(java.io.OutputStream os) throws java.io.IOException {
        org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
        org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Borrows");
        // Header
        org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("User ID");
        header.createCell(2).setCellValue("Borrow Date");
        header.createCell(3).setCellValue("Due Date");
        header.createCell(4).setCellValue("Return Date");
        header.createCell(5).setCellValue("Status");
        header.createCell(6).setCellValue("Books");
        // Data
        java.util.List<BorrowReceipt> receipts = borrowReceiptRepository.findAll();
        int rowIdx = 1;
        for (BorrowReceipt receipt : receipts) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(receipt.getId());
            row.createCell(1).setCellValue(receipt.getUser().getId());
            row.createCell(2).setCellValue(receipt.getBorrowDate() != null ? receipt.getBorrowDate().toString() : "");
            row.createCell(3).setCellValue(receipt.getDueDate() != null ? receipt.getDueDate().toString() : "");
            row.createCell(4).setCellValue(receipt.getReturnDate() != null ? receipt.getReturnDate().toString() : "");
            row.createCell(5).setCellValue(receipt.getStatus());
            // Lấy danh sách tên sách đã mượn
            String books = "";
            if (receipt.getBorrowReceiptBooks() != null && !receipt.getBorrowReceiptBooks().isEmpty()) {
                books = receipt.getBorrowReceiptBooks().stream()
                        .map(brb -> brb.getBook() != null ? brb.getBook().getTitle() : "")
                        .filter(title -> !title.isEmpty())
                        .reduce((a, b) -> a + ", " + b).orElse("");
            }
            row.createCell(6).setCellValue(books);
        }
        workbook.write(os);
        workbook.close();
    }

    private BorrowDTO toDTO(BorrowReceipt receipt) {
        BorrowDTO dto = new BorrowDTO();
        dto.setId(receipt.getId());
        dto.setUserId(receipt.getUser().getId());
        dto.setBorrowDate(receipt.getBorrowDate());
        dto.setDueDate(receipt.getDueDate());
        dto.setReturnDate(receipt.getReturnDate());
        dto.setStatus(receipt.getStatus());
        return dto;
    }
} 