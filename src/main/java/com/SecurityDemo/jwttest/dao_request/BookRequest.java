package com.SecurityDemo.jwttest.dao_request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequest {
    Integer id;
    @NotNull(message = "title is mandatory")
    @NotEmpty(message = "title is mandatory")
    private String title;
    @NotNull(message = "authorName is mandatory")
    @NotEmpty(message = "authorName is mandatory")
    String authorName;
    @NotNull(message = "isbn is mandatory")
    @NotEmpty(message = "isbn is mandatory")
    String isbn;
    @NotNull(message = "synopsis is mandatory")
    @NotEmpty(message = "synopsis is mandatory")
    String synopsis;
    boolean shareable;

    public boolean getShareable() {
        return shareable;
    }
}
