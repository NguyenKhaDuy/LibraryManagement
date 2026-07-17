package org.example.librarymanagement.Services.Impl;

import org.example.librarymanagement.Entity.SupplierEntity;
import org.example.librarymanagement.Model.DTO.SupplierDTO;
import org.example.librarymanagement.Model.Requests.SupplierRequest;
import org.example.librarymanagement.Model.Responses.DataResponse;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.example.librarymanagement.Repository.SupplierRepository;
import org.example.librarymanagement.Services.SupplierService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SupplierServiceImpl implements SupplierService {
    @Autowired
    SupplierRepository supplierRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Page<SupplierDTO> getAllSuppliers(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 20);
        Page<SupplierEntity> supplierEntities = supplierRepository.findAll(pageable);
        List<SupplierDTO> supplierDTOs = supplierEntities.stream().map(supplierEntity -> {
            SupplierDTO supplierDTO = new SupplierDTO();
            modelMapper.map(supplierEntity, supplierDTO);
            return supplierDTO;
        }).toList();
        return new PageImpl<>(supplierDTOs, supplierEntities.getPageable(), supplierEntities.getTotalElements());
    }

    @Override
    public DataResponse getAllSuppliers() {
        List<SupplierEntity> supplierEntities = supplierRepository.findAll();
        List<SupplierDTO> supplierDTOs = supplierEntities.stream().map(supplierEntity -> {
            SupplierDTO supplierDTO = new SupplierDTO();
            modelMapper.map(supplierEntity, supplierDTO);
            return supplierDTO;
        }).toList();
        DataResponse dataResponse = new DataResponse();
        dataResponse.setData(supplierDTOs);
        dataResponse.setStatus(HttpStatus.OK);
        dataResponse.setMessage("Success");
        return dataResponse;
    }

    @Override
    public Object getSupplierById(Long idSupplier) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse dataResponse = new DataResponse();
        try {
            SupplierEntity supplierEntity = supplierRepository.findById(idSupplier).get();
            SupplierDTO supplierDTO = new SupplierDTO();
            modelMapper.map(supplierEntity, supplierDTO);
            dataResponse.setData(supplierDTO);
            dataResponse.setMessage("Success");
            dataResponse.setStatus(HttpStatus.OK);
            return dataResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not found supplier");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }

    @Override
    public MessageResponse addSupplier(SupplierRequest supplierRequest) {
        MessageResponse messageResponse = new MessageResponse();
        SupplierEntity supplierEntity = new SupplierEntity();
        modelMapper.map(supplierRequest, supplierEntity);
        supplierRepository.save(supplierEntity);
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse updateSupplier(SupplierRequest supplierRequest) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            SupplierEntity supplierEntity = supplierRepository.findById(supplierRequest.getIdSupplier()).get();
            modelMapper.map(supplierRequest, supplierEntity);
            supplierRepository.save(supplierEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
            return messageResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not found supplier");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }

    @Override
    public MessageResponse deleteSupplier(Long idSupplier) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            SupplierEntity supplierEntity = supplierRepository.findById(idSupplier).get();
            supplierRepository.delete(supplierEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
            return messageResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Can not found supplier");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }
}
