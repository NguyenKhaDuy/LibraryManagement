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
public class Staff {
    private String idStaff;
    private String fullName;
    private String email;
    private String phone;
    private String cccd;
    private String avatar;
}
