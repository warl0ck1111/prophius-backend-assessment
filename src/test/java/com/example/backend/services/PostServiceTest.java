package com.example.backend.services;

import com.example.backend.exceptions.PostException;
import com.example.backend.models.dtos.CreatePostRequest;
import com.example.backend.models.dtos.CreatePostResponse;
import com.example.backend.models.entities.Post;
import com.example.backend.models.entities.User;
import com.example.backend.models.enums.Role;
import com.example.backend.repositories.CommentRepository;
import com.example.backend.repositories.PostRepository;
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
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {PostService.class})
@ExtendWith(SpringExtension.class)
class PostServiceTest {
    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @MockBean
    private UserService userService;

    /**
     * Method under test: {@link PostService#createPost(CreatePostRequest)}
     */
    @Test
    void testCreatePost() throws UnsupportedEncodingException {
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
        when(postRepository.save(Mockito.<Post>any())).thenReturn(post);

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
        when(userService.getUser(anyLong())).thenReturn(user2);
        CreatePostResponse actualCreatePostResult = postService
                .createPost(new CreatePostRequest("Not all who wander are lost", 1L));
        verify(userService).getUser(anyLong());
        verify(postRepository).save(Mockito.<Post>any());
        assertEquals("Not all who wander are lost", actualCreatePostResult.getContent());
        assertEquals("bashir.okala@hotmail.com", actualCreatePostResult.getUsername());
        assertEquals(0, actualCreatePostResult.getLikesCount());
        assertEquals(1L, actualCreatePostResult.getUserId());
        assertEquals(1L, actualCreatePostResult.getPostId().longValue());
    }

    /**
     * Method under test: {@link PostService#createPost(CreatePostRequest)}
     */
    @Test
    void testCreatePost2() {
        when(userService.getUser(anyLong())).thenThrow(new PostException("An error occurred"));
        assertThrows(PostException.class,
                () -> postService.createPost(new CreatePostRequest("Not all who wander are lost", 1L)));
        verify(userService).getUser(anyLong());
    }

    /**
     * Method under test: {@link PostService#createPost(CreatePostRequest)}
     */
    @Test
    void testCreatePost3() throws UnsupportedEncodingException {
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
        Post post = mock(Post.class);
        when(post.getId()).thenReturn(1L);
        doNothing().when(post).setContent(Mockito.<String>any());
        doNothing().when(post).setCreationDate(Mockito.<LocalDateTime>any());
        doNothing().when(post).setId(Mockito.<Long>any());
        doNothing().when(post).setLikesCount(anyInt());
        doNothing().when(post).setUser(Mockito.<User>any());
        post.setContent("Not all who wander are lost");
        post.setCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        post.setId(1L);
        post.setLikesCount(3);
        post.setUser(user);
        when(postRepository.save(Mockito.<Post>any())).thenReturn(post);

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
        when(userService.getUser(anyLong())).thenReturn(user2);
        CreatePostResponse actualCreatePostResult = postService
                .createPost(new CreatePostRequest("Not all who wander are lost", 1L));
        verify(post).getId();
        verify(post).setContent(Mockito.<String>any());
        verify(post).setCreationDate(Mockito.<LocalDateTime>any());
        verify(post).setId(Mockito.<Long>any());
        verify(post).setLikesCount(anyInt());
        verify(post).setUser(Mockito.<User>any());
        verify(userService).getUser(anyLong());
        verify(postRepository).save(Mockito.<Post>any());
        assertEquals("Not all who wander are lost", actualCreatePostResult.getContent());
        assertEquals("bashir.okala@hotmail.com", actualCreatePostResult.getUsername());
        assertEquals(0, actualCreatePostResult.getLikesCount());
        assertEquals(1L, actualCreatePostResult.getUserId());
        assertEquals(1L, actualCreatePostResult.getPostId().longValue());
    }

    /**
     * Method under test: {@link PostService#createPost(CreatePostRequest)}
     */
    @Test
    void testCreatePost4() throws UnsupportedEncodingException {
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
        Post post = mock(Post.class);
        when(post.getId()).thenReturn(1L);
        doNothing().when(post).setContent(Mockito.<String>any());
        doNothing().when(post).setCreationDate(Mockito.<LocalDateTime>any());
        doNothing().when(post).setId(Mockito.<Long>any());
        doNothing().when(post).setLikesCount(anyInt());
        doNothing().when(post).setUser(Mockito.<User>any());
        post.setContent("Not all who wander are lost");
        post.setCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        post.setId(1L);
        post.setLikesCount(3);
        post.setUser(user);
        when(postRepository.save(Mockito.<Post>any())).thenReturn(post);
        User user2 = mock(User.class);
        when(user2.getId()).thenReturn(1L);
        when(user2.getUsername()).thenReturn("warl0ck");
        doNothing().when(user2).setCreationDate(Mockito.<LocalDateTime>any());
        doNothing().when(user2).setEmail(Mockito.<String>any());
        doNothing().when(user2).setEnabled(Mockito.<Boolean>any());
        doNothing().when(user2).setFollowers(Mockito.<Set<User>>any());
        doNothing().when(user2).setFollowing(Mockito.<Set<User>>any());
        doNothing().when(user2).setId(Mockito.<Long>any());
        doNothing().when(user2).setLocked(Mockito.<Boolean>any());
        doNothing().when(user2).setPassword(Mockito.<String>any());
        doNothing().when(user2).setProfilePicture(Mockito.<byte[]>any());
        doNothing().when(user2).setRole(Mockito.<Role>any());
        doNothing().when(user2).setUsername(Mockito.<String>any());
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
        when(userService.getUser(anyLong())).thenReturn(user2);
        CreatePostResponse actualCreatePostResult = postService
                .createPost(new CreatePostRequest("Not all who wander are lost", 1L));
        verify(post).getId();
        verify(post).setContent(Mockito.<String>any());
        verify(post).setCreationDate(Mockito.<LocalDateTime>any());
        verify(post).setId(Mockito.<Long>any());
        verify(post).setLikesCount(anyInt());
        verify(post).setUser(Mockito.<User>any());
        verify(user2).getId();
        verify(user2).getUsername();
        verify(user2).setCreationDate(Mockito.<LocalDateTime>any());
        verify(user2).setEmail(Mockito.<String>any());
        verify(user2).setEnabled(Mockito.<Boolean>any());
        verify(user2).setFollowers(Mockito.<Set<User>>any());
        verify(user2).setFollowing(Mockito.<Set<User>>any());
        verify(user2).setId(Mockito.<Long>any());
        verify(user2).setLocked(Mockito.<Boolean>any());
        verify(user2).setPassword(Mockito.<String>any());
        verify(user2).setProfilePicture(Mockito.<byte[]>any());
        verify(user2).setRole(Mockito.<Role>any());
        verify(user2).setUsername(Mockito.<String>any());
        verify(userService).getUser(anyLong());
        verify(postRepository).save(Mockito.<Post>any());
        assertEquals("Not all who wander are lost", actualCreatePostResult.getContent());
        assertEquals("warl0ck", actualCreatePostResult.getUsername());
        assertEquals(0, actualCreatePostResult.getLikesCount());
        assertEquals(1L, actualCreatePostResult.getUserId());
        assertEquals(1L, actualCreatePostResult.getPostId().longValue());
    }
}

