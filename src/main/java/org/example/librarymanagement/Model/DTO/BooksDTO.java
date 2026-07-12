package org.example.librarymanagement.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.librarymanagement.Entity.Status;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BooksDTO {
    private String idBook;
    private String nameBook;
    private Long yearOfPub;
    private Long quantity;
    private Long edition;
    private String language;
    private Double valueOfBook;
    private String description;
    private Status status;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
    private List<ImageDTO> imageDTOS;
    private AuthorDTO authorDTO;
    private CategoryDTO categoryDTO;
    private BookshelfDTO bookshelfDTO;
    private PublishingHouseDTO publishingHouseDTO;
}
