package org.example.librarymanagement.Services;

import org.example.librarymanagement.Model.DTO.BooksDTO;
import org.example.librarymanagement.Model.Requests.BookRequest;
import org.example.librarymanagement.Model.Responses.DataResponse;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface BooksService {
    Page<BooksDTO> getBooks(String idAuthor, Long idCategory, Long idBookShelf, Long idPublisher, Integer pageNo);
    DataResponse getBooks();
    Object getBooksById(String idBook);
    MessageResponse addBook(BookRequest bookRequest);
    MessageResponse updateBook(BookRequest bookRequest);
    MessageResponse deleteBook(String idBook);
}
