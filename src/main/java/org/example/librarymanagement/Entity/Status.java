package org.example.librarymanagement.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum Status {
    AVAILABLE,   // Có sẵn
    BORROWING,   // Đang được mượn
    RETURNED,    // Đã trả
    OVERDUE,     // Quá hạn
    LOST,        // Bị mất
    DAMAGED      // Bị hỏng
}
