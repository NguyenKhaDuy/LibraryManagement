package org.example.librarymanagement.Services.Impl;

import org.example.librarymanagement.Entity.LibraryCardEntity;
import org.example.librarymanagement.Entity.ReadersEntity;
import org.example.librarymanagement.Entity.Status;
import org.example.librarymanagement.Model.DTO.LibraryCardDTO;
import org.example.librarymanagement.Model.DTO.Reader;
import org.example.librarymanagement.Model.Requests.LibraryCardRequest;
import org.example.librarymanagement.Model.Responses.DataResponse;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.example.librarymanagement.Repository.LibraryCardRepository;
import org.example.librarymanagement.Repository.ReaderRepository;
import org.example.librarymanagement.Services.LibraryCardService;
import org.example.librarymanagement.Utils.ConvertByteToBase64;
import org.example.librarymanagement.Utils.RandomIdUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LibraryCardServiceImpl implements LibraryCardService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ReaderRepository readerRepository;
    @Autowired
    LibraryCardRepository libraryCardRepository;


    @Override
    public Page<LibraryCardDTO> getAllLibraryCards(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 20);
        Page<LibraryCardEntity> libraryCardEntities = libraryCardRepository.findAll(pageable);
        List<LibraryCardDTO> libraryCardDTOS = libraryCardEntities.stream().map(libraryCardEntity -> {
            LibraryCardDTO libraryCardDTO = new LibraryCardDTO();
            modelMapper.map(libraryCardEntity, libraryCardDTO);
            Reader reader = new Reader();
            modelMapper.map(libraryCardEntity.getReadersEntity(), reader);
            reader.setAvatar(ConvertByteToBase64.toBase64(libraryCardEntity.getReadersEntity().getAvatar()));
            libraryCardDTO.setReader(reader);
            return libraryCardDTO;
        }).toList();
        return new PageImpl<>(libraryCardDTOS, libraryCardEntities.getPageable(), libraryCardEntities.getTotalElements());
    }

    @Override
    public Object getLibraryCard(String idCard) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse dataResponse = new DataResponse();
        try {
            LibraryCardEntity libraryCardEntity = libraryCardRepository.findById(idCard).get();
            LibraryCardDTO libraryCardDTO = new LibraryCardDTO();
            modelMapper.map(libraryCardEntity, libraryCardDTO);
            Reader reader = new Reader();
            modelMapper.map(libraryCardEntity.getReadersEntity(), reader);
            reader.setAvatar(ConvertByteToBase64.toBase64(libraryCardEntity.getReadersEntity().getAvatar()));
            libraryCardDTO.setReader(reader);
            dataResponse.setData(libraryCardDTO);
            dataResponse.setMessage("Success");
            dataResponse.setStatus(HttpStatus.OK);
            return dataResponse;
        }catch (NoSuchElementException e) {
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            messageResponse.setMessage("No such library Card");
            return messageResponse;
        }
    }

    @Override
    public MessageResponse createCardReader(LibraryCardRequest libraryCardRequest) {
        MessageResponse messageResponse = new MessageResponse();
        ReadersEntity readersEntity = null;
        try {
            readersEntity = readerRepository.findById(libraryCardRequest.getIdReader()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("No such reader");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        LibraryCardEntity libraryCardEntity = new LibraryCardEntity();
        modelMapper.map(libraryCardRequest, libraryCardEntity);
        libraryCardEntity.setIdCard(RandomIdUtils.generateRandomId("C", 15));
        libraryCardEntity.setStatus(Status.ACTIVE);
        libraryCardEntity.setReadersEntity(readersEntity);
        libraryCardRepository.save(libraryCardEntity);
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse updateCardReader(LibraryCardRequest libraryCardRequest) {
        MessageResponse messageResponse = new MessageResponse();
        LibraryCardEntity libraryCardEntity = null;
        try {
            libraryCardEntity = libraryCardRepository.findById(libraryCardRequest.getIdReader()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("No such Card");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        modelMapper.map(libraryCardRequest, libraryCardEntity);
        libraryCardEntity.setStatus(libraryCardRequest.getStatus());
        libraryCardRepository.save(libraryCardEntity);
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse deleteCardReader(String idCard) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            LibraryCardEntity libraryCardEntity = libraryCardRepository.findById(idCard).get();
            libraryCardRepository.delete(libraryCardEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
            return messageResponse;
        }catch (NoSuchElementException e) {
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            messageResponse.setMessage("No such library Card");
            return messageResponse;
        }
    }
}
