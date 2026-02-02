package com.matriz.backend.modules.schedules.dto;

import com.matriz.backend.modules.schedules.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ScheduleMapper.class})
public interface ScheduleMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    Schedule toEntity(ScheduleReqDto scheduleReqDto);

    ScheduleResDto toDto(Schedule schedule);
}
