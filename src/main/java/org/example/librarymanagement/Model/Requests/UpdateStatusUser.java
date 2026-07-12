package org.example.librarymanagement.Model.Requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.librarymanagement.Entity.Status;

import java.util.Stack;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStatusUser {
    private String idUser;
    private Status status;
}
