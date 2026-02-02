package com.matriz.backend.modules.schedules.dto;

import com.matriz.backend.shared.Mode;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record ScheduleReqDto(
        @Schema(example = "MONDAY", description = "Day of the week for the schedule.")
        DayOfWeek day,
        @Schema(example = "08:00:00", description = "Start time of the schedule. Format HH:mm:ss")
        LocalTime startTime,
        @Schema(example = "10:00:00", description = "End time of the schedule. Format HH:mm:ss")
        LocalTime endTime,
        @Schema(example = "ONLINE", description = "Mode of the schedule. Allowed values: ONLINE, IN_PERSON, HYBRID")
        Mode mode,
        @Schema(example = "25", description = "Maximum number of students for this schedule.")
        int capacity,
        @Schema(example = "Virtual Room 1", description = "Physical or virtual classroom for the schedule.")
        String classroom
) {}