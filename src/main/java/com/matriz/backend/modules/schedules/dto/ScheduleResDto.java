package com.matriz.backend.modules.schedules.dto;

import com.matriz.backend.shared.Mode;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

public record ScheduleResDto(
        UUID id,
        DayOfWeek day,
        LocalTime startTime,
        LocalTime endTime,
        Mode mode,
        int capacity,
        String classroom
) {}