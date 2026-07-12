package org.example.librarymanagement.Entity;

public enum Status {

    // ===== Book =====

    AVAILABLE,     // Sách có sẵn trong thư viện
    BORROWED,      // Sách đang được mượn
    RESERVED,      // Sách đã được độc giả đặt trước
    RETURNED,      // Sách đã được trả về thư viện
    OVERDUE,       // Sách quá hạn trả
    LOST,          // Sách bị mất
    DAMAGED,       // Sách bị hư hỏng
    MAINTENANCE,   // Sách đang bảo trì, sửa chữa
    REMOVED,       // Sách đã thanh lý hoặc ngừng lưu hành

    // ===== Account / Reader =====

    PENDING,       // Chờ kích hoạt hoặc chờ phê duyệt
    ACTIVE,        // Đang hoạt động
    INACTIVE,      // Không hoạt động
    LOCKED,        // Bị khóa
    SUSPENDED,     // Tạm đình chỉ sử dụng
    EXPIRED,       // Hết hạn (thẻ thư viện, tài khoản...)
    DEBT,          // Đang nợ sách hoặc phí phạt
    BANNED,        // Bị cấm sử dụng thư viện
    DISABLED,      // Đã vô hiệu hóa
    DELETED,       // Đã xóa (xóa mềm)

    // ===== Bookshelf =====

    EMPTY,         // Kệ trống
    FULL,          // Kệ đã đầy

    // ===== Reservation =====

    WAITTING,        //Chờ nhan viên duyệt
    APPROVED,      // Đã phê duyệt yêu cầu
    REJECTED,      // Từ chối yêu cầu
    CANCELLED,     // Đã hủy
    COMPLETED,       //Đã trả
    BORROWING          //Đang mượn
}
