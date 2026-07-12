package org.example.librarymanagement.Model.DTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImportBookDetailDTO {
    private Long idImportBookDetail;
    private Double price;
    private Integer quantity;
    private Double totalPrice;
    private String note;
    private BooksDTO booksDTO;
}
