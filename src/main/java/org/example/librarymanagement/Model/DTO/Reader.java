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
public class Reader {
    private String idReader;
    private String fullName;
    private String phone;
    private String email;
    private String cccd;
    private String address;
    private String avatar;
}
