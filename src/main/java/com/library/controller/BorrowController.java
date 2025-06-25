package com.library.controller;

import com.library.dto.BorrowDTO;
import com.library.dto.CreateBorrowRequest;
import com.library.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/borrows")
@RequiredArgsConstructor
public class BorrowController {
    private final BorrowService borrowService;

    @GetMapping
    public ResponseEntity<List<BorrowDTO>> getAllBorrows() {
        return ResponseEntity.ok(borrowService.getAllBorrows());
    }

    @PostMapping
    public ResponseEntity<BorrowDTO> createBorrow(@RequestBody CreateBorrowRequest request) {
        return ResponseEntity.ok(borrowService.createBorrow(request));
    }
} 