package org.example.librarymanagement.Services;

import org.example.librarymanagement.Model.DTO.LibraryCardDTO;
import org.example.librarymanagement.Model.Requests.LibraryCardRequest;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface LibraryCardService {
    Page<LibraryCardDTO> getAllLibraryCards(Integer pageNo);
    Object getLibraryCard(String idCard);
    MessageResponse createCardReader(LibraryCardRequest libraryCardRequest);
    MessageResponse updateCardReader(LibraryCardRequest libraryCardRequest);
    MessageResponse deleteCardReader(String idCard);
}
