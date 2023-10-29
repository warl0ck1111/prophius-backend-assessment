package com.example.backend.services;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.backend.models.dtos.CreateNotificationRequest;
import com.example.backend.models.entities.Notification;
import com.example.backend.models.entities.Post;
import com.example.backend.models.entities.User;
import com.example.backend.models.enums.NotificationType;
import com.example.backend.models.enums.Role;
import com.example.backend.repositories.NotificationRepository;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {NotificationService.class})
@ExtendWith(SpringExtension.class)
class NotificationServiceTest {
    @MockBean
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationService notificationService;

    /**
     * Method under test: {@link NotificationService#createNotification(CreateNotificationRequest)}
     */
    @Test
    void testCreateNotification() throws UnsupportedEncodingException {
        User user = new User();
        user.setCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setEmail("bashir.okala@hotmail.com");
        user.setEnabled(true);
        user.setFollowers(new HashSet<>());
        user.setFollowing(new HashSet<>());
        user.setId(1L);
        user.setLocked(true);
        user.setPassword("password");
        user.setProfilePicture("AXAXAXAX".getBytes("UTF-8"));
        user.setRole(Role.USER);
        user.setUsername("warl0ck");

        Post post = new Post();
        post.setContent("Not all who wander are lost");
        post.setCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        post.setId(1L);
        post.setLikesCount(3);
        post.setUser(user);

        User sender = new User();
        sender.setCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sender.setEmail("bashir.okala@hotmail.com");
        sender.setEnabled(true);
        sender.setFollowers(new HashSet<>());
        sender.setFollowing(new HashSet<>());
        sender.setId(1L);
        sender.setLocked(true);
        sender.setPassword("password");
        sender.setProfilePicture("AXAXAXAX".getBytes("UTF-8"));
        sender.setRole(Role.USER);
        sender.setUsername("warl0ck");

        User user2 = new User();
        user2.setCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user2.setEmail("bashir.okala@hotmail.com");
        user2.setEnabled(true);
        user2.setFollowers(new HashSet<>());
        user2.setFollowing(new HashSet<>());
        user2.setId(1L);
        user2.setLocked(true);
        user2.setPassword("password");
        user2.setProfilePicture("AXAXAXAX".getBytes("UTF-8"));
        user2.setRole(Role.USER);
        user2.setUsername("warl0ck");

        Notification notification = new Notification();
        notification.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        notification.setId(1L);
        notification.setNotificationType(NotificationType.LIKE);
        notification.setPost(post);
        notification.setRead(true);
        notification.setSender(sender);
        notification.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        notification.setUser(user2);
        when(notificationRepository.save(Mockito.<Notification>any())).thenReturn(notification);

        User user3 = new User();
        user3.setCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user3.setEmail("bashir.okala@hotmail.com");
        user3.setEnabled(true);
        user3.setFollowers(new HashSet<>());
        user3.setFollowing(new HashSet<>());
        user3.setId(1L);
        user3.setLocked(true);
        user3.setPassword("password");
        user3.setProfilePicture("AXAXAXAX".getBytes("UTF-8"));
        user3.setRole(Role.USER);
        user3.setUsername("warl0ck");

        Post post2 = new Post();
        post2.setContent("Not all who wander are lost");
        post2.setCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        post2.setId(1L);
        post2.setLikesCount(3);
        post2.setUser(user3);
        CreateNotificationRequest.CreateNotificationRequestBuilder postResult = CreateNotificationRequest.builder()
                .id(1L)
                .notificationType(NotificationType.LIKE)
                .post(post2);

        User sender2 = new User();
        sender2.setCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        sender2.setEmail("bashir.okala@hotmail.com");
        sender2.setEnabled(true);
        sender2.setFollowers(new HashSet<>());
        sender2.setFollowing(new HashSet<>());
        sender2.setId(1L);
        sender2.setLocked(true);
        sender2.setPassword("password");
        sender2.setProfilePicture("AXAXAXAX".getBytes("UTF-8"));
        sender2.setRole(Role.USER);
        sender2.setUsername("warl0ck");
        CreateNotificationRequest.CreateNotificationRequestBuilder senderResult = postResult.sender(sender2);

        User user4 = new User();
        user4.setCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user4.setEmail("bashir.okala@hotmail.com");
        user4.setEnabled(true);
        user4.setFollowers(new HashSet<>());
        user4.setFollowing(new HashSet<>());
        user4.setId(1L);
        user4.setLocked(true);
        user4.setPassword("password");
        user4.setProfilePicture("AXAXAXAX".getBytes("UTF-8"));
        user4.setRole(Role.USER);
        user4.setUsername("warl0ck");
        Notification actualCreateNotificationResult = notificationService
                .createNotification(senderResult.user(user4).build());
        verify(notificationRepository).save(Mockito.<Notification>any());
        assertSame(notification, actualCreateNotificationResult);
    }

    /**
     * Method under test: {@link NotificationService#createNotification(CreateNotificationRequest)}
     */
    @Test
    void testCreateNotification2() throws UnsupportedEncodingException {
        when(notificationRepository.save(Mockito.<Notification>any())).thenThrow(new NoSuchElementException("foo"));

        User user = new User();
        user.setCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setEmail("bashir.okala@hotmail.com");
        user.setEnabled(true);
        user.setFollowers(new HashSet<>());
        user.setFollowing(new HashSet<>());
        user.setId(1L);
        user.setLocked(true);
        user.setPassword("password");
        user.setProfilePicture("AXAXAXAX".getBytes("UTF-8"));
        user.setRole(Role.USER);
        user.setUsername("warl0ck");

        Post post = new Post();
        post.setContent("Not all who wander are lost");
        post.setCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        post.setId(1L);
        post.setLikesCount(3);
        post.setUser(user);

        User user2 = new User();
        user2.setCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user2.setEmail("bashir.okala@hotmail.com");
        user2.setEnabled(true);
        user2.setFollowers(new HashSet<>());
        user2.setFollowing(new HashSet<>());
        user2.setId(1L);
        user2.setLocked(true);
        user2.setPassword("password");
        user2.setProfilePicture("AXAXAXAX".getBytes("UTF-8"));
        user2.setRole(Role.USER);
        user2.setUsername("warl0ck");

        User user3 = new User();
        user3.setCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user3.setEmail("bashir.okala@hotmail.com");
        user3.setEnabled(true);
        user3.setFollowers(new HashSet<>());
        user3.setFollowing(new HashSet<>());
        user3.setId(1L);
        user3.setLocked(true);
        user3.setPassword("password");
        user3.setProfilePicture("AXAXAXAX".getBytes("UTF-8"));
        user3.setRole(Role.USER);
        user3.setUsername("warl0ck");
        CreateNotificationRequest notificationRequest = mock(CreateNotificationRequest.class);
        when(notificationRequest.getPost()).thenReturn(post);
        when(notificationRequest.getSender()).thenReturn(user2);
        when(notificationRequest.getUser()).thenReturn(user3);
        when(notificationRequest.getNotificationType()).thenReturn(NotificationType.LIKE);
        assertThrows(NoSuchElementException.class, () -> notificationService.createNotification(notificationRequest));
        verify(notificationRequest).getNotificationType();
        verify(notificationRequest).getPost();
        verify(notificationRequest).getSender();
        verify(notificationRequest).getUser();
        verify(notificationRepository).save(Mockito.<Notification>any());
    }
}

