package org.example.librarymanagement.Controllers;

import org.example.librarymanagement.Model.DTO.PublishingHouseDTO;
import org.example.librarymanagement.Model.Requests.PublishingHouseRequest;
import org.example.librarymanagement.Model.Responses.DataResponse;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.example.librarymanagement.Services.PublishingHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PublishingHouseController {
    @Autowired
    PublishingHouseService publishingHouseService;

    @GetMapping(value = "/api/admin/publishing-house")
    public ResponseEntity<Object> getAllPublishingHouses(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        DataResponse dataResponse = new DataResponse();
        Page<PublishingHouseDTO> publishingHouseDTOS = publishingHouseService.getAllPublishingHouses(pageNo);
        dataResponse.setData(publishingHouseDTOS.getContent());
        dataResponse.setMessage("Success");
        dataResponse.setStatus(HttpStatus.OK);
        dataResponse.setTotal_pages(publishingHouseDTOS.getTotalPages());
        dataResponse.setCurrent_page(pageNo);
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/publishing-houses")
    public ResponseEntity<Object> getAllPublishingHouses() {
        DataResponse dataResponse = publishingHouseService.getAllPublishingHouses();
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/publishing-house/id={id}")
    public ResponseEntity<Object> getPublishingHouseById(@PathVariable("id") Long id) {
        Object result = publishingHouseService.getPublishingHouseById(id);
        if(result instanceof MessageResponse){
            return new ResponseEntity<>(result, ((MessageResponse) result).getStatus());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/api/admin/publishing-house")
    public ResponseEntity<Object> addPublishingHouse(@RequestBody PublishingHouseRequest publishingHouseRequest) {
        MessageResponse messageResponse = publishingHouseService.addPublishingHouse(publishingHouseRequest);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @PutMapping(value = "/api/admin/publishing-house")
    public ResponseEntity<Object> updatePublishingHouse(@RequestBody PublishingHouseRequest publishingHouseRequest) {
        MessageResponse messageResponse = publishingHouseService.updatePublishingHouse(publishingHouseRequest);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }

    @DeleteMapping(value = "/api/admin/publishing-house/id={id}")
    public ResponseEntity<Object> deletePublishingHouse(@PathVariable("id") Long id) {
        MessageResponse messageResponse = publishingHouseService.deletePublishingHouse(id);
        return new ResponseEntity<>(messageResponse, messageResponse.getStatus());
    }
}
