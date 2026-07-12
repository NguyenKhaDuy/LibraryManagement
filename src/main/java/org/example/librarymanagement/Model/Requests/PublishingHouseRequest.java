package org.example.librarymanagement.Model.Requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublishingHouseRequest {
    private Long idPublishingHouse;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String website;
}
