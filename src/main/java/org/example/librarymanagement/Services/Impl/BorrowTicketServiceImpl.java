package org.example.librarymanagement.Services.Impl;

import org.example.librarymanagement.Entity.*;
import org.example.librarymanagement.Model.DTO.*;
import org.example.librarymanagement.Model.Requests.AcceptRequest;
import org.example.librarymanagement.Model.Requests.BorrowTicketRequest;
import org.example.librarymanagement.Model.Responses.DataResponse;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.example.librarymanagement.Repository.*;
import org.example.librarymanagement.Services.BorrowTicketService;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BorrowTicketServiceImpl implements BorrowTicketService {
    @Autowired
    BorrowTicketRepository borrowTicketRepository;
    @Autowired
    ReaderRepository readerRepository;
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    LibraryCardRepository libraryCardRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Page<BorrowTicketDTO> getAllBorrowTickets(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 20);
        Page<BorrowTicketEntity> borrowTicketEntities = borrowTicketRepository.findAll(pageable);
        List<BorrowTicketDTO> borrowTicketDTOS = borrowTicketEntities.stream().map(borrowTicketEntity -> {
            BorrowTicketDTO borrowTicketDTO = new BorrowTicketDTO();
            modelMapper.map(borrowTicketEntity, borrowTicketDTO);

            Reader reader = new Reader();
            modelMapper.map(borrowTicketEntity.getReadersEntity(), reader);
            reader.setAvatar(ConvertByteToBase64.toBase64(borrowTicketEntity.getReadersEntity().getAvatar()));
            borrowTicketDTO.setReader(reader);

            Staff staff = new Staff();
            modelMapper.map(borrowTicketEntity.getStaffEntity(), staff);
            staff.setAvatar(ConvertByteToBase64.toBase64(borrowTicketEntity.getStaffEntity().getAvatar()));
            borrowTicketDTO.setStaff(staff);

            List<BorrowDetailTicketEntity> borrowDetailTicketEntities = borrowTicketEntity.getBorrowDetailTicketEntities();
            List<BorrowDetailTicketDTO> borrowDetailTicketDTOS = borrowDetailTicketEntities.stream().map(borrowDetailTicketEntity -> {
                BorrowDetailTicketDTO borrowDetailTicketDTO = new BorrowDetailTicketDTO();
                modelMapper.map(borrowDetailTicketEntity, borrowDetailTicketDTO);

                BooksDTO booksDTO = new BooksDTO();
                modelMapper.map(borrowDetailTicketEntity.getBooksEntity(), booksDTO);
                List<ImageDTO> imageDTOS = borrowDetailTicketEntity.getBooksEntity().getImageEntities().stream().map(imageEntity -> {
                    ImageDTO imageDTO = new ImageDTO();
                    imageDTO.setIdImage(imageEntity.getIdImage());
                    imageDTO.setImageBase64(ConvertByteToBase64.toBase64(imageEntity.getImage()));
                    return imageDTO;
                }).toList();
                booksDTO.setImageDTOS(imageDTOS);

                //tác giả
                AuthorDTO authorDTO = new AuthorDTO();
                modelMapper.map(borrowDetailTicketEntity.getBooksEntity().getAuthorEntity(), authorDTO);
                booksDTO.setAuthorDTO(authorDTO);

                //catefory
                CategoryDTO categoryDTO = new CategoryDTO();
                modelMapper.map(borrowDetailTicketEntity.getBooksEntity().getCategoryEntity(), categoryDTO);
                booksDTO.setCategoryDTO(categoryDTO);


                //kệ sách
                BookshelfDTO bookshelfDTO = new BookshelfDTO();
                modelMapper.map(borrowDetailTicketEntity.getBooksEntity().getBookshelfEntity(), bookshelfDTO);
                booksDTO.setBookshelfDTO(bookshelfDTO);

                //nhà xuất bản
                PublishingHouseDTO publishingHouseDTO = new PublishingHouseDTO();
                modelMapper.map(borrowDetailTicketEntity.getBooksEntity().getPublishingHouseEntity(), publishingHouseDTO);
                booksDTO.setPublishingHouseDTO(publishingHouseDTO);

                borrowDetailTicketDTO.setBooksDTO(booksDTO);

                return borrowDetailTicketDTO;
            }).toList();
            borrowTicketDTO.setBorrowDetailTicketDTOS(borrowDetailTicketDTOS);

            return borrowTicketDTO;
        }).toList();
        return new PageImpl<>(borrowTicketDTOS, borrowTicketEntities.getPageable(), borrowTicketEntities.getTotalElements());
    }

    @Override
    public Object getAllBorrowTicketsByReader(String idReader) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse dataResponse = new DataResponse();
        ReadersEntity readersEntity = null;
        try {
            readersEntity = readerRepository.findById(idReader).get();
        } catch (NoSuchElementException ex) {
            messageResponse.setMessage("Can not find reader");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        List<BorrowTicketEntity> borrowTicketEntities = borrowTicketRepository.findByReadersEntity(readersEntity);
        List<BorrowTicketDTO> borrowTicketDTOS = borrowTicketEntities.stream().map(borrowTicketEntity -> {
            BorrowTicketDTO borrowTicketDTO = new BorrowTicketDTO();
            modelMapper.map(borrowTicketEntity, borrowTicketDTO);

            Reader reader = new Reader();
            modelMapper.map(borrowTicketEntity.getReadersEntity(), reader);
            reader.setAvatar(ConvertByteToBase64.toBase64(borrowTicketEntity.getReadersEntity().getAvatar()));
            borrowTicketDTO.setReader(reader);

            Staff staff = new Staff();
            modelMapper.map(borrowTicketEntity.getStaffEntity(), staff);
            staff.setAvatar(ConvertByteToBase64.toBase64(borrowTicketEntity.getStaffEntity().getAvatar()));
            borrowTicketDTO.setStaff(staff);

            List<BorrowDetailTicketEntity> borrowDetailTicketEntities = borrowTicketEntity.getBorrowDetailTicketEntities();
            List<BorrowDetailTicketDTO> borrowDetailTicketDTOS = borrowDetailTicketEntities.stream().map(borrowDetailTicketEntity -> {
                BorrowDetailTicketDTO borrowDetailTicketDTO = new BorrowDetailTicketDTO();
                modelMapper.map(borrowDetailTicketEntity, borrowDetailTicketDTO);

                BooksDTO booksDTO = new BooksDTO();
                modelMapper.map(borrowDetailTicketEntity.getBooksEntity(), booksDTO);
                List<ImageDTO> imageDTOS = borrowDetailTicketEntity.getBooksEntity().getImageEntities().stream().map(imageEntity -> {
                    ImageDTO imageDTO = new ImageDTO();
                    imageDTO.setIdImage(imageEntity.getIdImage());
                    imageDTO.setImageBase64(ConvertByteToBase64.toBase64(imageEntity.getImage()));
                    return imageDTO;
                }).toList();
                booksDTO.setImageDTOS(imageDTOS);

                //tác giả
                AuthorDTO authorDTO = new AuthorDTO();
                modelMapper.map(borrowDetailTicketEntity.getBooksEntity().getAuthorEntity(), authorDTO);
                booksDTO.setAuthorDTO(authorDTO);

                //catefory
                CategoryDTO categoryDTO = new CategoryDTO();
                modelMapper.map(borrowDetailTicketEntity.getBooksEntity().getCategoryEntity(), categoryDTO);
                booksDTO.setCategoryDTO(categoryDTO);


                //kệ sách
                BookshelfDTO bookshelfDTO = new BookshelfDTO();
                modelMapper.map(borrowDetailTicketEntity.getBooksEntity().getBookshelfEntity(), bookshelfDTO);
                booksDTO.setBookshelfDTO(bookshelfDTO);

                //nhà xuất bản
                PublishingHouseDTO publishingHouseDTO = new PublishingHouseDTO();
                modelMapper.map(borrowDetailTicketEntity.getBooksEntity().getPublishingHouseEntity(), publishingHouseDTO);
                booksDTO.setPublishingHouseDTO(publishingHouseDTO);

                borrowDetailTicketDTO.setBooksDTO(booksDTO);

                return borrowDetailTicketDTO;
            }).toList();
            borrowTicketDTO.setBorrowDetailTicketDTOS(borrowDetailTicketDTOS);

            return borrowTicketDTO;
        }).toList();
        dataResponse.setData(borrowTicketDTOS);
        dataResponse.setMessage("Success");
        dataResponse.setStatus(HttpStatus.OK);
        return dataResponse;
    }

    @Override
    public Object getBorrowTicketById(String idTicker) {
        MessageResponse messageResponse = new MessageResponse();
        DataResponse dataResponse = new DataResponse();
        try {
            BorrowTicketEntity borrowTicketEntity = borrowTicketRepository.findById(idTicker).get();
            BorrowTicketDTO borrowTicketDTO = new BorrowTicketDTO();
            modelMapper.map(borrowTicketEntity, borrowTicketDTO);

            Reader reader = new Reader();
            modelMapper.map(borrowTicketEntity.getReadersEntity(), reader);
            reader.setAvatar(ConvertByteToBase64.toBase64(borrowTicketEntity.getReadersEntity().getAvatar()));
            borrowTicketDTO.setReader(reader);

            Staff staff = new Staff();
            modelMapper.map(borrowTicketEntity.getStaffEntity(), staff);
            staff.setAvatar(ConvertByteToBase64.toBase64(borrowTicketEntity.getStaffEntity().getAvatar()));
            borrowTicketDTO.setStaff(staff);

            List<BorrowDetailTicketEntity> borrowDetailTicketEntities = borrowTicketEntity.getBorrowDetailTicketEntities();
            List<BorrowDetailTicketDTO> borrowDetailTicketDTOS = borrowDetailTicketEntities.stream().map(borrowDetailTicketEntity -> {
                BorrowDetailTicketDTO borrowDetailTicketDTO = new BorrowDetailTicketDTO();
                modelMapper.map(borrowDetailTicketEntity, borrowDetailTicketDTO);

                BooksDTO booksDTO = new BooksDTO();
                modelMapper.map(borrowDetailTicketEntity.getBooksEntity(), booksDTO);
                List<ImageDTO> imageDTOS = borrowDetailTicketEntity.getBooksEntity().getImageEntities().stream().map(imageEntity -> {
                    ImageDTO imageDTO = new ImageDTO();
                    imageDTO.setIdImage(imageEntity.getIdImage());
                    imageDTO.setImageBase64(ConvertByteToBase64.toBase64(imageEntity.getImage()));
                    return imageDTO;
                }).toList();
                booksDTO.setImageDTOS(imageDTOS);

                //tác giả
                AuthorDTO authorDTO = new AuthorDTO();
                modelMapper.map(borrowDetailTicketEntity.getBooksEntity().getAuthorEntity(), authorDTO);
                booksDTO.setAuthorDTO(authorDTO);

                //catefory
                CategoryDTO categoryDTO = new CategoryDTO();
                modelMapper.map(borrowDetailTicketEntity.getBooksEntity().getCategoryEntity(), categoryDTO);
                booksDTO.setCategoryDTO(categoryDTO);


                //kệ sách
                BookshelfDTO bookshelfDTO = new BookshelfDTO();
                modelMapper.map(borrowDetailTicketEntity.getBooksEntity().getBookshelfEntity(), bookshelfDTO);
                booksDTO.setBookshelfDTO(bookshelfDTO);

                //nhà xuất bản
                PublishingHouseDTO publishingHouseDTO = new PublishingHouseDTO();
                modelMapper.map(borrowDetailTicketEntity.getBooksEntity().getPublishingHouseEntity(), publishingHouseDTO);
                booksDTO.setPublishingHouseDTO(publishingHouseDTO);

                borrowDetailTicketDTO.setBooksDTO(booksDTO);

                return borrowDetailTicketDTO;
            }).toList();
            borrowTicketDTO.setBorrowDetailTicketDTOS(borrowDetailTicketDTOS);

            dataResponse.setData(borrowTicketDTO);
            dataResponse.setMessage("Success");
            dataResponse.setStatus(HttpStatus.OK);
            return dataResponse;
        } catch (NoSuchElementException ex) {
            messageResponse.setMessage("Can not find ticket");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }

    @Override
    public MessageResponse addBorrowTicket(BorrowTicketRequest borrowTicketRequest) {
        MessageResponse messageResponse = new MessageResponse();
        BorrowTicketEntity borrowTicketEntity = new BorrowTicketEntity();
        ReadersEntity readersEntity = new ReadersEntity();
        StaffEntity staffEntity = new StaffEntity();
        LibraryCardEntity libraryCardEntity = null;
        try {
            readersEntity = readerRepository.findById(borrowTicketRequest.getReaderId()).get();
            libraryCardEntity = libraryCardRepository.findById(borrowTicketRequest.getCardLibraryId()).get();
            if (!readersEntity.getStatus().equals(Status.ACTIVE) && !readersEntity.getAccountEntity().getStatus().equals(Status.ACTIVE)) {
                messageResponse.setMessage("Reader is not active, reader " + readersEntity.getStatus().toString());
                messageResponse.setStatus(HttpStatus.BAD_REQUEST);
                return messageResponse;
            }
            if (libraryCardEntity.getExpirationDate().isBefore(LocalDate.now())) {
                messageResponse.setMessage("Library Card expired");
                messageResponse.setStatus(HttpStatus.BAD_REQUEST);
                return messageResponse;
            }
        } catch (NoSuchElementException ex) {
            messageResponse.setMessage("Can not find reader");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        try {
            staffEntity = staffRepository.findById(borrowTicketRequest.getStaffId()).get();
        } catch (NoSuchElementException ex) {
            messageResponse.setMessage("Can not find staff");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }

        for (BorrowDetailTicketEntity borrowDetailTicketEntity : borrowTicketEntity.getBorrowDetailTicketEntities()) {
            BooksEntity booksEntity = bookRepository.findById(borrowDetailTicketEntity.getBooksEntity().getIdBook()).get();
            Long newQuantity = booksEntity.getQuantity() - 1;
            booksEntity.setQuantity(newQuantity);
            if (newQuantity == 0) {
                booksEntity.setStatus(Status.BORROWED);
            }
            bookRepository.save(booksEntity);
        }

        modelMapper.map(borrowTicketRequest, borrowTicketEntity);

        //id
        borrowTicketEntity.setIdTicket(RandomIdUtils.generateRandomId("T", 15));

        //ticket detail
        List<BorrowDetailTicketEntity> borrowDetailTicketEntities = new ArrayList<>();
        borrowTicketRequest.getBorrowTicketDetailRequests().stream().forEach(borrowTicketDetailRequest -> {
            BorrowDetailTicketEntity borrowDetailTicketEntity = new BorrowDetailTicketEntity();
            modelMapper.map(borrowTicketDetailRequest, borrowDetailTicketEntity);
            BooksEntity booksEntity = bookRepository.findById(borrowTicketDetailRequest.getBookId()).get();
            borrowDetailTicketEntity.setBooksEntity(booksEntity);
            borrowDetailTicketEntity.setBorrowTicketEntity(borrowTicketEntity);
            borrowDetailTicketEntities.add(borrowDetailTicketEntity);
        });
        borrowTicketEntity.setBorrowDetailTicketEntities(borrowDetailTicketEntities);

        borrowTicketEntity.setStatus(Status.BORROWING);

        //staff
        borrowTicketEntity.setStaffEntity(staffEntity);

        //reader
        borrowTicketEntity.setReadersEntity(readersEntity);

        borrowTicketEntity.setStatus(Status.APPROVED);

        borrowTicketRepository.save(borrowTicketEntity);

        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse addBorrowTicketByReader(BorrowTicketRequest borrowTicketRequest) {
        MessageResponse messageResponse = new MessageResponse();
        BorrowTicketEntity borrowTicketEntity = new BorrowTicketEntity();
        ReadersEntity readersEntity = new ReadersEntity();
        LibraryCardEntity libraryCardEntity = null;
        try {
            readersEntity = readerRepository.findById(borrowTicketRequest.getReaderId()).get();
            libraryCardEntity = libraryCardRepository.findById(borrowTicketRequest.getCardLibraryId()).get();
            if (!readersEntity.getStatus().equals(Status.ACTIVE) && !readersEntity.getAccountEntity().getStatus().equals(Status.ACTIVE)) {
                messageResponse.setMessage("Reader is not active, reader " + readersEntity.getStatus().toString());
                messageResponse.setStatus(HttpStatus.BAD_REQUEST);
                return messageResponse;
            }
            if (!libraryCardEntity.getExpirationDate().isBefore(LocalDate.now())) {
                messageResponse.setMessage("Library Card expired");
                messageResponse.setStatus(HttpStatus.BAD_REQUEST);
                return messageResponse;
            }
        } catch (NoSuchElementException ex) {
            messageResponse.setMessage("Can not find reader");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        modelMapper.map(borrowTicketRequest, borrowTicketEntity);

        //id
        borrowTicketEntity.setIdTicket(RandomIdUtils.generateRandomId("T", 15));

        //ticket detail
        List<BorrowDetailTicketEntity> borrowDetailTicketEntities = new ArrayList<>();
        borrowTicketRequest.getBorrowTicketDetailRequests().stream().forEach(borrowTicketDetailRequest -> {
            BorrowDetailTicketEntity borrowDetailTicketEntity = new BorrowDetailTicketEntity();
            modelMapper.map(borrowTicketDetailRequest, borrowDetailTicketEntity);
            BooksEntity booksEntity = bookRepository.findById(borrowTicketDetailRequest.getBookId()).get();
            borrowDetailTicketEntity.setBooksEntity(booksEntity);
            borrowDetailTicketEntities.add(borrowDetailTicketEntity);
        });
        borrowTicketEntity.setBorrowDetailTicketEntities(borrowDetailTicketEntities);

        //reader
        borrowTicketEntity.setReadersEntity(readersEntity);

        borrowTicketEntity.setStatus(Status.WAITTING);

        borrowTicketRepository.save(borrowTicketEntity);

        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse updateStatus(AcceptRequest acceptRequest) {
        MessageResponse messageResponse = new MessageResponse();
        StaffEntity staffEntity = null;
        BorrowTicketEntity borrowTicketEntity = null;
        try {
            staffEntity = staffRepository.findById(acceptRequest.getStaffId()).get();
        } catch (NoSuchElementException ex) {
            messageResponse.setMessage("Can not find staff");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
        try {
            borrowTicketEntity = borrowTicketRepository.findById(acceptRequest.getTicketId()).get();
        } catch (NoSuchElementException ex) {
            messageResponse.setMessage("Can not find ticket");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }

        if (acceptRequest.getStatus().equals(Status.APPROVED)) {
            for (BorrowDetailTicketEntity borrowDetailTicketEntity : borrowTicketEntity.getBorrowDetailTicketEntities()) {
                BooksEntity booksEntity = bookRepository.findById(borrowDetailTicketEntity.getBooksEntity().getIdBook()).get();
                Long newQuantity = booksEntity.getQuantity() - 1;
                booksEntity.setQuantity(newQuantity);
                if (newQuantity == 0) {
                    booksEntity.setStatus(Status.BORROWED);
                }
                bookRepository.save(booksEntity);
            }
        }

        borrowTicketEntity.setStaffEntity(staffEntity);
        borrowTicketEntity.setStatus(acceptRequest.getStatus());
        borrowTicketRepository.save(borrowTicketEntity);
        messageResponse.setMessage("Success");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse updateBorrowTicket(BorrowTicketRequest borrowTicketRequest) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            BorrowTicketEntity borrowTicketEntity = borrowTicketRepository.findById(borrowTicketRequest.getIdTicket()).get();
            modelMapper.map(borrowTicketRequest, borrowTicketEntity);

            //ticket detail
            List<BorrowDetailTicketEntity> borrowDetailTicketEntities = new ArrayList<>();
            borrowTicketRequest.getBorrowTicketDetailRequests().stream().forEach(borrowTicketDetailRequest -> {
                BorrowDetailTicketEntity borrowDetailTicketEntity = new BorrowDetailTicketEntity();
                modelMapper.map(borrowTicketDetailRequest, borrowDetailTicketEntity);
                BooksEntity booksEntity = bookRepository.findById(borrowTicketDetailRequest.getBookId()).get();
                borrowDetailTicketEntity.setBooksEntity(booksEntity);
                borrowDetailTicketEntities.add(borrowDetailTicketEntity);
            });
            borrowTicketEntity.setBorrowDetailTicketEntities(borrowDetailTicketEntities);

            if (borrowTicketRequest.getStatus().equals(Status.COMPLETED)) {
                borrowTicketEntity.getBorrowDetailTicketEntities().stream().forEach(borrowDetailTicketEntity -> {
                   BooksEntity booksEntity = borrowDetailTicketEntity.getBooksEntity();
                   if (booksEntity.getStatus().equals(Status.BORROWED)) {
                       Long newQuantity = booksEntity.getQuantity() + 1;
                       booksEntity.setQuantity(newQuantity);
                       booksEntity.setStatus(Status.AVAILABLE);
                       bookRepository.save(booksEntity);
                   }else {
                       Long newQuantity = booksEntity.getQuantity() + 1;
                       booksEntity.setQuantity(newQuantity);
                       bookRepository.save(booksEntity);
                   }
                });
            }

            borrowTicketEntity.setStatus(borrowTicketRequest.getStatus());
            borrowTicketRepository.save(borrowTicketEntity);

            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
            return messageResponse;
        } catch (NoSuchElementException ex) {
            messageResponse.setMessage("Can not find ticket");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }

    @Override
    public MessageResponse deleteBorrowTicket(String idTicket) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            BorrowTicketEntity borrowTicketEntity = borrowTicketRepository.findById(idTicket).get();
            borrowTicketRepository.delete(borrowTicketEntity);
            messageResponse.setMessage("Success");
            messageResponse.setStatus(HttpStatus.OK);
            return messageResponse;
        } catch (NoSuchElementException ex) {
            messageResponse.setMessage("Can not find ticket");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }
    }
}
