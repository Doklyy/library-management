package com.library.controller;

import com.library.dto.BorrowDTO;
import com.library.dto.CreateBorrowRequest;
import com.library.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpHeaders;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

@RestController
@RequestMapping("/api/borrows")
@RequiredArgsConstructor
public class BorrowController {
    private final BorrowService borrowService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<List<BorrowDTO>> getAllBorrows() {
        return ResponseEntity.ok(borrowService.getAllBorrows());
    }

    @PostMapping
    public ResponseEntity<BorrowDTO> createBorrow(@RequestBody CreateBorrowRequest request) {
        return ResponseEntity.ok(borrowService.createBorrow(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<BorrowDTO> updateBorrow(@PathVariable Long id, @RequestBody CreateBorrowRequest request) {
        // TODO: Implement logic in service
        return ResponseEntity.ok(borrowService.updateBorrow(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<Void> deleteBorrow(@PathVariable Long id) {
        // TODO: Implement logic in service
        borrowService.deleteBorrow(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<Void> deleteBorrows(@RequestBody List<Long> ids) {
        borrowService.deleteBorrows(ids);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/export")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<Map<String, String>> exportBorrows() throws IOException {
        String dirPath = "/tmp";
        java.io.File dir = new java.io.File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String filename = "borrows-" + System.currentTimeMillis() + ".xlsx";
        File file = new File(dirPath + "/" + filename);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            borrowService.exportBorrowsToExcel(fos);
        }
        String downloadUrl = "/api/borrows/download/" + filename;
        return ResponseEntity.ok(Map.of("downloadUrl", downloadUrl));
    }

    @GetMapping("/download/{filename}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    public void downloadBorrowExcel(@PathVariable String filename, jakarta.servlet.http.HttpServletResponse response) throws IOException {
        File file = new File("/tmp/" + filename);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        java.nio.file.Files.copy(file.toPath(), response.getOutputStream());
        response.getOutputStream().flush();
    }
} 