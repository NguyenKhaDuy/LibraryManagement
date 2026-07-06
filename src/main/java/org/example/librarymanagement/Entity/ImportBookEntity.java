package org.example.librarymanagement.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "import_book")
public class ImportBookEntity {
    @Id
    @Column(name = "id_import")
    private String idImport;

    @Column(name = "import_date")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate importDate;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "note")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_staff")
    private StaffEntity staffEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_supplier")
    private SupplierEntity supplierEntity;

    @OneToMany(mappedBy = "importBookEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<ImportBookDetailEntity> importBookDetailEntities = new ArrayList<>();

}
