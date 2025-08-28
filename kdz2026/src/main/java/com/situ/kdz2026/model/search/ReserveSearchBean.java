package com.situ.kdz2026.model.search;

import com.situ.kdz2026.model.Reserve;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class ReserveSearchBean extends Reserve {
    private LocalDate reserveFrom;
    private LocalDate reserveTo;
}
