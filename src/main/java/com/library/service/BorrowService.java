package com.library.service;

import com.library.dto.BorrowDTO;
import com.library.dto.CreateBorrowRequest;
import java.util.List;

public interface BorrowService {
    List<BorrowDTO> getAllBorrows();
    BorrowDTO createBorrow(CreateBorrowRequest request);
} 