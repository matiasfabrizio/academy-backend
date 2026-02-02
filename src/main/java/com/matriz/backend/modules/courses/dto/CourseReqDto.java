package com.matriz.backend.modules.courses.dto;

import com.matriz.backend.modules.schedules.dto.ScheduleReqDto;
import com.matriz.backend.shared.CourseType;
import com.matriz.backend.shared.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CourseReqDto(
        
    /*
    The names of some descriptions and enums are in spanish because
    the Deployed Application will be for spanish speaking users.
    Therefore, some notifications must be in spanish.
    */
        
    /* Mandatory fields */
        
   @NotBlank(message = "El nombre es obligatorio")
   @Schema(example = "Estadística 1", description = "Course name")
   String name,

   @NotNull(message = "La fecha de inicio es obligatoria")
   @Schema(example = "2024-03-01", description = "Format YYYY-MM-DD")
   LocalDate startDate,

   @NotNull(message = "La fecha de fin es obligatoria")
   @Schema(example = "2024-07-15", description = "Format YYYY-MM-DD")
   LocalDate endDate,

   @NotBlank(message = "La foto es obligatoria")
   @Schema(example = "https://cdn.matriz.edu/img/math.png")
   String photoUrl,

   @NotNull(message = "El tipo de curso es obligatorio")
   @Schema(example = "PRE", description = "Allowed types: PRE, REFUERZO, ADMISION, ESCOLAR")
   CourseType type,

   /* Optional fields */

   @Schema(example = "This is a test course. Add more information.", description = "Description of the course.")
   String description,

   @Schema(example = "Rubén Prado", description = "Names the professor in charge of the course.")
   String professor,

   @Schema(example = "1400.00", description = "Lists the price of the course (if applicable).")
   BigDecimal price,

   @Schema(description = "UUID of the holder of the bank information of the course.", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
   UUID holderId,

   @Schema(description = "List of schedules that will be assigned to the course.")
   List<ScheduleReqDto> schedules,

   /* Refuerzo */

   @Schema(example = "MATE1", description = "Assigns the code of the course (will be displayed in URL).")
   String code,

   @Schema(example = "MATH", description = "Allowed types: MATH, STATISTICS, INFORMATICS, LANGUAGE, ECONOMY")
   Tag tag,
   
   /* Text fields */

   @Schema(example = "Materiales", description = "Used as a subtitle for the course. Add additional info in the textList.")
   String subtitle,

   @Schema(description = "Additional information in multiple lines.")
   List<String> textList
) {}
