package com.example.backend;

import com.example.backend.exceptions.CommentException;
import com.example.backend.models.entities.Comment;
import com.example.backend.models.entities.Post;
import com.example.backend.models.entities.User;
import com.example.backend.models.enums.Role;
import com.example.backend.repositories.CommentRepository;
import com.example.backend.services.CommentService;
import com.example.backend.services.PostService;
import com.example.backend.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {CommentService.class})
@ExtendWith(SpringExtension.class)
class CommentServiceTest {
    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @MockBean
    private PostService postService;

    @MockBean
    private UserService userService;

    /**
     * Method under test: {@link CommentService#createComment(long, long, String)}
     */
    @Test
    void testCreateComment() throws UnsupportedEncodingException {
        User user = new User();
        user.setCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setEmail("jane.doe@example.org");
        user.setEnabled(true);
        user.setFollowers(new HashSet<>());
        user.setFollowing(new HashSet<>());
        user.setId(1L);
        user.setLocked(true);
        user.setPassword("iloveyou");
        user.setProfilePicture("AXAXAXAX".getBytes("UTF-8"));
        user.setRole(Role.USER);
        user.setUsername("janedoe");

        Post post = new Post();
        post.setContent("Not all who wander are lost");
        post.setCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        post.setId(1L);
        post.setLikesCount(3);
        post.setUser(user);

        User user2 = new User();
        user2.setCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user2.setEmail("jane.doe@example.org");
        user2.setEnabled(true);
        user2.setFollowers(new HashSet<>());
        user2.setFollowing(new HashSet<>());
        user2.setId(1L);
        user2.setLocked(true);
        user2.setPassword("iloveyou");
        user2.setProfilePicture("AXAXAXAX".getBytes("UTF-8"));
        user2.setRole(Role.USER);
        user2.setUsername("janedoe");

        Comment comment = new Comment();
        comment.setContent("Not all who wander are lost");
        comment.setCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        comment.setId(1L);
        comment.setPost(post);
        comment.setUser(user2);
        when(commentRepository.save(Mockito.<Comment>any())).thenReturn(comment);

        User user3 = new User();
        user3.setCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user3.setEmail("jane.doe@example.org");
        user3.setEnabled(true);
        user3.setFollowers(new HashSet<>());
        user3.setFollowing(new HashSet<>());
        user3.setId(1L);
        user3.setLocked(true);
        user3.setPassword("iloveyou");
        user3.setProfilePicture("AXAXAXAX".getBytes("UTF-8"));
        user3.setRole(Role.USER);
        user3.setUsername("janedoe");

        Post post2 = new Post();
        post2.setContent("Not all who wander are lost");
        post2.setCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        post2.setId(1L);
        post2.setLikesCount(3);
        post2.setUser(user3);
        when(postService.getPost(Mockito.<Long>any())).thenReturn(post2);

        User user4 = new User();
        user4.setCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user4.setEmail("jane.doe@example.org");
        user4.setEnabled(true);
        user4.setFollowers(new HashSet<>());
        user4.setFollowing(new HashSet<>());
        user4.setId(1L);
        user4.setLocked(true);
        user4.setPassword("iloveyou");
        user4.setProfilePicture("AXAXAXAX".getBytes("UTF-8"));
        user4.setRole(Role.USER);
        user4.setUsername("janedoe");
        when(userService.getUser(anyLong())).thenReturn(user4);
        commentService.createComment(1L, 1L, "Not all who wander are lost");
        verify(postService).getPost(Mockito.<Long>any());
        verify(userService).getUser(anyLong());
        verify(commentRepository).save(Mockito.<Comment>any());
    }

    /**
     * Method under test: {@link CommentService#createComment(long, long, String)}
     */
    @Test
    void testCreateComment2() throws UnsupportedEncodingException {
        User user = new User();
        user.setCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setEmail("jane.doe@example.org");
        user.setEnabled(true);
        user.setFollowers(new HashSet<>());
        user.setFollowing(new HashSet<>());
        user.setId(1L);
        user.setLocked(true);
        user.setPassword("iloveyou");
        user.setProfilePicture("AXAXAXAX".getBytes("UTF-8"));
        user.setRole(Role.USER);
        user.setUsername("janedoe");

        Post post = new Post();
        post.setContent("Not all who wander are lost");
        post.setCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        post.setId(1L);
        post.setLikesCount(3);
        post.setUser(user);
        when(postService.getPost(Mockito.<Long>any())).thenReturn(post);
        when(userService.getUser(anyLong())).thenThrow(new CommentException("An error occurred"));
        assertThrows(CommentException.class, () -> commentService.createComment(1L, 1L, "Not all who wander are lost"));
        verify(postService).getPost(Mockito.<Long>any());
        verify(userService).getUser(anyLong());
    }
}

