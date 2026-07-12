package org.example.librarymanagement.Model.Requests;

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
public class ImportBookRequest {
    private String idImport;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate importDate;
    private Double totalPrice;
    private String note;
    private String staffId;
    private Long supplierId;
    private List<ImportDetailBookRequest> importDetailBookRequests;
}
