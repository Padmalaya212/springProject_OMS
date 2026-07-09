package com.oms.controller;


import com.oms.entity.Notification;
import com.oms.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
	@Autowired
    private  NotificationService notificationService;

    NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(notificationService.getByUser(userId));
    }

    @PostMapping
    public ResponseEntity<Notification> create(@RequestParam Integer userId, @RequestParam String title, @RequestParam String message) {
        return ResponseEntity.ok(notificationService.create(userId, title, message));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Notification> markRead(@PathVariable Integer id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }
}