package com.situ.kdz2026.model.search;

import com.situ.kdz2026.model.Course;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class CourseSearchBean extends Course {
    private LocalDate beginTime;
    private LocalDate endTime;
}
