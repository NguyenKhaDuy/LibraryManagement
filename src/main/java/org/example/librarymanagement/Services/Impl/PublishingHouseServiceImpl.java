package org.example.librarymanagement.Services.Impl;

import org.example.librarymanagement.Entity.PublishingHouseEntity;
import org.example.librarymanagement.Model.DTO.PublishingHouseDTO;
import org.example.librarymanagement.Model.Requests.PublishingHouseRequest;
import org.example.librarymanagement.Model.Responses.DataResponse;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.example.librarymanagement.Repository.PublishingHouseRepository;
import org.example.librarymanagement.Services.PublishingHouseService;
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
public class PublishingHouseServiceImpl implements PublishingHouseService {
    @Autowired
    PublishingHouseRepository publishingHouseRepository;
    @Autowired
    ModelMapper modelMapper;


    @Override
    public Page<PublishingHouseDTO> getAllPublishingHouses(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 10);
        Page<PublishingHouseEntity> publishingHouseEntities = publishingHouseRepository.findAll(pageable);
        List<PublishingHouseDTO> publishingHouseDTOS = publishingHouseEntities.stream().map(publishingHouseEntity -> {
            PublishingHouseDTO publishingHouseDTO = new PublishingHouseDTO();
            modelMapper.map(publishingHouseEntity, publishingHouseDTO);
            return publishingHouseDTO;
        }).toList();
        return new PageImpl<>(publishingHouseDTOS, publishingHouseEntities.getPageable(), publishingHouseEntities.getTotalElements());
    }

    @Override
    public DataResponse getAllPublishingHouses() {
        List<PublishingHouseEntity> publishingHouseEntities = publishingHouseRepository.findAll();
        List<PublishingHouseDTO> publishingHouseDTOS = publishingHouseEntities.stream().map(publishingHouseEntity -> {
            PublishingHouseDTO publishingHouseDTO = new PublishingHouseDTO();
            modelMapper.map(publishingHouseEntity, publishingHouseDTO);
            return publishingHouseDTO;
        }).toList();
        DataResponse dataResponse = new DataResponse();
        dataResponse.setData(publishingHouseDTOS);
        dataResponse.setStatus(HttpStatus.OK);
        dataResponse.setMessage("Success");
        return dataResponse;
    }

    @Override
    public Object getPublishingHouseById(Long id) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse dataResponse = new DataResponse();
        try{
            PublishingHouseEntity publishingHouseEntity = publishingHouseRepository.findById(id).get();
            PublishingHouseDTO publishingHouseDTO = new PublishingHouseDTO();
            modelMapper.map(publishingHouseEntity, publishingHouseDTO);
            dataResponse.setData(publishingHouseDTO);
            dataResponse.setMessage("Success");
            dataResponse.setStatus(HttpStatus.OK);
            return dataResponse;
        }catch (NoSuchElementException e){
            messageResponse.setMessage("Can not find publishing house with id " + id);
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }

    @Override
    public MessageResponse addPublishingHouse(PublishingHouseRequest publishingHouseRequest) {
        MessageResponse messageResponse = new MessageResponse();
        PublishingHouseEntity publishingHouseEntity = new PublishingHouseEntity();
        modelMapper.map(publishingHouseRequest, publishingHouseEntity);
        publishingHouseRepository.save(publishingHouseEntity);
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse updatePublishingHouse(PublishingHouseRequest publishingHouseRequest) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse dataResponse = new DataResponse();
        try{
            PublishingHouseEntity publishingHouseEntity = publishingHouseRepository.findById(publishingHouseRequest.getIdPublishingHouse()).get();
            modelMapper.map(publishingHouseRequest, publishingHouseEntity);
            publishingHouseRepository.save(publishingHouseEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
            return messageResponse;
        }catch (NoSuchElementException e){
            messageResponse.setMessage("Can not find publishing house with id " + publishingHouseRequest.getIdPublishingHouse());
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }

    @Override
    public MessageResponse deletePublishingHouse(Long id) {
        MessageResponse messageResponse = new MessageResponse();
        try{
            PublishingHouseEntity publishingHouseEntity = publishingHouseRepository.findById(id).get();
            publishingHouseRepository.delete(publishingHouseEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
            return messageResponse;
        }catch (NoSuchElementException e){
            messageResponse.setMessage("Can not find publishing house with id " + id);
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }
}
