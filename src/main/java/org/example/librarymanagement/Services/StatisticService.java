package org.example.librarymanagement.Services;

import org.example.librarymanagement.Model.DTO.StatisticBorrowDTO;
import org.example.librarymanagement.Model.DTO.StatisticBooksDTO;
import org.example.librarymanagement.Model.DTO.StatisticUsersDTO;
import org.springframework.stereotype.Service;

@Service
public interface StatisticService {
    StatisticBorrowDTO statisticBorrowYear(Integer year);
    StatisticUsersDTO statisticUsers(Integer year);
    StatisticBooksDTO statisticBooks(Integer year);
}
