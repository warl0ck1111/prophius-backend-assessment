package com.example.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.backend.config.JwtService;
import com.example.backend.exceptions.UserException;
import com.example.backend.exceptions.UserNotFoundException;
import com.example.backend.models.dtos.*;
import com.example.backend.models.entities.Post;
import com.example.backend.models.entities.User;
import com.example.backend.models.enums.Role;
import com.example.backend.repositories.CommentRepository;
import com.example.backend.repositories.PostRepository;
import com.example.backend.repositories.UserRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

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
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        when(jwtService.generateRefreshToken(Mockito.<UserDetails>any())).thenReturn("ABC123");
        when(jwtService.generateToken(Mockito.<UserDetails>any())).thenReturn("ABC123");
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        AuthenticationResponse actualLoginUserResult = userService
                .loginUser(new AuthenticationRequest("bashir.okala@hotmail.com", "password"));
        verify(jwtService).generateRefreshToken(Mockito.<UserDetails>any());
        verify(jwtService).generateToken(Mockito.<UserDetails>any());
        verify(userRepository).findByEmail(Mockito.<String>any());
        verify(authenticationManager).authenticate(Mockito.<Authentication>any());
        assertEquals("1", actualLoginUserResult.getUserId());
        assertEquals("ABC123", actualLoginUserResult.getAccessToken());
        assertEquals("ABC123", actualLoginUserResult.getRefreshToken());
        assertEquals("bashir.okala@hotmail.com", actualLoginUserResult.getEmail());
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
                () -> userService.loginUser(new AuthenticationRequest("bashir.okala@hotmail.com", "password")));
        verify(authenticationManager).authenticate(Mockito.<Authentication>any());
    }

    /**
     * Method under test: {@link UserService#loginUser(AuthenticationRequest)}
     */
    @Test
    void testLoginUser3() throws UnsupportedEncodingException, AuthenticationException {
        User user = mock(User.class);
        when(user.getId()).thenReturn(1L);
        when(user.getEmail()).thenReturn("bashir.okala@hotmail.com");
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
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        when(jwtService.generateRefreshToken(Mockito.<UserDetails>any())).thenReturn("ABC123");
        when(jwtService.generateToken(Mockito.<UserDetails>any())).thenReturn("ABC123");
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        AuthenticationResponse actualLoginUserResult = userService
                .loginUser(new AuthenticationRequest("bashir.okala@hotmail.com", "password"));
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
        assertEquals("bashir.okala@hotmail.com", actualLoginUserResult.getEmail());
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
                () -> userService.loginUser(new AuthenticationRequest("bashir.okala@hotmail.com", "password")));
        verify(userRepository).findByEmail(Mockito.<String>any());
        verify(authenticationManager).authenticate(Mockito.<Authentication>any());
    }

    /**
     * Method under test: {@link UserService#createUser(CreateUserRequest)}
     */
    @Test
    void testCreateUser() {
        when(userRepository.existsByEmail(Mockito.<String>any())).thenReturn(true);
        when(userRepository.existsByUsername(Mockito.<String>any())).thenReturn(true);
        assertThrows(RuntimeException.class,
                () -> userService.createUser(new CreateUserRequest("warl0ck", "bashir.okala@hotmail.com", "password")));
        verify(userRepository).existsByEmail(Mockito.<String>any());
        verify(userRepository).existsByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserService#createUser(CreateUserRequest)}
     */
    @Test
    void testCreateUser2() {
        when(userRepository.existsByEmail(Mockito.<String>any())).thenReturn(false);
        when(userRepository.existsByUsername(Mockito.<String>any())).thenReturn(true);
        assertThrows(RuntimeException.class,
                () -> userService.createUser(new CreateUserRequest("warl0ck", "bashir.okala@hotmail.com", "password")));
        verify(userRepository).existsByEmail(Mockito.<String>any());
        verify(userRepository).existsByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserService#createUser(CreateUserRequest)}
     */
    @Test
    void testCreateUser3() {
        when(userRepository.existsByEmail(Mockito.<String>any())).thenReturn(null);
        when(userRepository.existsByUsername(Mockito.<String>any())).thenReturn(true);
        assertThrows(RuntimeException.class,
                () -> userService.createUser(new CreateUserRequest("warl0ck", "bashir.okala@hotmail.com", "password")));
        verify(userRepository).existsByEmail(Mockito.<String>any());
        verify(userRepository).existsByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserService#createUser(CreateUserRequest)}
     */
    @Test
    void testCreateUser4() {
        when(userRepository.existsByEmail(Mockito.<String>any())).thenReturn(true);
        assertThrows(RuntimeException.class,
                () -> userService.createUser(new CreateUserRequest(null, "bashir.okala@hotmail.com", "password")));
        verify(userRepository).existsByEmail(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserService#createUser(CreateUserRequest)}
     */
    @Test
    void testCreateUser5() {
        assertThrows(RuntimeException.class,
                () -> userService.createUser(new CreateUserRequest("warl0ck", null, "password")));
    }

    /**
     * Method under test: {@link UserService#createUser(CreateUserRequest)}
     */
    @Test
    void testCreateUser6() {
        assertNull(userService.createUser(null));
    }

    /**
     * Method under test: {@link UserService#createUser(CreateUserRequest)}
     */
    @Test
    void testCreateUser7() {
        CreateUserRequest userRequest = mock(CreateUserRequest.class);
        when(userRequest.getEmail()).thenThrow(new UserException("An error occurred"));
        assertThrows(RuntimeException.class, () -> userService.createUser(userRequest));
        verify(userRequest).getEmail();
    }

    /**
     * Method under test: {@link UserService#getUser(long)}
     */
    @Test
    void testGetUser() throws UnsupportedEncodingException {
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
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        User actualUser = userService.getUser(1L);
        verify(userRepository).findById(Mockito.<Long>any());
        assertSame(user, actualUser);
    }

    /**
     * Method under test: {@link UserService#getUser(long)}
     */
    @Test
    void testGetUser2() {
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(UserNotFoundException.class, () -> userService.getUser(1L));
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserService#getUser(long)}
     */
    @Test
    void testGetUser3() {
        when(userRepository.findById(Mockito.<Long>any())).thenThrow(new UserException("An error occurred"));
        assertThrows(UserException.class, () -> userService.getUser(1L));
        verify(userRepository).findById(Mockito.<Long>any());
    }


    /**
     * Method under test: {@link UserService#updateUser(long, UpdateUserRequest)}
     */
    @Test
    void testUpdateUser() throws UnsupportedEncodingException {
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
        Optional<User> ofResult = Optional.of(user);

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
        when(userRepository.save(Mockito.<User>any())).thenReturn(user2);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        User actualUpdateUserResult = userService.updateUser(1L, new UpdateUserRequest("warl0ck", "bashir.okala@hotmail.com"));
        verify(userRepository).findById(Mockito.<Long>any());
        verify(userRepository).save(Mockito.<User>any());
        assertSame(user2, actualUpdateUserResult);
    }

    /**
     * Method under test: {@link UserService#updateUser(long, UpdateUserRequest)}
     */
    @Test
    void testUpdateUser2() throws UnsupportedEncodingException {
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
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.save(Mockito.<User>any())).thenThrow(new UserException("An error occurred"));
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(UserException.class,
                () -> userService.updateUser(1L, new UpdateUserRequest("warl0ck", "bashir.okala@hotmail.com")));
        verify(userRepository).findById(Mockito.<Long>any());
        verify(userRepository).save(Mockito.<User>any());
    }

    /**
     * Method under test: {@link UserService#updateUser(long, UpdateUserRequest)}
     */
    @Test
    void testUpdateUser3() throws UnsupportedEncodingException {
        User user = mock(User.class);
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
        Optional<User> ofResult = Optional.of(user);

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
        when(userRepository.save(Mockito.<User>any())).thenReturn(user2);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        User actualUpdateUserResult = userService.updateUser(1L,
                new UpdateUserRequest("warl0ck", "bashir.okala@hotmail.com"));
        verify(user).setCreationDate(Mockito.<LocalDateTime>any());
        verify(user, atLeast(1)).setEmail(Mockito.<String>any());
        verify(user).setEnabled(Mockito.<Boolean>any());
        verify(user).setFollowers(Mockito.<Set<User>>any());
        verify(user).setFollowing(Mockito.<Set<User>>any());
        verify(user).setId(Mockito.<Long>any());
        verify(user).setLocked(Mockito.<Boolean>any());
        verify(user).setPassword(Mockito.<String>any());
        verify(user).setProfilePicture(Mockito.<byte[]>any());
        verify(user).setRole(Mockito.<Role>any());
        verify(user, atLeast(1)).setUsername(Mockito.<String>any());
        verify(userRepository).findById(Mockito.<Long>any());
        verify(userRepository).save(Mockito.<User>any());
        assertSame(user2, actualUpdateUserResult);
    }

    /**
     * Method under test: {@link UserService#uploadProfilePicture(long, MultipartFile)}
     */
    @Test
    void testUploadProfilePicture() throws IOException {
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
        Optional<User> ofResult = Optional.of(user);

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
        when(userRepository.save(Mockito.<User>any())).thenReturn(user2);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        User actualUploadProfilePictureResult = userService.uploadProfilePicture(1L,
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
        verify(userRepository).findById(Mockito.<Long>any());
        verify(userRepository).save(Mockito.<User>any());
        assertSame(user2, actualUploadProfilePictureResult);
    }

    /**
     * Method under test: {@link UserService#uploadProfilePicture(long, MultipartFile)}
     */
    @Test
    void testUploadProfilePicture2() throws IOException {
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
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.save(Mockito.<User>any())).thenThrow(new UserException("An error occurred"));
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(UserException.class, () -> userService.uploadProfilePicture(1L,
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8")))));
        verify(userRepository).findById(Mockito.<Long>any());
        verify(userRepository).save(Mockito.<User>any());
    }

    /**
     * Method under test: {@link UserService#uploadProfilePicture(long, MultipartFile)}
     */
    @Test
    void testUploadProfilePicture3() throws IOException {
        User user = mock(User.class);
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
        Optional<User> ofResult = Optional.of(user);

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
        when(userRepository.save(Mockito.<User>any())).thenReturn(user2);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        User actualUploadProfilePictureResult = userService.uploadProfilePicture(1L,
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
        verify(user).setCreationDate(Mockito.<LocalDateTime>any());
        verify(user).setEmail(Mockito.<String>any());
        verify(user).setEnabled(Mockito.<Boolean>any());
        verify(user).setFollowers(Mockito.<Set<User>>any());
        verify(user).setFollowing(Mockito.<Set<User>>any());
        verify(user).setId(Mockito.<Long>any());
        verify(user).setLocked(Mockito.<Boolean>any());
        verify(user).setPassword(Mockito.<String>any());
        verify(user, atLeast(1)).setProfilePicture(Mockito.<byte[]>any());
        verify(user).setRole(Mockito.<Role>any());
        verify(user).setUsername(Mockito.<String>any());
        verify(userRepository).findById(Mockito.<Long>any());
        verify(userRepository).save(Mockito.<User>any());
        assertSame(user2, actualUploadProfilePictureResult);
    }
    /**
     * Method under test: {@link UserService#deleteUser(long)}
     */
    @Test
    void testDeleteUser() throws UnsupportedEncodingException {
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
        Optional<User> ofResult = Optional.of(user);
        doNothing().when(userRepository).delete(Mockito.<User>any());
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

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

        Post post = new Post();
        post.setContent("Not all who wander are lost");
        post.setCreationDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        post.setId(1L);
        post.setLikesCount(3);
        post.setUser(user2);

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post);

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

    /**
     * Method under test: {@link UserService#followUser(long, long)}
     */
    @Test
    void testFollowUser() {
        when(userRepository.existsById(Mockito.<Long>any())).thenReturn(true);
        assertThrows(UserException.class, () -> userService.followUser(1L, 1L));
        verify(userRepository).existsById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserService#followUser(long, long)}
     */
    @Test
    void testFollowUser2() {
        when(userRepository.existsById(Mockito.<Long>any())).thenReturn(false);
        assertThrows(UserException.class, () -> userService.followUser(1L, 1L));
        verify(userRepository).existsById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserService#followUser(long, long)}
     */
    @Test
    void testFollowUser3() {
        doNothing().when(userRepository).insertUserFollower(Mockito.<Long>any(), Mockito.<Long>any());
        when(userRepository.existsById(Mockito.<Long>any())).thenReturn(true);
        userService.followUser(2L, 1L);
        verify(userRepository).insertUserFollower(Mockito.<Long>any(), Mockito.<Long>any());
        verify(userRepository, atLeast(1)).existsById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserService#followUser(long, long)}
     */
    @Test
    void testFollowUser4() {
        when(userRepository.existsById(Mockito.<Long>any())).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> userService.followUser(2L, 1L));
        verify(userRepository).existsById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserService#followUser(long, long)}
     */
    @Test
    void testFollowUser5() {
        doThrow(new UserException("An error occurred")).when(userRepository)
                .insertUserFollower(Mockito.<Long>any(), Mockito.<Long>any());
        when(userRepository.existsById(Mockito.<Long>any())).thenReturn(true);
        assertThrows(UserException.class, () -> userService.followUser(2L, 1L));
        verify(userRepository).insertUserFollower(Mockito.<Long>any(), Mockito.<Long>any());
        verify(userRepository, atLeast(1)).existsById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserService#getUserFollowers(long)}
     */
    @Test
    void testGetUserFollowers() {
        HashSet<Followers> followersSet = new HashSet<>();
        when(userRepository.getUsersFollowers(anyLong())).thenReturn(followersSet);
        when(userRepository.existsById(Mockito.<Long>any())).thenReturn(true);
        Set<Followers> actualUserFollowers = userService.getUserFollowers(1L);
        verify(userRepository).getUsersFollowers(anyLong());
        verify(userRepository).existsById(Mockito.<Long>any());
        assertTrue(actualUserFollowers.isEmpty());
        assertSame(followersSet, actualUserFollowers);
    }

    /**
     * Method under test: {@link UserService#getUserFollowers(long)}
     */
    @Test
    void testGetUserFollowers2() {
        when(userRepository.getUsersFollowers(anyLong())).thenThrow(new UserException("An error occurred"));
        when(userRepository.existsById(Mockito.<Long>any())).thenReturn(true);
        assertThrows(UserException.class, () -> userService.getUserFollowers(1L));
        verify(userRepository).getUsersFollowers(anyLong());
        verify(userRepository).existsById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserService#getUserFollowers(long)}
     */
    @Test
    void testGetUserFollowers3() {
        when(userRepository.existsById(Mockito.<Long>any())).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> userService.getUserFollowers(1L));
        verify(userRepository).existsById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserService#getUserFollowing(long)}
     */
    @Test
    void testGetUserFollowing() {
        HashSet<Followers> followersSet = new HashSet<>();
        when(userRepository.getUsersFollowing(anyLong())).thenReturn(followersSet);
        when(userRepository.existsById(Mockito.<Long>any())).thenReturn(true);
        Set<Followers> actualUserFollowing = userService.getUserFollowing(1L);
        verify(userRepository).getUsersFollowing(anyLong());
        verify(userRepository).existsById(Mockito.<Long>any());
        assertTrue(actualUserFollowing.isEmpty());
        assertSame(followersSet, actualUserFollowing);
    }

    /**
     * Method under test: {@link UserService#getUserFollowing(long)}
     */
    @Test
    void testGetUserFollowing2() {
        when(userRepository.getUsersFollowing(anyLong())).thenThrow(new UserException("An error occurred"));
        when(userRepository.existsById(Mockito.<Long>any())).thenReturn(true);
        assertThrows(UserException.class, () -> userService.getUserFollowing(1L));
        verify(userRepository).getUsersFollowing(anyLong());
        verify(userRepository).existsById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserService#getUserFollowing(long)}
     */
    @Test
    void testGetUserFollowing3() {
        when(userRepository.existsById(Mockito.<Long>any())).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> userService.getUserFollowing(1L));
        verify(userRepository).existsById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserService#getAllUsers(int, int, String, Sort.Direction)}
     */
    @Test
    void testGetAllUsers() {
        PageImpl<User> pageImpl = new PageImpl<>(new ArrayList<>());
        when(userRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);
        Page<User> actualAllUsers = userService.getAllUsers(1, 3, "Sort Field", Sort.Direction.ASC);
        verify(userRepository).findAll(Mockito.<Pageable>any());
        assertTrue(actualAllUsers.toList().isEmpty());
        assertSame(pageImpl, actualAllUsers);
    }

    /**
     * Method under test: {@link UserService#getAllUsers(int, int, String, Sort.Direction)}
     */
    @Test
    void testGetAllUsers2() {
        PageImpl<User> pageImpl = new PageImpl<>(new ArrayList<>());
        when(userRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);
        Page<User> actualAllUsers = userService.getAllUsers(1, 3, "Sort Field", Sort.Direction.DESC);
        verify(userRepository).findAll(Mockito.<Pageable>any());
        assertTrue(actualAllUsers.toList().isEmpty());
        assertSame(pageImpl, actualAllUsers);
    }

    /**
     * Method under test: {@link UserService#getAllUsers(int, int, String, Sort.Direction)}
     */
    @Test
    void testGetAllUsers3() {
        when(userRepository.findAll(Mockito.<Pageable>any())).thenThrow(new UserException("An error occurred"));
        assertThrows(UserException.class, () -> userService.getAllUsers(1, 3, "Sort Field", Sort.Direction.ASC));
        verify(userRepository).findAll(Mockito.<Pageable>any());
    }

    /**
     * Method under test: {@link UserService#searchUsers(String, int, int, String, Sort.Direction)}
     */
    @Test
    void testSearchUsers() {
        PageImpl<User> pageImpl = new PageImpl<>(new ArrayList<>());
        when(userRepository.searchUsers(Mockito.<String>any(), Mockito.<PageRequest>any())).thenReturn(pageImpl);
        Page<User> actualSearchUsersResult = userService.searchUsers("Keyword", 1, 3, "Sort Field", Sort.Direction.ASC);
        verify(userRepository).searchUsers(Mockito.<String>any(), Mockito.<PageRequest>any());
        assertTrue(actualSearchUsersResult.toList().isEmpty());
        assertSame(pageImpl, actualSearchUsersResult);
    }

    /**
     * Method under test: {@link UserService#searchUsers(String, int, int, String, Sort.Direction)}
     */
    @Test
    void testSearchUsers2() {
        PageImpl<User> pageImpl = new PageImpl<>(new ArrayList<>());
        when(userRepository.searchUsers(Mockito.<String>any(), Mockito.<PageRequest>any())).thenReturn(pageImpl);
        Page<User> actualSearchUsersResult = userService.searchUsers("Keyword", 1, 3, "Sort Field", Sort.Direction.DESC);
        verify(userRepository).searchUsers(Mockito.<String>any(), Mockito.<PageRequest>any());
        assertTrue(actualSearchUsersResult.toList().isEmpty());
        assertSame(pageImpl, actualSearchUsersResult);
    }

    /**
     * Method under test: {@link UserService#searchUsers(String, int, int, String, Sort.Direction)}
     */
    @Test
    void testSearchUsers3() {
        when(userRepository.searchUsers(Mockito.<String>any(), Mockito.<PageRequest>any()))
                .thenThrow(new UserException("An error occurred"));
        assertThrows(UserException.class,
                () -> userService.searchUsers("Keyword", 1, 3, "Sort Field", Sort.Direction.ASC));
        verify(userRepository).searchUsers(Mockito.<String>any(), Mockito.<PageRequest>any());
    }

    /**
     * Method under test: {@link UserService#loadUserByUsername(String)}
     */
    @Test
    void testLoadUserByUsername() throws UnsupportedEncodingException, UsernameNotFoundException {
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
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        UserDetails actualLoadUserByUsernameResult = userService.loadUserByUsername("bashir.okala@hotmail.com");
        verify(userRepository).findByEmail(Mockito.<String>any());
        assertSame(user, actualLoadUserByUsernameResult);
    }

    /**
     * Method under test: {@link UserService#loadUserByUsername(String)}
     */
    @Test
    void testLoadUserByUsername2() throws UsernameNotFoundException {
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(emptyResult);
        assertThrows(UserException.class, () -> userService.loadUserByUsername("bashir.okala@hotmail.com"));
        verify(userRepository).findByEmail(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserService#loadUserByUsername(String)}
     */
    @Test
    void testLoadUserByUsername3() throws UsernameNotFoundException {
        when(userRepository.findByEmail(Mockito.<String>any())).thenThrow(new UserException("An error occurred"));
        assertThrows(UserException.class, () -> userService.loadUserByUsername("bashir.okala@hotmail.com"));
        verify(userRepository).findByEmail(Mockito.<String>any());
    }


    /**
     * Method under test: {@link UserService#unFollowUser(long, long)}
     */
    @Test
    void testUnFollowUser() {
        when(userRepository.existsById(Mockito.<Long>any())).thenReturn(true);
        assertThrows(UserException.class, () -> userService.unFollowUser(1L, 1L));
        verify(userRepository).existsById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserService#unFollowUser(long, long)}
     */
    @Test
    void testUnFollowUser2() {
        when(userRepository.existsById(Mockito.<Long>any())).thenReturn(false);
        assertThrows(UserException.class, () -> userService.unFollowUser(1L, 1L));
        verify(userRepository).existsById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserService#unFollowUser(long, long)}
     */
    @Test
    void testUnFollowUser3() {
        doNothing().when(userRepository).deleteByUserIdAndFollowerId(Mockito.<Long>any(), Mockito.<Long>any());
        when(userRepository.existsById(Mockito.<Long>any())).thenReturn(true);
        userService.unFollowUser(2L, 1L);
        verify(userRepository).deleteByUserIdAndFollowerId(Mockito.<Long>any(), Mockito.<Long>any());
        verify(userRepository, atLeast(1)).existsById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserService#unFollowUser(long, long)}
     */
    @Test
    void testUnFollowUser4() {
        when(userRepository.existsById(Mockito.<Long>any())).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> userService.unFollowUser(2L, 1L));
        verify(userRepository).existsById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserService#unFollowUser(long, long)}
     */
    @Test
    void testUnFollowUser5() {
        doThrow(new UserException("An error occurred")).when(userRepository)
                .deleteByUserIdAndFollowerId(Mockito.<Long>any(), Mockito.<Long>any());
        when(userRepository.existsById(Mockito.<Long>any())).thenReturn(true);
        assertThrows(UserException.class, () -> userService.unFollowUser(2L, 1L));
        verify(userRepository).deleteByUserIdAndFollowerId(Mockito.<Long>any(), Mockito.<Long>any());
        verify(userRepository, atLeast(1)).existsById(Mockito.<Long>any());
    }
}


