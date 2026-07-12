package org.example.librarymanagement.Model.Requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.librarymanagement.Entity.Status;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BorrowTicketRequest {
    private String idTicket;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime borrowingDate;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dueDate;
    private Long quantity;
    private Double totalFine;
    private String readerId; //không được update reader (không cần truyền lên khi update)
    private List<BorrowTicketDetailRequest> borrowTicketDetailRequests;
    private String staffId; //chỉ truyền khi yêu cầu được nhập trực tiếp bởi nhân viên, và không được update nhân viên tạo hoặc chấp nhận yêu cầu (không cần truyền lên khi uodate)
    private Status status; // chỉ truyền khi update
    private String cardLibraryId; // không được update (không cần truyền khi update)
}
