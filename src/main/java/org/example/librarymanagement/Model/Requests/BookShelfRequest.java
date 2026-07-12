package org.example.librarymanagement.Model.Requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookShelfRequest {
    private Long idBookshelf;
    private String location;
    private Long floor;
    private String area;
    private Long capacity;
}
