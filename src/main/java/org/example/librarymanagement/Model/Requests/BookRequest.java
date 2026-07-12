package org.example.librarymanagement.Model.Requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.librarymanagement.Entity.Status;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {
    private String idBook;
    private String nameBook;
    private Long yearOfPub;
    private Long quantity;
    private Long edition;
    private String language;
    private Double valueOfBook;
    private String description;
    private List<MultipartFile> images;
    private String authorId;
    private Long categoryId;
    private Long bookShelfId;
    private Long publisherId;
    private Status status;
}
