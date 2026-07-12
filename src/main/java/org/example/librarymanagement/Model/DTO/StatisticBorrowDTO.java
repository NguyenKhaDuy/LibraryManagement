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
public class StatisticBorrowDTO {
    private long numberOfBorrowInDay; // tổng sách mượn trong hôm nay
    private long numberOfBorrow;  //tổng số lượt mượn
    private List<StatisticMonth> statisticMonths;
}
