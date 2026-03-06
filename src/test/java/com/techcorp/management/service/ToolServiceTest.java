package com.techcorp.management.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.techcorp.management.repository.ToolRepository;
import com.techcorp.management.exception.BadRequestException;
import com.techcorp.management.exception.ResourceNotFoundException;
import com.techcorp.management.dto.ToolDetailDTO;
import com.techcorp.management.entity.ToolStatus;
import com.techcorp.management.entity.Tool;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ToolServiceTest {

    @Mock
    private ToolRepository toolRepository;

    @InjectMocks
    private ToolService toolService;

    @Test
    void shouldThrowExceptionWhenCostIsNegative() {
        Tool existingTool = new Tool();
        existingTool.setId(1L);
        existingTool.setName("Slack");
        existingTool.setMonthlyCost(10.0);

        Tool updateRequest = new Tool();
        updateRequest.setMonthlyCost(-50.0); // Coût invalide
        when(toolRepository.findById(1L)).thenReturn(Optional.of(existingTool));
        assertThrows(BadRequestException.class, () -> {
            toolService.updateTool(1L, updateRequest);
        });
        verify(toolRepository, never()).save(any());
    }

    @Test
    void shouldUpdateNameSuccessfully() {
        Tool existingTool = new Tool();
        existingTool.setName("Old Name");
        existingTool.setDescription("Old Description");
        existingTool.setStatus(ToolStatus.trial);
        existingTool.setMonthlyCost(10.0);

        Tool updateRequest = new Tool();
        existingTool.setDescription("New Description");
        existingTool.setStatus(ToolStatus.active);
        existingTool.setMonthlyCost(12.0);

        when(toolRepository.findById(1L)).thenReturn(Optional.of(existingTool));
        when(toolRepository.save(any(Tool.class))).thenAnswer(i -> i.getArgument(0));
        ToolDetailDTO result = toolService.updateTool(1L, updateRequest);
        assertEquals("New Description", result.getDescription());
        verify(toolRepository).save(existingTool);
    }

    @Test
    void shouldThrowExceptionWhenToolNotFound() {
        Tool updateRequest = new Tool();
        updateRequest.setName("Ghost Tool");
        assertThrows(ResourceNotFoundException.class, () -> {
            toolService.updateTool(99L, updateRequest);
        });
        verify(toolRepository, never()).save(any());
    }

    @Test
    void shouldThrowBadRequestWhenCostIsNegative() {
        Tool existingTool = new Tool();
        existingTool.setMonthlyCost(10.0);
        
        Tool updateRequest = new Tool();
        updateRequest.setMonthlyCost(-5.0);

        when(toolRepository.findById(1L)).thenReturn(Optional.of(existingTool));

        BadRequestException ex = assertThrows(BadRequestException.class, () -> {
            toolService.updateTool(1L, updateRequest);
        });

        assertEquals("Cout ne peut être négatif !", ex.getMessage());
        verify(toolRepository, never()).save(any());
    }
}