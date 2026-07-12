package org.example.librarymanagement.Model.Requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.librarymanagement.Entity.Status;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AcceptRequest {
    private String staffId;
    private String ticketId;
    private Status status;
}
