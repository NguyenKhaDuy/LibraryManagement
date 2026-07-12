package org.example.librarymanagement.Services;

import org.example.librarymanagement.Model.DTO.SupplierDTO;
import org.example.librarymanagement.Model.Requests.SupplierRequest;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface SupplierService {
    Page<SupplierDTO> getAllSuppliers(Integer pageNo);
    Object getSupplierById(Long idSupplier);
    MessageResponse addSupplier(SupplierRequest supplierRequest);
    MessageResponse updateSupplier(SupplierRequest supplierRequest);
    MessageResponse deleteSupplier(Long idSupplier);
}
