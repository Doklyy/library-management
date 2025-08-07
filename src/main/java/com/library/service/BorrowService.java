package com.library.service;

import com.library.dto.BorrowDTO;
import com.library.dto.CreateBorrowRequest;
import java.util.List;

public interface BorrowService {
    List<BorrowDTO> getAllBorrows();
    BorrowDTO createBorrow(CreateBorrowRequest request);
    BorrowDTO updateBorrow(Long id, CreateBorrowRequest request);
    void deleteBorrow(Long id);
    void deleteBorrows(List<Long> ids);
    void exportBorrowsToExcel(java.io.OutputStream os) throws java.io.IOException;
} 