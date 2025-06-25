package com.library.repository;

import com.library.entity.BorrowReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowReceiptRepository extends JpaRepository<BorrowReceipt, Long> {
} 