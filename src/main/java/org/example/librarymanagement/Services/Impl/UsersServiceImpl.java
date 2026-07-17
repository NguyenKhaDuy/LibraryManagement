package org.example.librarymanagement.Services.Impl;

import org.example.librarymanagement.Entity.ReadersEntity;
import org.example.librarymanagement.Entity.StaffEntity;
import org.example.librarymanagement.Model.DTO.LibraryCardDTO;
import org.example.librarymanagement.Model.DTO.ReaderDTO;
import org.example.librarymanagement.Model.DTO.StaffDTO;
import org.example.librarymanagement.Model.Requests.StaffRequest;
import org.example.librarymanagement.Model.Requests.UpdateAvatarRequest;
import org.example.librarymanagement.Model.Requests.UserRequest;
import org.example.librarymanagement.Model.Responses.DataResponse;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.example.librarymanagement.Repository.LibraryCardRepository;
import org.example.librarymanagement.Repository.ReaderRepository;
import org.example.librarymanagement.Repository.StaffRepository;
import org.example.librarymanagement.Services.UsersService;
import org.example.librarymanagement.Utils.ConvertByteToBase64;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    ReaderRepository readerRepository;
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    LibraryCardRepository libraryCardRepository;
    @Autowired
    ModelMapper modelMapper;


    @Override
    public Page<ReaderDTO> getAllReaders(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 20);
        Page<ReadersEntity> readersEntities = readerRepository.findAll(pageable);
        List<ReaderDTO> readerDTOs = readersEntities.stream().map(readersEntity -> {
            ReaderDTO readerDTO = new ReaderDTO();
            modelMapper.map(readersEntity, readerDTO);
            readerDTO.setIdUser(readersEntity.getIdReader());
            List<LibraryCardDTO> libraryCardDTOS = readersEntity.getLibraryCardEntities().stream().map(libraryCardEntity -> {
                LibraryCardDTO libraryCardDTO = new LibraryCardDTO();
                modelMapper.map(libraryCardEntity, libraryCardDTO);
                return libraryCardDTO;
            }).toList();
            readerDTO.setAvatar(ConvertByteToBase64.toBase64(readersEntity.getAvatar()));
            readerDTO.setLibraryCardDTOS(libraryCardDTOS);
            return readerDTO;
        }).toList();
        return new PageImpl<>(readerDTOs, readersEntities.getPageable(), readersEntities.getTotalElements());
    }

    @Override
    public Page<StaffDTO> getAllStaff(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 20);
        Page<StaffEntity> staffEntities = staffRepository.findAll(pageable);
        List<StaffDTO> staffDTOS = staffEntities.stream().map(staffEntity -> {
            StaffDTO staffDTO = new StaffDTO();
            modelMapper.map(staffEntity, staffDTO);
            staffDTO.setIdUser(staffEntity.getIdStaff());
            staffDTO.setAvatar(ConvertByteToBase64.toBase64(staffEntity.getAvatar()));
            return staffDTO;
        }).toList();
        return new PageImpl<>(staffDTOS, staffEntities.getPageable(), staffEntities.getTotalElements());
    }

    @Override
    public Object getReaderById(String idReader) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse dataResponse = new DataResponse();
        try{
            ReadersEntity readersEntity = readerRepository.findById(idReader).get();
            ReaderDTO readerDTO = new ReaderDTO();
            modelMapper.map(readersEntity, readerDTO);
            readerDTO.setIdUser(readersEntity.getIdReader());
            readerDTO.setAvatar(ConvertByteToBase64.toBase64(readersEntity.getAvatar()));
            List<LibraryCardDTO> libraryCardDTOS = readersEntity.getLibraryCardEntities().stream().map(libraryCardEntity -> {
                LibraryCardDTO libraryCardDTO = new LibraryCardDTO();
                modelMapper.map(libraryCardEntity, libraryCardDTO);
                return libraryCardDTO;
            }).toList();
            readerDTO.setLibraryCardDTOS(libraryCardDTOS);
            dataResponse.setMessage("Success");
            dataResponse.setData(readerDTO);
            dataResponse.setStatus(HttpStatus.OK);
            return dataResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("No such reader");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }

    @Override
    public Object getStaffById(String idStaff) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse dataResponse = new DataResponse();
        try {
            StaffEntity staffEntity = staffRepository.findById(idStaff).get();
            StaffDTO staffDTO = new StaffDTO();
            modelMapper.map(staffEntity, staffDTO);
            staffDTO.setAvatar(ConvertByteToBase64.toBase64(staffEntity.getAvatar()));
            staffDTO.setIdUser(staffEntity.getIdStaff());
            dataResponse.setMessage("Success");
            dataResponse.setData(staffDTO);
            dataResponse.setStatus(HttpStatus.OK);
            return dataResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("No such staff");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }

    @Override
    public MessageResponse updateReader(UserRequest userRequest) {
        MessageResponse messageResponse = new MessageResponse();
        ReadersEntity readersEntity = null;
        try {
            readersEntity = readerRepository.findById(userRequest.getIdUser()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("No such reader");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        modelMapper.map(userRequest, readersEntity);
        readerRepository.save(readersEntity);
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse updateStaff(StaffRequest staffRequest) {
        MessageResponse messageResponse = new MessageResponse();
        StaffEntity staffEntity = null;
        try {
            staffEntity = staffRepository.findById(staffRequest.getIdUser()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("No such staff");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        modelMapper.map(staffRequest, staffEntity);
        staffRepository.save(staffEntity);
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse deleteReader(String idReader) {
        MessageResponse messageResponse = new MessageResponse();
        ReadersEntity readersEntity = null;
        try {
            readersEntity = readerRepository.findById(idReader).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("No such reader");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        readerRepository.delete(readersEntity);
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse deleteStaff(String idStaff) {
        MessageResponse messageResponse = new MessageResponse();
        StaffEntity staffEntity = null;
        try {
            staffEntity = staffRepository.findById(idStaff).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("No such staff");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        staffRepository.delete(staffEntity);
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse updateAvatarReader(UpdateAvatarRequest updateAvatarRequest) {
        MessageResponse messageResponse = new MessageResponse();
        ReadersEntity readersEntity = null;
        try {
            readersEntity = readerRepository.findById(updateAvatarRequest.getIdUser()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("No such reader");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        try {
            readersEntity.setAvatar(updateAvatarRequest.getAvatar().getBytes());
            readerRepository.save(readersEntity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse updateAvatarStaff(UpdateAvatarRequest updateAvatarRequest) {
        MessageResponse messageResponse = new MessageResponse();
        StaffEntity staffEntity = null;
        try {
            staffEntity = staffRepository.findById(updateAvatarRequest.getIdUser()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("No such staff");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        try {
            staffEntity.setAvatar(updateAvatarRequest.getAvatar().getBytes());
            staffRepository.save(staffEntity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }


}
