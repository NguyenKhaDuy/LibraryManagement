package org.example.librarymanagement.Model.Requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierRequest {
    private Long idSupplier;
    private String nameSupplier;
    private String email;
    private String phone;
    private String address;
}
