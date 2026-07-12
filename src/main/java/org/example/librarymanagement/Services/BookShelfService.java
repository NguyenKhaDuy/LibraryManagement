package org.example.librarymanagement.Services;

import org.apache.logging.log4j.message.Message;
import org.example.librarymanagement.Model.DTO.BookshelfDTO;
import org.example.librarymanagement.Model.Requests.BookShelfRequest;
import org.example.librarymanagement.Model.Responses.DataResponse;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface BookShelfService {
    Page<BookshelfDTO> getAllBookShelf(Integer pageNo);
    Object getBookShelfById(Long idBookshelf);
    MessageResponse addBookShelf(BookShelfRequest bookShelfRequest);
    MessageResponse updateBookShelf(BookShelfRequest bookShelfRequest);
    MessageResponse deleteBookShelf(Long idBookshelf);
}
