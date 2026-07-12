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
public class StatisticUsersDTO {
    private long numberOfReader;
    private long numberOfStaff;
    private List<StatisticMonth> statisticReadersMonths;
    private List<StatisticMonth> statisticStaffsMonths;
}
