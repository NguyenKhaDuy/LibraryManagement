package org.example.librarymanagement.Services;

import org.example.librarymanagement.Model.DTO.ReaderDTO;
import org.example.librarymanagement.Model.DTO.StaffDTO;
import org.example.librarymanagement.Model.Requests.*;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface UsersService {
    Page<ReaderDTO> getAllReaders(Integer pageNo);
    Page<StaffDTO> getAllStaff(Integer pageNo);
    Object getReaderById(String idReader);
    Object getStaffById(String idStaff);
    MessageResponse updateReader(UserRequest userRequest);
    MessageResponse updateStaff(StaffRequest staffRequest);
    MessageResponse deleteReader(String idReader);
    MessageResponse deleteStaff(String idStaff);
    MessageResponse updateAvatarReader(UpdateAvatarRequest updateAvatarRequest);
    MessageResponse updateAvatarStaff(UpdateAvatarRequest updateAvatarRequest);
}
