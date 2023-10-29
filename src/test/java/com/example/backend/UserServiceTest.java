package com.example.backend;

import com.example.backend.config.JwtService;
import com.example.backend.exceptions.UserException;
import com.example.backend.exceptions.UserNotFoundException;
import com.example.backend.models.dtos.AuthenticationRequest;
import com.example.backend.models.dtos.AuthenticationResponse;
import com.example.backend.models.entities.Post;
import com.example.backend.models.entities.User;
import com.example.backend.models.enums.Role;
import com.example.backend.repositories.CommentRepository;
import com.example.backend.repositories.PostRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UserService.class})
@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /**
     * Method under test: {@link UserService#loginUser(AuthenticationRequest)}
     */
    @Test
    void testLoginUser() throws UnsupportedEncodingException, AuthenticationException {
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
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        when(jwtService.generateRefreshToken(Mockito.<UserDetails>any())).thenReturn("ABC123");
        when(jwtService.generateToken(Mockito.<UserDetails>any())).thenReturn("ABC123");
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        AuthenticationResponse actualLoginUserResult = userService
                .loginUser(new AuthenticationRequest("jane.doe@example.org", "iloveyou"));
        verify(jwtService).generateRefreshToken(Mockito.<UserDetails>any());
        verify(jwtService).generateToken(Mockito.<UserDetails>any());
        verify(userRepository).findByEmail(Mockito.<String>any());
        verify(authenticationManager).authenticate(Mockito.<Authentication>any());
        assertEquals("1", actualLoginUserResult.getUserId());
        assertEquals("ABC123", actualLoginUserResult.getAccessToken());
        assertEquals("ABC123", actualLoginUserResult.getRefreshToken());
        assertEquals("jane.doe@example.org", actualLoginUserResult.getEmail());
        assertNull(actualLoginUserResult.getRole());
        assertNull(actualLoginUserResult.getUsername());
    }

    /**
     * Method under test: {@link UserService#loginUser(AuthenticationRequest)}
     */
    @Test
    void testLoginUser2() throws AuthenticationException {
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenThrow(new UserException("An error occurred"));
        assertThrows(UserException.class,
                () -> userService.loginUser(new AuthenticationRequest("jane.doe@example.org", "iloveyou")));
        verify(authenticationManager).authenticate(Mockito.<Authentication>any());
    }

    /**
     * Method under test: {@link UserService#loginUser(AuthenticationRequest)}
     */
    @Test
    void testLoginUser3() throws UnsupportedEncodingException, AuthenticationException {
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getEmail()).thenReturn("jane.doe@example.org");
        doNothing().when(user).setCreationDate(Mockito.<LocalDateTime>any());
        doNothing().when(user).setEmail(Mockito.<String>any());
        doNothing().when(user).setEnabled(Mockito.<Boolean>any());
        doNothing().when(user).setFollowers(Mockito.<Set<User>>any());
        doNothing().when(user).setFollowing(Mockito.<Set<User>>any());
        doNothing().when(user).setId(Mockito.<Long>any());
        doNothing().when(user).setLocked(Mockito.<Boolean>any());
        doNothing().when(user).setPassword(Mockito.<String>any());
        doNothing().when(user).setProfilePicture(Mockito.<byte[]>any());
        doNothing().when(user).setRole(Mockito.<Role>any());
        doNothing().when(user).setUsername(Mockito.<String>any());
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
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        when(jwtService.generateRefreshToken(Mockito.<UserDetails>any())).thenReturn("ABC123");
        when(jwtService.generateToken(Mockito.<UserDetails>any())).thenReturn("ABC123");
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        AuthenticationResponse actualLoginUserResult = userService
                .loginUser(new AuthenticationRequest("jane.doe@example.org", "iloveyou"));
        verify(jwtService).generateRefreshToken(Mockito.<UserDetails>any());
        verify(jwtService).generateToken(Mockito.<UserDetails>any());
        verify(user).getEmail();
        verify(user).getId();
        verify(user).setCreationDate(Mockito.<LocalDateTime>any());
        verify(user).setEmail(Mockito.<String>any());
        verify(user).setEnabled(Mockito.<Boolean>any());
        verify(user).setFollowers(Mockito.<Set<User>>any());
        verify(user).setFollowing(Mockito.<Set<User>>any());
        verify(user).setId(Mockito.<Long>any());
        verify(user).setLocked(Mockito.<Boolean>any());
        verify(user).setPassword(Mockito.<String>any());
        verify(user).setProfilePicture(Mockito.<byte[]>any());
        verify(user).setRole(Mockito.<Role>any());
        verify(user).setUsername(Mockito.<String>any());
        verify(userRepository).findByEmail(Mockito.<String>any());
        verify(authenticationManager).authenticate(Mockito.<Authentication>any());
        assertEquals("1", actualLoginUserResult.getUserId());
        assertEquals("ABC123", actualLoginUserResult.getAccessToken());
        assertEquals("ABC123", actualLoginUserResult.getRefreshToken());
        assertEquals("jane.doe@example.org", actualLoginUserResult.getEmail());
        assertNull(actualLoginUserResult.getRole());
        assertNull(actualLoginUserResult.getUsername());
    }

    /**
     * Method under test: {@link UserService#loginUser(AuthenticationRequest)}
     */
    @Test
    void testLoginUser4() throws AuthenticationException {
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(emptyResult);
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        assertThrows(UserNotFoundException.class,
                () -> userService.loginUser(new AuthenticationRequest("jane.doe@example.org", "iloveyou")));
        verify(userRepository).findByEmail(Mockito.<String>any());
        verify(authenticationManager).authenticate(Mockito.<Authentication>any());
    }

    /**
     * Method under test: {@link UserService#followUser(long, long)}
     */
    @Test
    void testFollowUser() throws UnsupportedEncodingException {
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
        Optional<User> ofResult = Optional.of(user);

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
        when(userRepository.save(Mockito.<User>any())).thenReturn(user2);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        userService.followUser(1L, 1L);
        verify(userRepository, atLeast(1)).findById(Mockito.<Long>any());
        verify(userRepository).save(Mockito.<User>any());
    }

    /**
     * Method under test: {@link UserService#followUser(long, long)}
     */
    @Test
    void testFollowUser2() throws UnsupportedEncodingException {
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
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.save(Mockito.<User>any())).thenThrow(new UserException("An error occurred"));
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(UserException.class, () -> userService.followUser(1L, 1L));
        verify(userRepository, atLeast(1)).findById(Mockito.<Long>any());
        verify(userRepository).save(Mockito.<User>any());
    }

    /**
     * Method under test: {@link UserService#followUser(long, long)}
     */
    @Test
    void testFollowUser3() throws UnsupportedEncodingException {
        User user = mock(User.class);
        when(user.getFollowing()).thenReturn(new HashSet<>());
        doNothing().when(user).setCreationDate(Mockito.<LocalDateTime>any());
        doNothing().when(user).setEmail(Mockito.<String>any());
        doNothing().when(user).setEnabled(Mockito.<Boolean>any());
        doNothing().when(user).setFollowers(Mockito.<Set<User>>any());
        doNothing().when(user).setFollowing(Mockito.<Set<User>>any());
        doNothing().when(user).setId(Mockito.<Long>any());
        doNothing().when(user).setLocked(Mockito.<Boolean>any());
        doNothing().when(user).setPassword(Mockito.<String>any());
        doNothing().when(user).setProfilePicture(Mockito.<byte[]>any());
        doNothing().when(user).setRole(Mockito.<Role>any());
        doNothing().when(user).setUsername(Mockito.<String>any());
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
        Optional<User> ofResult = Optional.of(user);

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
        when(userRepository.save(Mockito.<User>any())).thenReturn(user2);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        userService.followUser(1L, 1L);
        verify(user).getFollowing();
        verify(user).setCreationDate(Mockito.<LocalDateTime>any());
        verify(user).setEmail(Mockito.<String>any());
        verify(user).setEnabled(Mockito.<Boolean>any());
        verify(user).setFollowers(Mockito.<Set<User>>any());
        verify(user).setFollowing(Mockito.<Set<User>>any());
        verify(user).setId(Mockito.<Long>any());
        verify(user).setLocked(Mockito.<Boolean>any());
        verify(user).setPassword(Mockito.<String>any());
        verify(user).setProfilePicture(Mockito.<byte[]>any());
        verify(user).setRole(Mockito.<Role>any());
        verify(user).setUsername(Mockito.<String>any());
        verify(userRepository, atLeast(1)).findById(Mockito.<Long>any());
        verify(userRepository).save(Mockito.<User>any());
    }

    /**
     * Method under test: {@link UserService#deleteUser(long)}
     */
    @Test
    void testDeleteUser() throws UnsupportedEncodingException {
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
        Optional<User> ofResult = Optional.of(user);
        doNothing().when(userRepository).delete(Mockito.<User>any());
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(postRepository.findAllByUser(Mockito.<User>any())).thenReturn(new ArrayList<>());
        when(commentRepository.findAllByUser(Mockito.<User>any())).thenReturn(new ArrayList<>());
        userService.deleteUser(1L);
        verify(commentRepository).findAllByUser(Mockito.<User>any());
        verify(postRepository).findAllByUser(Mockito.<User>any());
        verify(userRepository).delete(Mockito.<User>any());
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserService#deleteUser(long)}
     */
    @Test
    void testDeleteUser2() throws UnsupportedEncodingException {
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
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(postRepository.findAllByUser(Mockito.<User>any())).thenReturn(new ArrayList<>());
        when(commentRepository.findAllByUser(Mockito.<User>any())).thenThrow(new UserException("An error occurred"));
        assertThrows(UserException.class, () -> userService.deleteUser(1L));
        verify(commentRepository).findAllByUser(Mockito.<User>any());
        verify(postRepository).findAllByUser(Mockito.<User>any());
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserService#deleteUser(long)}
     */
    @Test
    void testDeleteUser3() {
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserService#deleteUser(long)}
     */
    @Test
    void testDeleteUser4() throws UnsupportedEncodingException {
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
        Optional<User> ofResult = Optional.of(user);
        doNothing().when(userRepository).delete(Mockito.<User>any());
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

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

        Post post = new Post();
        post.setContent("Not all who wander are lost");
        post.setCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        post.setId(1L);
        post.setUser(user2);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post);

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
        post2.setUser(user3);
        when(postRepository.save(Mockito.<Post>any())).thenReturn(post2);
        when(postRepository.findAllByUser(Mockito.<User>any())).thenReturn(postList);
        when(commentRepository.findAllByUser(Mockito.<User>any())).thenReturn(new ArrayList<>());
        userService.deleteUser(1L);
        verify(commentRepository).findAllByUser(Mockito.<User>any());
        verify(postRepository).findAllByUser(Mockito.<User>any());
        verify(userRepository).delete(Mockito.<User>any());
        verify(userRepository).findById(Mockito.<Long>any());
        verify(postRepository).save(Mockito.<Post>any());
    }
}

