package org.example.librarymanagement.Services.Impl;

import org.example.librarymanagement.Entity.*;
import org.example.librarymanagement.Model.DTO.LoginDTO;
import org.example.librarymanagement.Model.Requests.AddStaffRequest;
import org.example.librarymanagement.Model.Requests.LoginRequest;
import org.example.librarymanagement.Model.Requests.RegisterRequest;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.example.librarymanagement.Repository.AccountRepository;
import org.example.librarymanagement.Repository.CartRepository;
import org.example.librarymanagement.Repository.LibraryCardRepository;
import org.example.librarymanagement.Repository.ReaderRepository;
import org.example.librarymanagement.Repository.StaffRepository;
import org.example.librarymanagement.Services.AccountService;
import org.example.librarymanagement.Utils.ConvertByteToBase64;
import org.example.librarymanagement.Utils.RandomIdUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ReaderRepository readerRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    LibraryCardRepository libraryCardRepository;

    @Override
    public MessageResponse RegisterForUser(RegisterRequest registerRequest) {
        AccountEntity accountEntity = null;
        MessageResponse messageResponse = new MessageResponse();
        accountEntity = accountRepository.findByUserName(registerRequest.getUserName());
        if (accountEntity != null) {
            messageResponse.setMessage("Username already exists");
            messageResponse.setStatus(HttpStatus.BAD_REQUEST);
            return messageResponse;
        }
        accountEntity = new AccountEntity();
        accountEntity.setRole(Role.USER.toString());
        accountEntity.setStatus(Status.ACTIVE);
        accountEntity.setCreatedAt(LocalDateTime.now());
        modelMapper.map(registerRequest, accountEntity);
        accountEntity.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        accountRepository.save(accountEntity);

        ReadersEntity readersEntity = new ReadersEntity();
        modelMapper.map(registerRequest, readersEntity);
        readersEntity.setIdReader(RandomIdUtils.generateRandomId("RU", 15));
        readersEntity.setRegistrationDate(LocalDateTime.now());
        readersEntity.setStatus(Status.ACTIVE);
        readersEntity.setAccountEntity(accountEntity);
        readerRepository.save(readersEntity);

        CartEntity cartEntity = new CartEntity();
        cartEntity.setReadersEntity(readersEntity);
        cartRepository.save(cartEntity);

        LibraryCardEntity libraryCardEntity = new LibraryCardEntity();
        libraryCardEntity.setIdCard(RandomIdUtils.generateRandomId("C", 15));
        libraryCardEntity.setDateOfIssue(LocalDateTime.now());
        libraryCardEntity.setExpirationDate(LocalDate.now().plusYears(1));
        libraryCardEntity.setStatus(Status.ACTIVE);
        libraryCardEntity.setReadersEntity(readersEntity);
        libraryCardRepository.save(libraryCardEntity);

        messageResponse.setMessage("Account created successfully");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public MessageResponse AddStaff(AddStaffRequest addStaffRequest) {
        AccountEntity accountEntity = null;
        MessageResponse messageResponse = new MessageResponse();
        accountEntity = accountRepository.findByUserName(addStaffRequest.getUserName());
        if (accountEntity != null) {
            messageResponse.setMessage("Username already exists");
            messageResponse.setStatus(HttpStatus.BAD_REQUEST);
            return messageResponse;
        }
        accountEntity = new AccountEntity();
        accountEntity.setRole(Role.STAFF.toString());
        accountEntity.setStatus(Status.ACTIVE);
        accountEntity.setCreatedAt(LocalDateTime.now());
        modelMapper.map(addStaffRequest, accountEntity);
        accountEntity.setPassword(passwordEncoder.encode(addStaffRequest.getPassword()));
        accountRepository.save(accountEntity);

        StaffEntity staffEntity = new StaffEntity();
        modelMapper.map(addStaffRequest, staffEntity);
        staffEntity.setIdStaff(RandomIdUtils.generateRandomId("STU", 15));
        staffEntity.setStatus(Status.ACTIVE);
        staffEntity.setAccountEntity(accountEntity);
        staffRepository.save(staffEntity);
        messageResponse.setMessage("Account created successfully");
        messageResponse.setStatus(HttpStatus.OK);
        return messageResponse;
    }

    @Override
    public Object Login(LoginRequest loginRequest) {
        MessageResponse messageResponse = new MessageResponse();
        LoginDTO loginDTO = new LoginDTO();
        try {
            AccountEntity accountEntity = accountRepository.findByUserName(loginRequest.getUsername());
            if(passwordEncoder.matches(loginRequest.getPassword(), accountEntity.getPassword())) {
                loginDTO.setUsername(loginRequest.getUsername());
                loginDTO.setRole(accountEntity.getRole().toString());
                if(loginDTO.getRole().equals(Role.STAFF.toString())) {
                    loginDTO.setIdUser(accountEntity.getStaffEntity().getIdStaff());
                    loginDTO.setEmail(accountEntity.getStaffEntity().getEmail());
                    loginDTO.setAvatar(ConvertByteToBase64.toBase64(accountEntity.getStaffEntity().getAvatar()));
                }else if (loginDTO.getRole().equals(Role.USER.toString())) {
                    loginDTO.setIdUser(accountEntity.getReadersEntity().getIdReader());
                    loginDTO.setEmail(accountEntity.getReadersEntity().getEmail());
                    loginDTO.setAvatar(ConvertByteToBase64.toBase64(accountEntity.getReadersEntity().getAvatar()));
                }else {
                    loginDTO.setIdUser(accountEntity.getStaffEntity().getIdStaff());
                    loginDTO.setEmail(accountEntity.getStaffEntity().getEmail());
                }
                loginDTO.setUsername(loginRequest.getUsername());
                return loginDTO;
            }
        }catch (NoSuchElementException ex){
            messageResponse.setMessage("Account not exists");
            messageResponse.setStatus(HttpStatus.BAD_REQUEST);
            return messageResponse;
        }
        return loginDTO;
    }
}
