package org.example.librarymanagement.Services;

import org.example.librarymanagement.Model.DTO.ImportBookDTO;
import org.example.librarymanagement.Model.Requests.ImportBookRequest;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface ImportBookService {
    Page<ImportBookDTO> getImportBooks(Integer pageNo);
    Object getImportBook(String idImport);
    MessageResponse addImportBook(ImportBookRequest importBookRequest);
    MessageResponse updateImportBook(ImportBookRequest importBookRequest);
    MessageResponse deleteImportBook(String idImport);
}
