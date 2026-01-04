package com.kaikeventura.petgram.controller;

import com.kaikeventura.petgram.dto.NotificationResponse;
import com.kaikeventura.petgram.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Endpoints for managing user notifications.")
@SecurityRequirement(name = "bearerAuth")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Get unread notifications for a pet", description = "Fetches a list of all unread notifications for the specified pet.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved notifications")
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(
            @Parameter(description = "The ID of the pet whose notifications are to be fetched.") @RequestHeader("X-Pet-Id") UUID petId
    ) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(petId));
    }

    @Operation(summary = "Get unread notifications count for a pet", description = "Fetches the number of unread notifications for the specified pet.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved notification count")
    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadNotificationsCount(
            @Parameter(description = "The ID of the pet whose notification count is to be fetched.") @RequestHeader("X-Pet-Id") UUID petId
    ) {
        return ResponseEntity.ok(notificationService.getUnreadNotificationsCount(petId));
    }

    @Operation(summary = "Mark all notifications as read for a pet", description = "Marks all unread notifications for the specified pet as read.")
    @ApiResponse(responseCode = "204", description = "Notifications marked as read")
    @PostMapping("/mark-as-read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markAllAsRead(
            @Parameter(description = "The ID of the pet whose notifications are to be marked as read.") @RequestHeader("X-Pet-Id") UUID petId
    ) {
        notificationService.markAllAsRead(petId);
    }
}
