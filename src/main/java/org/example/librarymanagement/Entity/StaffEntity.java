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
@Table(name = "staffs")
public class StaffEntity {
    @Id
    @Column(name = "id_staff")
    private String idStaff;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "dob")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "cccd")
    private String cccd;

    @Column(name = "address")
    private String address;

    @Column(name = "date_start")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateStart;

    @Lob
    @Column(name = "avatar", columnDefinition = "LONGBLOB")
    private byte[] avatar;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "wage")
    private Double wage;

    @Column(name = "position")
    private String position;

    @OneToMany(mappedBy = "staffEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<BorrowTicketEntity> borrowTicketEntities = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_account")
    private AccountEntity accountEntity;

    @OneToMany(mappedBy = "staffEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<ImportBookEntity> importBookEntities = new ArrayList<>();
}
