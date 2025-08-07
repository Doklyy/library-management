package com.library.controller;

import com.library.common.ApiResponse;
import com.library.common.ResponseCode;
import com.library.dto.PageResponse;
import com.library.dto.UserDTO;
import com.library.repository.UserRepository;
import com.library.service.UserService;
import com.library.dto.LoginRequest;
import com.library.dto.CreateUserRequest;
import com.library.dto.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ContentDisposition;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import org.springframework.core.io.InputStreamResource;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDTO>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDTO user = userService.getUserByUsername(username);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @GetMapping
    public ResponseEntity<PageResponse<UserDTO>> getAllUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String role,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(userService.searchUsers(keyword, isActive, role, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        UserDTO user = userService.findDetailUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody @Valid CreateUserRequest request) {
        UserDTO user = userService.createUser(request);
        return ResponseEntity.ok(ApiResponse.created(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteUser(@RequestBody List<Long> ids) {
        userService.deleteUserById(ids);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/check-role/{username}")
    public ResponseEntity<ApiResponse<String>> checkUserRole(@PathVariable String username) {
        UserDTO user = userService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.ok(ApiResponse.error(ResponseCode.NOT_FOUND, "User not found"));
        }
        return ResponseEntity.ok(ApiResponse.success("User role: " + user.getRole()));
    }

    @GetMapping("/check")
    public ResponseEntity<String> checkAuth(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        return ResponseEntity.ok("Authenticated successfully. Auth header: " + authHeader);
    }

    @PostMapping("/import")
    public ResponseEntity<?> importUsers(@RequestParam("file") MultipartFile file) throws IOException {
        List<String[]> errorRows = new java.util.ArrayList<>();
        int totalRows = 0;
        int successRows = 0;
        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            boolean hasPhone = false;
            int phoneColIdx = -1;
            for (Cell cell : headerRow) {
                if (cell.getStringCellValue().trim().equalsIgnoreCase("phone")) {
                    hasPhone = true;
                    phoneColIdx = cell.getColumnIndex();
                    break;
                }
            }
            if (!hasPhone) {
                throw new RuntimeException("File import phải có cột 'phone' (số điện thoại)!");
            }

            int rowNum = 0;
            for (Row row : sheet) {
                if (rowNum++ == 0) continue; // Bỏ qua header
                totalRows++;
                String username = getCellValue(row.getCell(0));
                String email = getCellValue(row.getCell(1));
                String fullName = getCellValue(row.getCell(2));
                String phone = getCellValue(row.getCell(phoneColIdx));
                String error = validateUser(username, email, fullName, phone);
                if (error == null) {
                    try {
                        CreateUserRequest req = new CreateUserRequest();
                        req.setUsername(username);
                        req.setEmail(email);
                        req.setFullName(fullName);
                        req.setPhone(phone);
                        userService.createUser(req);
                        successRows++;
                    } catch (Exception ex) {
                        error = ex.getMessage();
                    }
                }
                if (error != null) {
                    errorRows.add(new String[]{username, email, fullName, phone, error});
                }
            }
        } catch (Exception ex) {
            errorRows.add(new String[]{"", "", "", "", "Lỗi import: " + ex.getMessage()});
        }
        if (!errorRows.isEmpty()) {
            // Tạo file Excel lỗi
            String fileName = "import-user-errors-" + System.currentTimeMillis() + ".xlsx";
            String filePath = System.getProperty("java.io.tmpdir") + File.separator + fileName;
            try (Workbook errorWorkbook = new XSSFWorkbook()) {
                Sheet errorSheet = errorWorkbook.createSheet("Errors");
                // Header
                Row header = errorSheet.createRow(0);
                header.createCell(0).setCellValue("Username");
                header.createCell(1).setCellValue("Email");
                header.createCell(2).setCellValue("Full Name");
                header.createCell(3).setCellValue("Phone");
                header.createCell(4).setCellValue("Error");
                // Dữ liệu lỗi
                int rowIdx = 1;
                for (String[] errRow : errorRows) {
                    Row r = errorSheet.createRow(rowIdx++);
                    for (int i = 0; i < errRow.length; i++) {
                        r.createCell(i).setCellValue(errRow[i]);
                    }
                }
                // Dòng tổng kết
                Row summaryRow = errorSheet.createRow(rowIdx);
                summaryRow.createCell(0).setCellValue("Tổng dòng:");
                summaryRow.createCell(1).setCellValue(totalRows);
                summaryRow.createCell(2).setCellValue("Thành công:");
                summaryRow.createCell(3).setCellValue(successRows);

                // Lưu file
                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    errorWorkbook.write(fos);
                }
            }
            // Trả về JSON gồm code, message, data, downloadUrl
            String downloadUrl = "/api/users/download/" + fileName;
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("code", ResponseCode.BAD_REQUEST);
            response.put("message", "Import thất bại");
            response.put("data", errorRows);
            response.put("downloadUrl", downloadUrl);
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(new ApiResponse<>(ResponseCode.SUCCESS, "Import thành công!", null));
    }

    private String validateUser(String username, String email, String fullName, String phone) {
        if (username == null || username.isEmpty()) return "Thiếu username";
        if (email == null || email.isEmpty()) return "Thiếu email";
        if (fullName == null || fullName.isEmpty()) return "Thiếu fullName";
        if (phone == null || phone.isEmpty()) return "Thiếu phone";

        // Kiểm tra username chỉ chứa chữ, số và dấu gạch dưới
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            return "Username chỉ được chứa chữ cái, số và dấu gạch dưới";
        }

        // Kiểm tra định dạng email
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!email.matches(emailRegex)) {
            return "Email không đúng định dạng";
        }

        // Kiểm tra domain email hợp lệ (phải có .com, .net, .org, etc.)
        if (!email.matches(".*\\.(com|net|org|edu|gov)$")) {
            return "Domain email không hợp lệ (phải kết thúc bằng .com, .net, .org, .edu hoặc .gov)";
        }

        // Kiểm tra độ dài fullName
        if (fullName.length() > 100) {
            return "Họ tên không được quá 100 ký tự";
        }

        // Kiểm tra fullName chỉ chứa chữ cái và khoảng trắng
        if (!fullName.matches("^[\\p{L} ]+$")) {
            return "Họ tên chỉ được chứa chữ cái và khoảng trắng";
        }

        try {
            // Kiểm tra username đã tồn tại
            if (userRepository.existsByUsername(username)) {
                return "Username '" + username + "' đã tồn tại";
            }

            // Kiểm tra email đã tồn tại
            if (userRepository.existsByEmail(email)) {
                return "Email '" + email + "' đã tồn tại";
            }
        } catch (Exception e) {
            return "Lỗi kiểm tra dữ liệu: " + e.getMessage();
        }
        return null;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((long) cell.getNumericCellValue());
        }
        return cell.getStringCellValue().trim();
    }

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<?> downloadImportErrorFile(@PathVariable String filename) throws IOException {
        String filePath = System.getProperty("java.io.tmpdir") + File.separator + filename;
        File file = new File(filePath);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);
    }
}