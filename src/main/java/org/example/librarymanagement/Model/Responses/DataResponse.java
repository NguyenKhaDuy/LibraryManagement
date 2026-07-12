package org.example.librarymanagement.Model.Responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataResponse <T> {
    private String message;
    private HttpStatus status;
    private Integer current_page;
    private Integer total_pages;
    private T data;
}
