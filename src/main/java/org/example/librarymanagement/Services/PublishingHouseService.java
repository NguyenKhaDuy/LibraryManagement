package org.example.librarymanagement.Services;

import org.example.librarymanagement.Model.DTO.PublishingHouseDTO;
import org.example.librarymanagement.Model.Requests.PublishingHouseRequest;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface PublishingHouseService {
    Page<PublishingHouseDTO> getAllPublishingHouses(Integer pageNo);
    Object getPublishingHouseById(Long id);
    MessageResponse addPublishingHouse(PublishingHouseRequest publishingHouseRequest);
    MessageResponse updatePublishingHouse(PublishingHouseRequest publishingHouseRequest);
    MessageResponse deletePublishingHouse(Long id);
}
