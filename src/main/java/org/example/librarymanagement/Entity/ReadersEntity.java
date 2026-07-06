package org.example.librarymanagement.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "readers")
public class ReadersEntity {
    @Id
    @Column(name = "id_reader")
    private String idReader;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "dob")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;

    @Column(name = "gender")
    private String gender;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "cccd")
    private String cccd;

    @Column(name = "address")
    private String address;

    @Column(name = "registration_date")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime registrationDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Lob
    @Column(name = "avatar", columnDefinition = "LONGBLOB")
    private byte[] avatar;

    @OneToMany(mappedBy = "readersEntity", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<LibraryCardEntity> libraryCardEntities = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_account")
    private AccountEntity accountEntity;
}
