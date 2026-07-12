package org.example.librarymanagement.Services.Impl;

import org.example.librarymanagement.Entity.*;
import org.example.librarymanagement.Model.DTO.*;
import org.example.librarymanagement.Repository.BookRepository;
import org.example.librarymanagement.Repository.BorrowTicketRepository;
import org.example.librarymanagement.Repository.ReaderRepository;
import org.example.librarymanagement.Repository.StaffRepository;
import org.example.librarymanagement.Services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    ReaderRepository readerRepository;
    @Autowired
    StaffRepository staffRepository;
    @Autowired
    BorrowTicketRepository borrowTicketRepository;


    @Override
    public StatisticBorrowDTO statisticBorrowYear(Integer year) {
        StatisticBorrowDTO statisticBorrowDTO = new StatisticBorrowDTO();
        LocalDate toDay = LocalDate.now();

        List<BorrowTicketEntity> borrowTicketEntities = borrowTicketRepository.findAll();

        long numberOfBorrowInDay = borrowTicketEntities
                .stream()
                .filter(borrowTicketEntity -> borrowTicketEntity.getBorrowingDate().toLocalDate().equals(toDay)).count();

        long numberOfBorrow = borrowTicketEntities.stream().count();

        List<StatisticMonth> statisticMonths = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            StatisticMonth statisticMonth = new StatisticMonth();
            statisticMonth.setMonth(i);
            Month month = Month.of(i);
            long count = borrowTicketEntities
                    .stream()
                    .filter(
                            borrowTicketEntity -> borrowTicketEntity.getBorrowingDate().getYear() == year &&
                                    borrowTicketEntity.getBorrowingDate().getMonth() == month).count();
            statisticMonth.setTotal(count);
            statisticMonths.add(statisticMonth);
        }

        statisticBorrowDTO.setNumberOfBorrow(numberOfBorrow);
        statisticBorrowDTO.setNumberOfBorrowInDay(numberOfBorrowInDay);
        statisticBorrowDTO.setStatisticMonths(statisticMonths);

        return statisticBorrowDTO;
    }

    @Override
    public StatisticUsersDTO statisticUsers(Integer year) {
        StatisticUsersDTO statisticUsersDTO = new StatisticUsersDTO();
        List<StaffEntity> staffEntities = staffRepository.findAll();
        List<ReadersEntity> readersEntities = readerRepository.findAll();

        statisticUsersDTO.setNumberOfStaff(staffEntities.size());
        statisticUsersDTO.setNumberOfReader(readersEntities.size());

        List<StatisticMonth> statisticStaffsMonths = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            StatisticMonth statisticMonth = new StatisticMonth();
            statisticMonth.setMonth(i);
            Month month = Month.of(i);
            long count = staffEntities
                    .stream()
                    .filter(
                            staffEntity -> staffEntity.getDateStart().getYear() == year &&
                                    staffEntity.getDateStart().getMonth() == month).count();
            statisticMonth.setTotal(count);
            statisticStaffsMonths.add(statisticMonth);
        }
        statisticUsersDTO.setStatisticStaffsMonths(statisticStaffsMonths);


        List<StatisticMonth> statisticReadersMonths = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            StatisticMonth statisticMonth = new StatisticMonth();
            statisticMonth.setMonth(i);
            Month month = Month.of(i);
            long count = readersEntities
                    .stream()
                    .filter(
                            readersEntity -> readersEntity.getRegistrationDate().getYear() == year &&
                                    readersEntity.getRegistrationDate().getMonth() == month).count();
            statisticMonth.setTotal(count);
            statisticReadersMonths.add(statisticMonth);
        }
        statisticUsersDTO.setStatisticReadersMonths(statisticReadersMonths);

        return statisticUsersDTO;
    }

    @Override
    public StatisticBooksDTO statisticBooks(Integer year) {
        StatisticBooksDTO statisticBooksDTO = new StatisticBooksDTO();
        List<BooksEntity> booksEntities = bookRepository.findAll();

        statisticBooksDTO.setNumberOfBooks(booksEntities.size());

        long numberOfBookBorrowed = booksEntities
                .stream()
                .filter(booksEntity -> booksEntity.getStatus().equals(Status.BORROWED)).count();
        statisticBooksDTO.setNumberOfBookBorrowed(numberOfBookBorrowed);

        List<StatisticMonth> statisticBooksMonths = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            StatisticMonth statisticMonth = new StatisticMonth();
            statisticMonth.setMonth(i);
            Month month = Month.of(i);

            long count = booksEntities
                    .stream()
                    .filter(
                            booksEntity -> booksEntity.getCreatedAt().getYear() == year &&
                                    booksEntity.getCreatedAt().getMonth() == month).count();
            statisticMonth.setTotal(count);
            statisticBooksMonths.add(statisticMonth);
        }
        statisticBooksDTO.setStatisticMonths(statisticBooksMonths);

        return statisticBooksDTO;
    }
}
