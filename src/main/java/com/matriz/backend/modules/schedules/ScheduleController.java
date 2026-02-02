package com.matriz.backend.modules.schedules;

import com.matriz.backend.modules.schedules.dto.ScheduleResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
@Tag(name = "Schedules", description = "Operations to manage schedules from courses.")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @GetMapping
    @Operation
    public ResponseEntity<List<ScheduleResDto>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.getAllSchedules());
    }

    @GetMapping("/{id}")
    @Operation
    public ResponseEntity<ScheduleResDto> getScheduleById(@PathVariable UUID id) {
        return ResponseEntity.ok(scheduleService.getScheduleById(id));
    }
}
