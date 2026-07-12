package org.example.librarymanagement.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailDTO {
    private String idBook;
    private String nameBook;
    private List<ImageDTO> imageDTOS;
    private String description;
    private Long yearOfPub;
    private String nameAuthor;
}
