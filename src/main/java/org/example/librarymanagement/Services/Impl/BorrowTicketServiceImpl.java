package org.example.librarymanagement.Services.Impl;

import org.example.librarymanagement.Entity.*;
import org.example.librarymanagement.Model.DTO.*;
import org.example.librarymanagement.Model.Requests.AcceptRequest;
import org.example.librarymanagement.Model.Requests.BorrowTicketDetailRequest;
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
import java.time.LocalDateTime;
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
        List<BorrowTicketDTO> borrowTicketDTOS = borrowTicketEntities.stream()
                .map(this::toBorrowTicketDTO)
                .toList();
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
        List<BorrowTicketDTO> borrowTicketDTOS = borrowTicketEntities.stream()
                .map(this::toBorrowTicketDTO)
                .toList();
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
            dataResponse.setData(toBorrowTicketDTO(borrowTicketEntity));
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

        ReadersEntity readersEntity = findReader(borrowTicketRequest.getReaderId(), messageResponse);
        if (readersEntity == null) {
            return messageResponse;
        }
        if (!validateReaderActive(readersEntity, messageResponse)) {
            return messageResponse;
        }
        if (resolveLibraryCard(borrowTicketRequest.getCardLibraryId(), readersEntity, messageResponse) == null) {
            return messageResponse;
        }

        StaffEntity staffEntity;
        if (isBlank(borrowTicketRequest.getStaffId())) {
            messageResponse.setMessage("Staff id is required");
            messageResponse.setStatus(HttpStatus.BAD_REQUEST);
            return messageResponse;
        }
        try {
            staffEntity = staffRepository.findById(borrowTicketRequest.getStaffId()).get();
        } catch (NoSuchElementException ex) {
            messageResponse.setMessage("Can not find staff");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return messageResponse;
        }

        modelMapper.map(borrowTicketRequest, borrowTicketEntity);
        borrowTicketEntity.setIdTicket(RandomIdUtils.generateRandomId("T", 15));

        List<BorrowDetailTicketEntity> borrowDetailTicketEntities = buildBorrowDetails(borrowTicketRequest, borrowTicketEntity, messageResponse);
        if (borrowDetailTicketEntities == null) {
            return messageResponse;
        }
        borrowTicketEntity.setBorrowDetailTicketEntities(borrowDetailTicketEntities);

        for (BorrowDetailTicketEntity borrowDetailTicketEntity : borrowDetailTicketEntities) {
            BooksEntity booksEntity = borrowDetailTicketEntity.getBooksEntity();
            Long newQuantity = booksEntity.getQuantity() - 1;
            booksEntity.setQuantity(newQuantity);
            if (newQuantity == 0) {
                booksEntity.setStatus(Status.BORROWED);
            }
            bookRepository.save(booksEntity);
        }

        borrowTicketEntity.setStaffEntity(staffEntity);
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

        ReadersEntity readersEntity = findReader(borrowTicketRequest.getReaderId(), messageResponse);
        if (readersEntity == null) {
            return messageResponse;
        }
        if (!validateReaderActive(readersEntity, messageResponse)) {
            return messageResponse;
        }
        if (resolveLibraryCard(borrowTicketRequest.getCardLibraryId(), readersEntity, messageResponse) == null) {
            return messageResponse;
        }

        modelMapper.map(borrowTicketRequest, borrowTicketEntity);
        borrowTicketEntity.setIdTicket(RandomIdUtils.generateRandomId("T", 15));

        List<BorrowDetailTicketEntity> borrowDetailTicketEntities = buildBorrowDetails(borrowTicketRequest, borrowTicketEntity, messageResponse);
        if (borrowDetailTicketEntities == null) {
            return messageResponse;
        }
        borrowTicketEntity.setBorrowDetailTicketEntities(borrowDetailTicketEntities);
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
            borrowTicketEntity.getBorrowDetailTicketEntities().clear();
            borrowTicketRequest.getBorrowTicketDetailRequests().stream().forEach(borrowTicketDetailRequest -> {
                BorrowDetailTicketEntity borrowDetailTicketEntity = new BorrowDetailTicketEntity();
                modelMapper.map(borrowTicketDetailRequest, borrowDetailTicketEntity);
                BooksEntity booksEntity = bookRepository.findById(borrowTicketDetailRequest.getBookId()).get();
                borrowDetailTicketEntity.setBooksEntity(booksEntity);
                borrowDetailTicketEntity.setBorrowTicketEntity(borrowTicketEntity);
                borrowTicketEntity.getBorrowDetailTicketEntities().add(borrowDetailTicketEntity);
            });

            if (borrowTicketRequest.getStatus() != null && borrowTicketRequest.getStatus().equals(Status.COMPLETED)) {
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

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private ReadersEntity findReader(String readerId, MessageResponse messageResponse) {
        if (isBlank(readerId)) {
            messageResponse.setMessage("Reader id is required");
            messageResponse.setStatus(HttpStatus.BAD_REQUEST);
            return null;
        }
        try {
            return readerRepository.findById(readerId).get();
        } catch (NoSuchElementException ex) {
            messageResponse.setMessage("Can not find reader");
            messageResponse.setStatus(HttpStatus.NOT_FOUND);
            return null;
        }
    }

    private boolean validateReaderActive(ReadersEntity readersEntity, MessageResponse messageResponse) {
        boolean readerActive = Status.ACTIVE.equals(readersEntity.getStatus());
        boolean accountActive = readersEntity.getAccountEntity() != null
                && Status.ACTIVE.equals(readersEntity.getAccountEntity().getStatus());
        if (!readerActive && !accountActive) {
            messageResponse.setMessage("Reader is not active, reader " + readersEntity.getStatus());
            messageResponse.setStatus(HttpStatus.BAD_REQUEST);
            return false;
        }
        return true;
    }

    private LibraryCardEntity resolveLibraryCard(String cardLibraryId, ReadersEntity readersEntity, MessageResponse messageResponse) {
        LibraryCardEntity libraryCardEntity;
        if (!isBlank(cardLibraryId)) {
            try {
                libraryCardEntity = libraryCardRepository.findById(cardLibraryId).get();
            } catch (NoSuchElementException ex) {
                messageResponse.setMessage("Can not find library card");
                messageResponse.setStatus(HttpStatus.NOT_FOUND);
                return null;
            }
        } else {
            libraryCardEntity = findValidLibraryCard(readersEntity.getIdReader());
            if (libraryCardEntity == null) {
                libraryCardEntity = createDefaultLibraryCard(readersEntity);
            }
        }
        if (libraryCardEntity.getExpirationDate() != null && libraryCardEntity.getExpirationDate().isBefore(LocalDate.now())) {
            messageResponse.setMessage("Library Card expired");
            messageResponse.setStatus(HttpStatus.BAD_REQUEST);
            return null;
        }
        return libraryCardEntity;
    }

    private LibraryCardEntity findValidLibraryCard(String idReader) {
        LibraryCardEntity libraryCardEntity = libraryCardRepository.findByReadersEntity_IdReaderAndStatus(idReader, Status.ACTIVE)
                .stream()
                .filter(this::isLibraryCardValid)
                .findFirst()
                .orElse(null);
        if (libraryCardEntity != null) {
            return libraryCardEntity;
        }
        return libraryCardRepository.findByReadersEntity_IdReader(idReader)
                .stream()
                .filter(this::isLibraryCardValid)
                .findFirst()
                .orElse(null);
    }

    private boolean isLibraryCardValid(LibraryCardEntity card) {
        return card.getExpirationDate() == null || !card.getExpirationDate().isBefore(LocalDate.now());
    }

    private LibraryCardEntity createDefaultLibraryCard(ReadersEntity readersEntity) {
        LibraryCardEntity libraryCardEntity = new LibraryCardEntity();
        libraryCardEntity.setIdCard(RandomIdUtils.generateRandomId("C", 15));
        libraryCardEntity.setDateOfIssue(LocalDateTime.now());
        libraryCardEntity.setExpirationDate(LocalDate.now().plusYears(1));
        libraryCardEntity.setStatus(Status.ACTIVE);
        libraryCardEntity.setReadersEntity(readersEntity);
        return libraryCardRepository.save(libraryCardEntity);
    }

    private List<BorrowDetailTicketEntity> buildBorrowDetails(BorrowTicketRequest borrowTicketRequest, BorrowTicketEntity borrowTicketEntity, MessageResponse messageResponse) {
        if (borrowTicketRequest.getBorrowTicketDetailRequests() == null || borrowTicketRequest.getBorrowTicketDetailRequests().isEmpty()) {
            messageResponse.setMessage("At least one book is required");
            messageResponse.setStatus(HttpStatus.BAD_REQUEST);
            return null;
        }
        List<BorrowDetailTicketEntity> borrowDetailTicketEntities = new ArrayList<>();
        for (BorrowTicketDetailRequest borrowTicketDetailRequest : borrowTicketRequest.getBorrowTicketDetailRequests()) {
            if (isBlank(borrowTicketDetailRequest.getBookId())) {
                messageResponse.setMessage("Book id is required");
                messageResponse.setStatus(HttpStatus.BAD_REQUEST);
                return null;
            }
            try {
                BorrowDetailTicketEntity borrowDetailTicketEntity = new BorrowDetailTicketEntity();
                modelMapper.map(borrowTicketDetailRequest, borrowDetailTicketEntity);
                BooksEntity booksEntity = bookRepository.findById(borrowTicketDetailRequest.getBookId()).get();
                borrowDetailTicketEntity.setBooksEntity(booksEntity);
                borrowDetailTicketEntity.setBorrowTicketEntity(borrowTicketEntity);
                borrowDetailTicketEntities.add(borrowDetailTicketEntity);
            } catch (NoSuchElementException ex) {
                messageResponse.setMessage("Can not find book");
                messageResponse.setStatus(HttpStatus.NOT_FOUND);
                return null;
            }
        }
        return borrowDetailTicketEntities;
    }

    private BorrowTicketDTO toBorrowTicketDTO(BorrowTicketEntity borrowTicketEntity) {
        BorrowTicketDTO borrowTicketDTO = new BorrowTicketDTO();
        modelMapper.map(borrowTicketEntity, borrowTicketDTO);
        borrowTicketDTO.setReader(toReader(borrowTicketEntity.getReadersEntity()));
        borrowTicketDTO.setStaff(toStaff(borrowTicketEntity.getStaffEntity()));
        borrowTicketDTO.setBorrowDetailTicketDTOS(toBorrowDetailDTOs(borrowTicketEntity.getBorrowDetailTicketEntities()));
        return borrowTicketDTO;
    }

    private Reader toReader(ReadersEntity readersEntity) {
        if (readersEntity == null) {
            return null;
        }
        Reader reader = new Reader();
        modelMapper.map(readersEntity, reader);
        reader.setAvatar(ConvertByteToBase64.toBase64(readersEntity.getAvatar()));
        return reader;
    }

    private Staff toStaff(StaffEntity staffEntity) {
        if (staffEntity == null) {
            return null;
        }
        Staff staff = new Staff();
        modelMapper.map(staffEntity, staff);
        staff.setAvatar(ConvertByteToBase64.toBase64(staffEntity.getAvatar()));
        return staff;
    }

    private List<BorrowDetailTicketDTO> toBorrowDetailDTOs(List<BorrowDetailTicketEntity> borrowDetailTicketEntities) {
        if (borrowDetailTicketEntities == null) {
            return List.of();
        }
        return borrowDetailTicketEntities.stream()
                .map(this::toBorrowDetailDTO)
                .toList();
    }

    private BorrowDetailTicketDTO toBorrowDetailDTO(BorrowDetailTicketEntity borrowDetailTicketEntity) {
        BorrowDetailTicketDTO borrowDetailTicketDTO = new BorrowDetailTicketDTO();
        modelMapper.map(borrowDetailTicketEntity, borrowDetailTicketDTO);
        BooksEntity booksEntity = borrowDetailTicketEntity.getBooksEntity();
        if (booksEntity != null) {
            borrowDetailTicketDTO.setBooksDTO(toBooksDTO(booksEntity));
        }
        return borrowDetailTicketDTO;
    }

    private BooksDTO toBooksDTO(BooksEntity booksEntity) {
        BooksDTO booksDTO = new BooksDTO();
        modelMapper.map(booksEntity, booksDTO);

        List<ImageEntity> imageEntities = booksEntity.getImageEntities() != null
                ? booksEntity.getImageEntities()
                : List.of();
        booksDTO.setImageDTOS(imageEntities.stream().map(imageEntity -> {
            ImageDTO imageDTO = new ImageDTO();
            imageDTO.setIdImage(imageEntity.getIdImage());
            imageDTO.setImageBase64(ConvertByteToBase64.toBase64(imageEntity.getImage()));
            return imageDTO;
        }).toList());

        if (booksEntity.getAuthorEntity() != null) {
            AuthorDTO authorDTO = new AuthorDTO();
            modelMapper.map(booksEntity.getAuthorEntity(), authorDTO);
            booksDTO.setAuthorDTO(authorDTO);
        }

        if (booksEntity.getCategoryEntity() != null) {
            CategoryDTO categoryDTO = new CategoryDTO();
            modelMapper.map(booksEntity.getCategoryEntity(), categoryDTO);
            booksDTO.setCategoryDTO(categoryDTO);
        }

        if (booksEntity.getBookshelfEntity() != null) {
            BookshelfDTO bookshelfDTO = new BookshelfDTO();
            modelMapper.map(booksEntity.getBookshelfEntity(), bookshelfDTO);
            booksDTO.setBookshelfDTO(bookshelfDTO);
        }

        if (booksEntity.getPublishingHouseEntity() != null) {
            PublishingHouseDTO publishingHouseDTO = new PublishingHouseDTO();
            modelMapper.map(booksEntity.getPublishingHouseEntity(), publishingHouseDTO);
            booksDTO.setPublishingHouseDTO(publishingHouseDTO);
        }

        return booksDTO;
    }
}
