package com.oms.service;


import com.oms.entity.Notification;
import com.oms.entity.User;
import com.oms.exception.ResourceNotFoundException;
import com.oms.repository.NotificationRepository;
import com.oms.repository.UserRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService {
	@Autowired
    private  NotificationRepository notificationRepository;
	@Autowired private  UserRepository userRepository;

    NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public List<Notification> getByUser(Integer userId) {
        return notificationRepository.findByUser_UserId(userId);
    }

    public Notification create(Integer userId, String title, String message) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        Notification n = new Notification();
        n.setUser(user);
        n.setTitle(title);
        n.setMessage(message);
        return notificationRepository.save(n);
    }

    public Notification markAsRead(Integer notificationId) {
        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found: " + notificationId));
        n.setIsRead(true);
        return notificationRepository.save(n);
    }

	
}