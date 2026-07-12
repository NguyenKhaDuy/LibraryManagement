package org.example.librarymanagement.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticBooksDTO {
    private long numberOfBooks; //tổng số sách
    private long numberOfBookBorrowed; // tổng số sách đang mượn (tức là không còn sách này trên kệ)
    private List<StatisticMonth> statisticMonths;
}
