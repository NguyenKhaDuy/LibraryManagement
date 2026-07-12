package org.example.librarymanagement.Model.DTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImportBookDTO {
    private String idImport;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate importDate;
    private Double totalPrice;
    private String note;
    private String staffId;
    private String staffName;
    private SupplierDTO supplierDTO;
    private List<ImportBookDetailDTO> importBookDetailDTOS;
}
