package org.example.librarymanagement.Controllers;

import org.example.librarymanagement.Model.DTO.StatisticBooksDTO;
import org.example.librarymanagement.Model.DTO.StatisticBorrowDTO;
import org.example.librarymanagement.Model.DTO.StatisticUsersDTO;
import org.example.librarymanagement.Services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class StatisticController {
    @Autowired
    StatisticService statisticService;

    @GetMapping(value = "/api/admin/statistic/borrow")
    public ResponseEntity<Object> statisticBorrow(@RequestParam(name = "year", required = false) Integer year) {
        if (year == null){
            year = LocalDate.now().getYear();
        }
        StatisticBorrowDTO statisticBorrowDTO = statisticService.statisticBorrowYear(year);
        return new ResponseEntity<>(statisticBorrowDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/statistic/users")
    public ResponseEntity<Object> statisticUsers(@RequestParam(name = "year", required = false) Integer year) {
        if (year == null){
            year = LocalDate.now().getYear();
        }
        StatisticUsersDTO statisticUsersDTO = statisticService.statisticUsers(year);
        return new ResponseEntity<>(statisticUsersDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/api/admin/statistic/books")
    public ResponseEntity<Object> statisticBooks(@RequestParam(name = "year", required = false) Integer year) {
        if (year == null){
            year = LocalDate.now().getYear();
        }
        StatisticBooksDTO statisticBooksDTO = statisticService.statisticBooks(year);
        return new ResponseEntity<>(statisticBooksDTO, HttpStatus.OK);
    }
}
