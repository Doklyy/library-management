package com.library.service;

import com.library.entity.Book;
import java.util.List;

public interface GoogleBooksService {
    /**
     * Đồng bộ sách từ Google Books API
     * @param query Từ khóa tìm kiếm
     * @return Danh sách sách đã đồng bộ
     */
    List<Book> syncBooks(String query);
} 