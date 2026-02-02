package com.matriz.backend.modules.finance.holder;

import com.matriz.backend.modules.finance.holder.dto.HolderReqDto;
import com.matriz.backend.modules.finance.holder.dto.HolderResDto;
import com.matriz.backend.modules.finance.holder.dto.HolderUpdateReqDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/holders")
@RequiredArgsConstructor
@Tag(name = "Holders", description = "Operations to manage bank account holders.")
public class HolderController {

    private final HolderService holderService;

    @PostMapping
    @Operation(summary = "Create a new holder", description = "Create a new holder with the provided information. Returns the created holder.")
    public ResponseEntity<HolderResDto> create(@RequestBody @Valid HolderReqDto reqDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(holderService.createHolder(reqDto));
    }

    @GetMapping
    @Operation(summary = "List all holders", description = "Returns a list of all holders.")
    public ResponseEntity<List<HolderResDto>> getAllHolders() {
        return ResponseEntity.ok(holderService.getAllHolders());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific holder", description = "Returns the holder with the specified ID")
    public ResponseEntity<HolderResDto> getHolderById(@PathVariable UUID id) {
        return ResponseEntity.ok(holderService.getHolderById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a specific course", description = "Updates the course with the specified ID.")
    public ResponseEntity<HolderResDto> updateCourseById(@RequestBody @Valid HolderUpdateReqDto updateReqDto, @PathVariable UUID id) {
        return ResponseEntity.ok(holderService.updateHolderById(updateReqDto, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a specific holder", description = "Deletes the holder with the specified ID")
    public ResponseEntity<Void> deleteHolderById(@PathVariable UUID id) {
        holderService.deleteHolderbyId(id);
        return ResponseEntity.noContent().build();
    }
}