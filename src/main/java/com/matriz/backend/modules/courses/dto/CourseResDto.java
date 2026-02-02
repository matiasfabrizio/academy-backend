package com.matriz.backend.modules.courses.dto;

import com.matriz.backend.modules.schedules.dto.ScheduleResDto;
import com.matriz.backend.shared.CourseType;
import com.matriz.backend.shared.Tag;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record CourseResDto(
        UUID id,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        String photoUrl,
        CourseType type,
        String description,
        String professor,
        BigDecimal price,
        UUID holderId,
        Set<ScheduleResDto> schedules,
        String code,
        Tag tag,
        String subtitle,
        List<String> textList
) {
}
