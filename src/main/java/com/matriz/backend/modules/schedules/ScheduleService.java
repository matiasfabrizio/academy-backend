package com.matriz.backend.modules.schedules;

import com.matriz.backend.modules.exceptions.ScheduleNotFoundException;
import com.matriz.backend.modules.schedules.dto.ScheduleMapper;
import com.matriz.backend.modules.schedules.dto.ScheduleResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;

    public List<ScheduleResDto> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(scheduleMapper::toDto)
                .toList();
    }

    public ScheduleResDto getScheduleById(java.util.UUID id) {
        return scheduleRepository.findById(id)
                .map(scheduleMapper::toDto)
                .orElseThrow(() -> new ScheduleNotFoundException("Schedule not found with id: " + id));
    }

    public void deleteScheduleById(java.util.UUID id) {
        scheduleRepository.deleteById(id);
    }
}
