package com.library.service.impl;

import com.library.dto.BorrowDTO;
import com.library.dto.CreateBorrowRequest;
import com.library.entity.BorrowReceipt;
import com.library.entity.BorrowReceiptBook;
import com.library.entity.User;
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
        receipt.setStatus("BORROWED");

        // Handle multiple books, each with different quantities
        if (request.getBooks() == null || request.getBooks().isEmpty()) {
            throw new IllegalArgumentException("Danh sách sách mượn (books) không được để trống!");
        }

        List<BorrowReceiptBook> receiptBooks = new ArrayList<>();
        for (CreateBorrowRequest.BookQuantityRequest bq : request.getBooks()) {
            BorrowReceiptBook brb = new BorrowReceiptBook();
            brb.setBorrowReceipt(receipt);
            brb.setBook(bookRepository.findById(bq.getBookId()).orElseThrow(() -> new RuntimeException("Book not found with ID: " + bq.getBookId())));
            brb.setQuantity(bq.getQuantity() != null ? bq.getQuantity() : 1);
            receiptBooks.add(brb);
        }
        receipt.setBorrowReceiptBooks(receiptBooks);
        BorrowReceipt saved = borrowReceiptRepository.save(receipt);
        return toDTO(saved);
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