package org.example.librarymanagement.Model.Requests;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImportDetailBookRequest {
    private String bookId;
    private Double price;
    private Integer quantity;
    private Double totalPrice;
    private String note;
}
