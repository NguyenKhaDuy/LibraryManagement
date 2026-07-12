package org.example.librarymanagement.Services;

import org.example.librarymanagement.Model.DTO.BorrowTicketDTO;
import org.example.librarymanagement.Model.Requests.AcceptRequest;
import org.example.librarymanagement.Model.Requests.BorrowTicketRequest;
import org.example.librarymanagement.Model.Responses.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface BorrowTicketService {
    Page<BorrowTicketDTO> getAllBorrowTickets(Integer pageNo);
    Object getAllBorrowTicketsByReader(String idReader);
    Object getBorrowTicketById(String idTicker);

    //yêu cầu được tạo bởi nhân viên
    MessageResponse addBorrowTicket(BorrowTicketRequest borrowTicketRequest);

    //yêu cầu được tạo bởi reader
    MessageResponse addBorrowTicketByReader(BorrowTicketRequest borrowTicketRequest);

    //dùng khi từ chối hoăc chấp yêu cầu từ reader
    MessageResponse updateStatus(AcceptRequest acceptRequest);
    MessageResponse updateBorrowTicket(BorrowTicketRequest borrowTicketRequest);
    MessageResponse deleteBorrowTicket(String idTicket);
}
