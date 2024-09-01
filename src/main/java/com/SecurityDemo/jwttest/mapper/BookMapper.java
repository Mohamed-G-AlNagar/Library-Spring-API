package com.SecurityDemo.jwttest.mapper;

import com.SecurityDemo.jwttest.dao_request.BookRequest;
import com.SecurityDemo.jwttest.entity.Book;
import com.SecurityDemo.jwttest.entity.BookTransactionHistory;
import com.SecurityDemo.jwttest.response.BookResponse;
import com.SecurityDemo.jwttest.response.BorrowedBookResponse;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {
    public Book toBook(BookRequest request) {
        return Book.builder()
                .id(request.getId())
                .title(request.getTitle())
                .isbn(request.getIsbn())
                .authorName(request.getAuthorName())
                .synopsis(request.getSynopsis())
                .archived(false)
                .shareable(request.getShareable())
                .build();

    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                .rate(book.getRate())
                .archived(book.isArchived())
                .shareable(book.isShareable())
                .owner(book.getOwner().getFullName())
//                .cover(FileUtils.readFileFromLocation(book.getBookCover()))
                .build();
    }


    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory history) {
        return BorrowedBookResponse.builder()
                .id(history.getBook().getId())
                .title(history.getBook().getTitle())
                .authorName(history.getBook().getAuthorName())
                .isbn(history.getBook().getIsbn())
                .rate(history.getBook().getRate())
                .returned(history.isReturned())
                .returnApproved(history.isReturnApproved())
                .build();
    }
}
