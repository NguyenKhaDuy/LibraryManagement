package org.example.librarymanagement.Controllers;

import org.example.librarymanagement.Model.DTO.SupplierDTO;
import org.example.librarymanagement.Model.Requests.SupplierRequest;
import org.example.librarymanagement.Model.Responses.DataResponse;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.example.librarymanagement.Services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SupplierControlller {
    @Autowired
    SupplierService supplierService;

    @GetMapping(value = "/api/admin/supplier")
    public ResponseEntity<Object> getSupplier(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        DataResponse dataResponse = new DataResponse();
        Page<SupplierDTO> supplierDTOS = supplierService.getAllSuppliers(pageNo);
        dataResponse.setData(supplierDTOS.getContent());
        dataResponse.setStatus(HttpStatus.OK);
        dataResponse.setMessage("Success");
        dataResponse.setCurrent_page(pageNo);
        dataResponse.setTotal_pages(supplierDTOS.getTotalPages());
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/suppliers")
    public ResponseEntity<Object> getSupplier() {
        DataResponse dataResponse = supplierService.getAllSuppliers();
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/supplier/idSupplier={id}")
    public ResponseEntity<Object> getSupplierById(@PathVariable("id") Long id) {
        Object result = supplierService.getSupplierById(id);
        if(result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/api/admin/supplier")
    public ResponseEntity<Object> addSupplier(@RequestBody SupplierRequest supplierRequest) {
        MessageResponse messageResponse = supplierService.addSupplier(supplierRequest);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PutMapping(value = "/api/admin/supplier")
    public ResponseEntity<Object> updateSupplier(@RequestBody SupplierRequest supplierRequest) {
        MessageResponse messageResponse = supplierService.updateSupplier(supplierRequest);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @DeleteMapping(value = "/api/admin/supplier/idSupplier={id}")
    public ResponseEntity<Object> deleteSupplier(@PathVariable("id") Long id) {
        MessageResponse messageResponse = supplierService.deleteSupplier(id);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }
}
