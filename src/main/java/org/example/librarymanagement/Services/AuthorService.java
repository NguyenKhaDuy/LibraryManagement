package org.example.librarymanagement.Services;

import org.example.librarymanagement.Model.DTO.AuthorDTO;
import org.example.librarymanagement.Model.Requests.AuthorRequest;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface AuthorService {
    Page<AuthorDTO> getAllAuthors(Integer pageNo);
    Object getAuthorById(String idAuthor);
    MessageResponse addAuthor(AuthorRequest authorRequest);
    MessageResponse updateAuthor(AuthorRequest authorRequest);
    MessageResponse deleteAuthor(String idAuthor);
}
