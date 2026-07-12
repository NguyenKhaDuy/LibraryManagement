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
public class SupplierDTO {
    private Long idSupplier;
    private String nameSupplier;
    private String email;
    private String phone;
    private String address;
}
