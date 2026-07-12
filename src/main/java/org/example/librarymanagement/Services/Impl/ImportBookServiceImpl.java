package org.example.librarymanagement.Services.Impl;

import org.example.librarymanagement.Entity.BooksEntity;
import org.example.librarymanagement.Entity.ImportBookDetailEntity;
import org.example.librarymanagement.Entity.ImportBookEntity;
import org.example.librarymanagement.Entity.StaffEntity;
import org.example.librarymanagement.Model.DTO.*;
import org.example.librarymanagement.Model.Requests.ImportBookRequest;
import org.example.librarymanagement.Model.Responses.DataResponse;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.example.librarymanagement.Repository.BookRepository;
import org.example.librarymanagement.Repository.ImportBookReopsitory;
import org.example.librarymanagement.Repository.StaffRepository;
import org.example.librarymanagement.Services.ImportBookService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ImportBookServiceImpl implements ImportBookService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    ImportBookReopsitory importBookReopsitory;
    @Autowired
    StaffRepository staffRepository;

    @Override
    public Page<ImportBookDTO> getImportBooks(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 20);
        Page<ImportBookEntity> importBookEntities = importBookReopsitory.findAll(pageable);
        List<ImportBookDTO> importBookDTOS = importBookEntities.stream().map(importBookEntity -> {
            ImportBookDTO importBookDTO = new ImportBookDTO();
            modelMapper.map(importBookEntity, importBookDTO);
            importBookDTO.setStaffId(importBookEntity.getStaffEntity().getIdStaff());
            importBookDTO.setStaffName(importBookEntity.getStaffEntity().getFullName());

            SupplierDTO supplierDTO = new SupplierDTO();
            modelMapper.map(importBookEntity.getSupplierEntity(), supplierDTO);
            importBookDTO.setSupplierDTO(supplierDTO);

            List<ImportBookDetailEntity> importBookDetailEntities = importBookEntity.getImportBookDetailEntities();
            List<ImportBookDetailDTO> importBookDetailDTOS = importBookDetailEntities.stream().map(importBookDetailEntity -> {
                ImportBookDetailDTO importBookDetailDTO = new ImportBookDetailDTO();
                modelMapper.map(importBookDetailEntity, importBookDetailDTO);

                BooksDTO booksDTO = new BooksDTO();
                modelMapper.map(importBookDetailEntity.getBooksEntity(), booksDTO);

                List<ImageDTO> imageDTOS = importBookDetailEntity.getBooksEntity().getImageEntities().stream().map(imageEntity -> {
                    ImageDTO imageDTO = new ImageDTO();
                    imageDTO.setIdImage(imageEntity.getIdImage());
                    imageDTO.setImageBase64(ConvertByteToBase64.toBase64(imageEntity.getImage()));
                    return imageDTO;
                }).toList();
                booksDTO.setImageDTOS(imageDTOS);

                //tác giả
                AuthorDTO authorDTO = new AuthorDTO();
                modelMapper.map(importBookDetailEntity.getBooksEntity().getAuthorEntity(), authorDTO);
                booksDTO.setAuthorDTO(authorDTO);

                //catefory
                CategoryDTO categoryDTO = new CategoryDTO();
                modelMapper.map(importBookDetailEntity.getBooksEntity().getCategoryEntity(), categoryDTO);
                booksDTO.setCategoryDTO(categoryDTO);

                //kệ sách
                BookshelfDTO bookshelfDTO = new BookshelfDTO();
                modelMapper.map(importBookDetailEntity.getBooksEntity().getBookshelfEntity(), bookshelfDTO);
                booksDTO.setBookshelfDTO(bookshelfDTO);

                //nhà xuất bản
                PublishingHouseDTO publishingHouseDTO = new PublishingHouseDTO();
                modelMapper.map(importBookDetailEntity.getBooksEntity().getPublishingHouseEntity(), publishingHouseDTO);
                booksDTO.setPublishingHouseDTO(publishingHouseDTO);

                importBookDetailDTO.setBooksDTO(booksDTO);
                return importBookDetailDTO;
            }).toList();
            importBookDTO.setImportBookDetailDTOS(importBookDetailDTOS);
            return importBookDTO;
        }).toList();

        return new PageImpl<>(importBookDTOS, importBookEntities.getPageable(), importBookEntities.getTotalElements());
    }

    @Override
    public Object getImportBook(String idImport) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse dataResponse = new DataResponse();
        try {
            ImportBookEntity importBookEntity = importBookReopsitory.findById(idImport).get();
            ImportBookDTO importBookDTO = new ImportBookDTO();
            modelMapper.map(importBookEntity, importBookDTO);
            importBookDTO.setStaffId(importBookEntity.getStaffEntity().getIdStaff());
            importBookDTO.setStaffName(importBookEntity.getStaffEntity().getFullName());

            SupplierDTO supplierDTO = new SupplierDTO();
            modelMapper.map(importBookEntity.getSupplierEntity(), supplierDTO);
            importBookDTO.setSupplierDTO(supplierDTO);

            List<ImportBookDetailEntity> importBookDetailEntities = importBookEntity.getImportBookDetailEntities();
            List<ImportBookDetailDTO> importBookDetailDTOS = importBookDetailEntities.stream().map(importBookDetailEntity -> {
                ImportBookDetailDTO importBookDetailDTO = new ImportBookDetailDTO();
                modelMapper.map(importBookDetailEntity, importBookDetailDTO);

                BooksDTO booksDTO = new BooksDTO();
                modelMapper.map(importBookDetailEntity.getBooksEntity(), booksDTO);

                List<ImageDTO> imageDTOS = importBookDetailEntity.getBooksEntity().getImageEntities().stream().map(imageEntity -> {
                    ImageDTO imageDTO = new ImageDTO();
                    imageDTO.setIdImage(imageEntity.getIdImage());
                    imageDTO.setImageBase64(ConvertByteToBase64.toBase64(imageEntity.getImage()));
                    return imageDTO;
                }).toList();
                booksDTO.setImageDTOS(imageDTOS);

                //tác giả
                AuthorDTO authorDTO = new AuthorDTO();
                modelMapper.map(importBookDetailEntity.getBooksEntity().getAuthorEntity(), authorDTO);
                booksDTO.setAuthorDTO(authorDTO);

                //catefory
                CategoryDTO categoryDTO = new CategoryDTO();
                modelMapper.map(importBookDetailEntity.getBooksEntity().getCategoryEntity(), categoryDTO);
                booksDTO.setCategoryDTO(categoryDTO);

                //kệ sách
                BookshelfDTO bookshelfDTO = new BookshelfDTO();
                modelMapper.map(importBookDetailEntity.getBooksEntity().getBookshelfEntity(), bookshelfDTO);
                booksDTO.setBookshelfDTO(bookshelfDTO);

                //nhà xuất bản
                PublishingHouseDTO publishingHouseDTO = new PublishingHouseDTO();
                modelMapper.map(importBookDetailEntity.getBooksEntity().getPublishingHouseEntity(), publishingHouseDTO);
                booksDTO.setPublishingHouseDTO(publishingHouseDTO);
                importBookDetailDTO.setBooksDTO(booksDTO);
                return importBookDetailDTO;
            }).toList();
            importBookDTO.setImportBookDetailDTOS(importBookDetailDTOS);
            dataResponse.setMessage("Success");
            dataResponse.setData(importBookDTO);
            dataResponse.setStatus(HttpStatus.OK);
            return dataResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Import Book Not Found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }

    @Override
    public MessageResponse addImportBook(ImportBookRequest importBookRequest) {
        MessageResponse messageResponse = new MessageResponse();
        StaffEntity staffEntity = new StaffEntity();
        ImportBookEntity importBookEntity = new ImportBookEntity();
        modelMapper.map(importBookRequest, importBookEntity);
        importBookEntity.setIdImport(RandomIdUtils.generateRandomId("IMP", 15));
        try {
            staffEntity = staffRepository.findById(importBookRequest.getStaffId()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Staff Not Found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        importBookEntity.setStaffEntity(staffEntity);

        List<ImportBookDetailEntity> importBookDetailEntities = new ArrayList<>();
        importBookRequest.getImportDetailBookRequests().stream().forEach(importDetailBookRequest -> {
            ImportBookDetailEntity importBookDetailEntity = new ImportBookDetailEntity();
            modelMapper.map(importDetailBookRequest, importBookDetailEntity);
            BooksEntity booksEntity= bookRepository.findById(importDetailBookRequest.getBookId()).get();
            importBookDetailEntity.setBooksEntity(booksEntity);
            importBookDetailEntities.add(importBookDetailEntity);
        });
        importBookEntity.setImportBookDetailEntities(importBookDetailEntities);
        importBookReopsitory.save(importBookEntity);
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse updateImportBook(ImportBookRequest importBookRequest) {
        MessageResponse messageResponse = new MessageResponse();
        StaffEntity staffEntity = null;
        ImportBookEntity importBookEntity = importBookReopsitory.findById(importBookRequest.getStaffId()).get();
        modelMapper.map(importBookRequest, importBookEntity);
        importBookEntity.setIdImport(RandomIdUtils.generateRandomId("IMP", 15));
        try {
            staffEntity = staffRepository.findById(importBookRequest.getStaffId()).get();
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Staff Not Found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        importBookEntity.setStaffEntity(staffEntity);

        importBookEntity.getImportBookDetailEntities().clear();

        List<ImportBookDetailEntity> importBookDetailEntities = new ArrayList<>();
        importBookRequest.getImportDetailBookRequests().stream().forEach(importDetailBookRequest -> {
            ImportBookDetailEntity importBookDetailEntity = new ImportBookDetailEntity();
            modelMapper.map(importDetailBookRequest, importBookDetailEntity);
            BooksEntity booksEntity= bookRepository.findById(importDetailBookRequest.getBookId()).get();
            importBookDetailEntity.setBooksEntity(booksEntity);
            importBookDetailEntities.add(importBookDetailEntity);
        });
        importBookEntity.setImportBookDetailEntities(importBookDetailEntities);
        importBookReopsitory.save(importBookEntity);
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse deleteImportBook(String idImport) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            ImportBookEntity importBookEntity = importBookReopsitory.findById(idImport).get();
            importBookReopsitory.delete(importBookEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
            return messageResponse;
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Import Book Not Found");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }
}
