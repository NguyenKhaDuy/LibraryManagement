package org.example.librarymanagement.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.librarymanagement.Entity.Status;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookshelfDTO {
    private Long idBookshelf;
    private String location;
    private Long floor;
    private String area;
    private Long capacity;
    private Long currentCapacity;
    private Status status;
}
