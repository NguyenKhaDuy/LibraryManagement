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
public class PublishingHouseDTO {
    private Long idPublishingHouse;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String website;
}
