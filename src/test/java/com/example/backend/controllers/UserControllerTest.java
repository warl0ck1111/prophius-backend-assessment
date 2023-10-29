package com.example.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.backend.config.JwtService;
import com.example.backend.exceptions.UserException;
import com.example.backend.exceptions.UserNotFoundException;
import com.example.backend.models.dtos.AuthenticationRequest;
import com.example.backend.models.dtos.AuthenticationResponse;
import com.example.backend.models.dtos.CreateUserRequest;
import com.example.backend.models.dtos.UpdateUserRequest;
import com.example.backend.models.entities.User;
import com.example.backend.models.enums.Role;
import com.example.backend.repositories.CommentRepository;
import com.example.backend.repositories.PostRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.response.ApiErrorResponse;
import com.example.backend.response.ApiFailedResponse;
import com.example.backend.response.ApiSuccessResponse;
import com.example.backend.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataInputStream;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Executable;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.catalina.realm.UserDatabaseRealm;
import org.apache.catalina.users.MemoryUserDatabase;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
class UserControllerTest {
    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    /**
     * Method under test: {@link UserController#getUser(int, int, String, Sort.Direction)}
     */
    @Test
    void testGetUser() {
       
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(new ArrayList<>()));

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);
        JwtService jwtService = new JwtService();
        ResponseEntity<Page<User>> actualUser = (new UserController(
                new UserService(userRepository, jwtService, authenticationManager, new BCryptPasswordEncoder(),
                        mock(PostRepository.class), mock(CommentRepository.class)))).getUser(1, 3, "Sort Field",
                Sort.Direction.ASC);
        verify(userRepository).findAll(Mockito.<Pageable>any());
        assertEquals(200, actualUser.getStatusCodeValue());
        assertTrue(actualUser.getBody().toList().isEmpty());
        assertTrue(actualUser.hasBody());
        assertTrue(actualUser.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserController#getUser(int, int, String, Sort.Direction)}
     */
    @Test
    void testGetUser2() {
       
        UserService userService = mock(UserService.class);
        when(userService.getAllUsers(anyInt(), anyInt(), Mockito.<String>any(), Mockito.<Sort.Direction>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        ResponseEntity<Page<User>> actualUser = (new UserController(userService)).getUser(1, 3, "Sort Field",
                Sort.Direction.ASC);
        verify(userService).getAllUsers(anyInt(), anyInt(), Mockito.<String>any(), Mockito.<Sort.Direction>any());
        assertEquals(200, actualUser.getStatusCodeValue());
        assertTrue(actualUser.getBody().toList().isEmpty());
        assertTrue(actualUser.hasBody());
        assertTrue(actualUser.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserController#getUser(int, int, String, Sort.Direction)}
     */
    @Test
    void testGetUser3() {
       
        UserService userService = mock(UserService.class);
        when(userService.getAllUsers(anyInt(), anyInt(), Mockito.<String>any(), Mockito.<Sort.Direction>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        ResponseEntity<Page<User>> actualUser = (new UserController(userService)).getUser(1, 3, "Sort Field",
                Sort.Direction.DESC);
        verify(userService).getAllUsers(anyInt(), anyInt(), Mockito.<String>any(), Mockito.<Sort.Direction>any());
        assertEquals(200, actualUser.getStatusCodeValue());
        assertTrue(actualUser.getBody().toList().isEmpty());
        assertTrue(actualUser.hasBody());
        assertTrue(actualUser.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserController#getUser(Long)}
     */
    @Test
    void testGetUser4() throws Exception {
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
        when(userService.getUser(anyLong())).thenReturn(user);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users/{userId}", 1L);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"email\":\"bashir.okala@hotmail.com\",\"username\":\"bashir.okala@hotmail.com\",\"role\":\"USER\",\"profilePicture"
                                        + "\":\"QVhBWEFYQVg=\"}"));
    }

    /**
     * Method under test: {@link UserController#getUser(Long)}
     */
    @Test
    void testGetUser5() throws Exception {
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
        when(userService.getUser(anyLong())).thenReturn(user);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users/{userId}", "Uri Variables",
                "Uri Variables");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"status\":\"error\",\"message\":\"Failed to convert value of type 'java.lang.String' to required type"
                                        + " 'java.lang.Long'; For input string: \\\"UriVariables\\\"\"}"));
    }

    /**
     * Method under test: {@link UserController#getUser(Long)}
     */
    @Test
    void testGetUser6() throws Exception {
        when(userService.getUser(anyLong())).thenThrow(new UserException("An error occurred"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users/{userId}", 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"status\":\"fail\",\"data\":\"An error occurred\"}"));
    }

    /**
     * Method under test: {@link UserController#getUser(Long)}
     */
    @Test
    void testGetUser7() throws Exception {
        when(userService.getUser(anyLong())).thenThrow(new UserNotFoundException("An error occurred"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/users/{userId}", 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"status\":\"fail\",\"data\":\"An error occurred\"}"));
    }

    /**
     * Method under test: {@link UserController#searchUsers(String, int, int, String, Sort.Direction)}
     */
    @Test
    void testSearchUsers() {
       
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.searchUsers(Mockito.<String>any(), Mockito.<PageRequest>any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);
        JwtService jwtService = new JwtService();
        ResponseEntity<Page<User>> actualSearchUsersResult = (new UserController(
                new UserService(userRepository, jwtService, authenticationManager, new BCryptPasswordEncoder(),
                        mock(PostRepository.class), mock(CommentRepository.class)))).searchUsers("Keyword", 1, 3, "Sort Field",
                Sort.Direction.ASC);
        verify(userRepository).searchUsers(Mockito.<String>any(), Mockito.<PageRequest>any());
        assertEquals(200, actualSearchUsersResult.getStatusCodeValue());
        assertTrue(actualSearchUsersResult.getBody().toList().isEmpty());
        assertTrue(actualSearchUsersResult.hasBody());
        assertTrue(actualSearchUsersResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserController#searchUsers(String, int, int, String, Sort.Direction)}
     */
    @Test
    void testSearchUsers2() {
       
        UserService userService = mock(UserService.class);
        when(userService.searchUsers(Mockito.<String>any(), anyInt(), anyInt(), Mockito.<String>any(),
                Mockito.<Sort.Direction>any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        ResponseEntity<Page<User>> actualSearchUsersResult = (new UserController(userService)).searchUsers("Keyword", 1,
                3, "Sort Field", Sort.Direction.ASC);
        verify(userService).searchUsers(Mockito.<String>any(), anyInt(), anyInt(), Mockito.<String>any(),
                Mockito.<Sort.Direction>any());
        assertEquals(200, actualSearchUsersResult.getStatusCodeValue());
        assertTrue(actualSearchUsersResult.getBody().toList().isEmpty());
        assertTrue(actualSearchUsersResult.hasBody());
        assertTrue(actualSearchUsersResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserController#searchUsers(String, int, int, String, Sort.Direction)}
     */
    @Test
    void testSearchUsers3() {
       
        UserService userService = mock(UserService.class);
        when(userService.searchUsers(Mockito.<String>any(), anyInt(), anyInt(), Mockito.<String>any(),
                Mockito.<Sort.Direction>any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        ResponseEntity<Page<User>> actualSearchUsersResult = (new UserController(userService)).searchUsers("Keyword", 1,
                3, "Sort Field", Sort.Direction.DESC);
        verify(userService).searchUsers(Mockito.<String>any(), anyInt(), anyInt(), Mockito.<String>any(),
                Mockito.<Sort.Direction>any());
        assertEquals(200, actualSearchUsersResult.getStatusCodeValue());
        assertTrue(actualSearchUsersResult.getBody().toList().isEmpty());
        assertTrue(actualSearchUsersResult.hasBody());
        assertTrue(actualSearchUsersResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserController#authenticateUser(AuthenticationRequest)}
     */
    @Test
    void testAuthenticateUser() throws Exception {
        when(userService.loginUser(Mockito.<AuthenticationRequest>any())).thenReturn(AuthenticationResponse.builder()
                .accessToken("ABC123")
                .email("bashir.okala@hotmail.com")
                .refreshToken("ABC123")
                .role("Role")
                .userId("42")
                .username("warl0ck")
                .build());

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("bashir.okala@hotmail.com");
        authenticationRequest.setPassword("password");
        String content = (new ObjectMapper()).writeValueAsString(authenticationRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/users/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"status\":\"success\",\"data\":{\"accessToken\":\"ABC123\",\"role\":\"Role\",\"userId\":\"42\",\"username\":\"warl0ck\","
                                        + "\"email\":\"bashir.okala@hotmail.com\",\"refreshToken\":\"ABC123\"}}"));
    }

    /**
     * Method under test: {@link UserController#deleteUser(Long)}
     */
    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(anyLong());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/users/{userId}", 1L);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"status\":\"success\",\"data\":null}"));
    }

    /**
     * Method under test: {@link UserController#deleteUser(Long)}
     */
    @Test
    void testDeleteUser2() throws Exception {
        doNothing().when(userService).deleteUser(anyLong());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/users/{userId}",
                "Uri Variables", "Uri Variables");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"status\":\"error\",\"message\":\"Failed to convert value of type 'java.lang.String' to required type"
                                        + " 'java.lang.Long'; For input string: \\\"UriVariables\\\"\"}"));
    }

    /**
     * Method under test: {@link UserController#deleteUser(Long)}
     */
    @Test
    void testDeleteUser3() throws Exception {
        doThrow(new UserException("An error occurred")).when(userService).deleteUser(anyLong());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/users/{userId}", 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"status\":\"fail\",\"data\":\"An error occurred\"}"));
    }

    /**
     * Method under test: {@link UserController#deleteUser(Long)}
     */
    @Test
    void testDeleteUser4() throws Exception {
        doThrow(new UserNotFoundException("An error occurred")).when(userService).deleteUser(anyLong());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/users/{userId}", 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"status\":\"fail\",\"data\":\"An error occurred\"}"));
    }

    /**
     * Method under test: {@link UserController#followUser(Long, Long)}
     */
    @Test
    void testFollowUser() throws Exception {
        doNothing().when(userService).followUser(anyLong(), anyLong());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/users/{userId}/follow/{followerId}", 1L, 1L);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"status\":\"success\",\"data\":null}"));
    }

    /**
     * Method under test: {@link UserController#followUser(Long, Long)}
     */
    @Test
    void testFollowUser2() throws Exception {
        doNothing().when(userService).followUser(anyLong(), anyLong());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/users/{userId}/follow/{followerId}", "Uri Variables", "Uri Variables", "Uri Variables");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"status\":\"error\",\"message\":\"Failed to convert value of type 'java.lang.String' to required type"
                                        + " 'java.lang.Long'; For input string: \\\"UriVariables\\\"\"}"));
    }

    /**
     * Method under test: {@link UserController#followUser(Long, Long)}
     */
    @Test
    void testFollowUser3() throws Exception {
        doThrow(new UserException("An error occurred")).when(userService).followUser(anyLong(), anyLong());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/users/{userId}/follow/{followerId}", 1L, 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"status\":\"fail\",\"data\":\"An error occurred\"}"));
    }

    /**
     * Method under test: {@link UserController#followUser(Long, Long)}
     */
    @Test
    void testFollowUser4() throws Exception {
        doThrow(new UserNotFoundException("An error occurred")).when(userService).followUser(anyLong(), anyLong());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/users/{userId}/follow/{followerId}", 1L, 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"status\":\"fail\",\"data\":\"An error occurred\"}"));
    }

    /**
     * Method under test: {@link UserController#unFollowUser(Long, Long)}
     */
    @Test
    void testUnFollowUser() throws Exception {
        doNothing().when(userService).unFollowUser(anyLong(), anyLong());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/users/{userId}/unfollow/{unFollowerId}", 1L, 1L);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"status\":\"success\",\"data\":null}"));
    }

    /**
     * Method under test: {@link UserController#unFollowUser(Long, Long)}
     */
    @Test
    void testUnFollowUser2() throws Exception {
        doNothing().when(userService).unFollowUser(anyLong(), anyLong());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/users/{userId}/unfollow/{unFollowerId}", "Uri Variables", "Uri Variables", "Uri Variables");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"status\":\"error\",\"message\":\"Failed to convert value of type 'java.lang.String' to required type"
                                        + " 'java.lang.Long'; For input string: \\\"UriVariables\\\"\"}"));
    }

    /**
     * Method under test: {@link UserController#unFollowUser(Long, Long)}
     */
    @Test
    void testUnFollowUser3() throws Exception {
        doThrow(new UserException("An error occurred")).when(userService).unFollowUser(anyLong(), anyLong());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/users/{userId}/unfollow/{unFollowerId}", 1L, 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"status\":\"fail\",\"data\":\"An error occurred\"}"));
    }

    /**
     * Method under test: {@link UserController#unFollowUser(Long, Long)}
     */
    @Test
    void testUnFollowUser4() throws Exception {
        doThrow(new UserNotFoundException("An error occurred")).when(userService).unFollowUser(anyLong(), anyLong());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/users/{userId}/unfollow/{unFollowerId}", 1L, 1L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"status\":\"fail\",\"data\":\"An error occurred\"}"));
    }

    /**
     * Method under test: {@link UserController#getAllUserFollowing(long)}
     */
    @Test
    void testGetAllUserFollowing() {
       
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.getUsersFollowing(anyLong())).thenReturn(new HashSet<>());
        when(userRepository.existsById(Mockito.<Long>any())).thenReturn(true);

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);
        JwtService jwtService = new JwtService();
        ResponseEntity<ApiSuccessResponse> actualAllUserFollowing = (new UserController(
                new UserService(userRepository, jwtService, authenticationManager, new BCryptPasswordEncoder(),
                        mock(PostRepository.class), mock(CommentRepository.class)))).getAllUserFollowing(1L);
        verify(userRepository).getUsersFollowing(anyLong());
        verify(userRepository).existsById(Mockito.<Long>any());
        assertEquals(200, actualAllUserFollowing.getStatusCodeValue());
        assertTrue(((Collection<Object>) actualAllUserFollowing.getBody().getData()).isEmpty());
        assertTrue(actualAllUserFollowing.hasBody());
        assertTrue(actualAllUserFollowing.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserController#getAllUserFollowing(long)}
     */
    @Test
    void testGetAllUserFollowing2() {
       
        UserService userService = mock(UserService.class);
        when(userService.getUserFollowing(anyLong())).thenReturn(new HashSet<>());
        ResponseEntity<ApiSuccessResponse> actualAllUserFollowing = (new UserController(userService))
                .getAllUserFollowing(1L);
        verify(userService).getUserFollowing(anyLong());
        assertEquals(200, actualAllUserFollowing.getStatusCodeValue());
        assertTrue(((Collection<Object>) actualAllUserFollowing.getBody().getData()).isEmpty());
        assertTrue(actualAllUserFollowing.hasBody());
        assertTrue(actualAllUserFollowing.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserController#getAllUserFollowers(long)}
     */
    @Test
    void testGetAllUserFollowers() {
        
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.getUsersFollowers(anyLong())).thenReturn(new HashSet<>());
        when(userRepository.existsById(Mockito.<Long>any())).thenReturn(true);

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);
        JwtService jwtService = new JwtService();
        ResponseEntity<ApiSuccessResponse> actualAllUserFollowers = (new UserController(
                new UserService(userRepository, jwtService, authenticationManager, new BCryptPasswordEncoder(),
                        mock(PostRepository.class), mock(CommentRepository.class)))).getAllUserFollowers(1L);
        verify(userRepository).getUsersFollowers(anyLong());
        verify(userRepository).existsById(Mockito.<Long>any());
        assertEquals(200, actualAllUserFollowers.getStatusCodeValue());
        assertTrue(((Collection<Object>) actualAllUserFollowers.getBody().getData()).isEmpty());
        assertTrue(actualAllUserFollowers.hasBody());
        assertTrue(actualAllUserFollowers.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserController#getAllUserFollowers(long)}
     */
    @Test
    void testGetAllUserFollowers2() {

        UserService userService = mock(UserService.class);
        when(userService.getUserFollowers(anyLong())).thenReturn(new HashSet<>());
        ResponseEntity<ApiSuccessResponse> actualAllUserFollowers = (new UserController(userService))
                .getAllUserFollowers(1L);
        verify(userService).getUserFollowers(anyLong());
        assertEquals(200, actualAllUserFollowers.getStatusCodeValue());
        assertTrue(((Collection<Object>) actualAllUserFollowers.getBody().getData()).isEmpty());
        assertTrue(actualAllUserFollowers.hasBody());
        assertTrue(actualAllUserFollowers.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserController#handleUserException(UserException)}
     */
    @Test
    void testHandleUserException() {
        ResponseEntity<ApiFailedResponse> actualHandleUserExceptionResult = userController
                .handleUserException(new UserException("An error occurred"));
        assertEquals("An error occurred", actualHandleUserExceptionResult.getBody().getData());
        assertEquals(400, actualHandleUserExceptionResult.getStatusCodeValue());
        assertTrue(actualHandleUserExceptionResult.hasBody());
        assertTrue(actualHandleUserExceptionResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserController#handleUserException(UserException)}
     */
    @Test
    void testHandleUserException2() {
        UserException e = mock(UserException.class);
        when(e.getMessage()).thenReturn("Not all who wander are lost");
        doNothing().when(e).printStackTrace();
        ResponseEntity<ApiFailedResponse> actualHandleUserExceptionResult = userController.handleUserException(e);
        verify(e).getMessage();
        verify(e).printStackTrace();
        assertEquals("Not all who wander are lost", actualHandleUserExceptionResult.getBody().getData());
        assertEquals(400, actualHandleUserExceptionResult.getStatusCodeValue());
        assertTrue(actualHandleUserExceptionResult.hasBody());
        assertTrue(actualHandleUserExceptionResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserController#handleUserNotFoundException(UserNotFoundException)}
     */
    @Test
    void testHandleUserNotFoundException() {
        ResponseEntity<ApiFailedResponse> actualHandleUserNotFoundExceptionResult = userController
                .handleUserNotFoundException(new UserNotFoundException("An error occurred"));
        assertEquals("An error occurred", actualHandleUserNotFoundExceptionResult.getBody().getData());
        assertEquals(404, actualHandleUserNotFoundExceptionResult.getStatusCodeValue());
        assertTrue(actualHandleUserNotFoundExceptionResult.hasBody());
        assertTrue(actualHandleUserNotFoundExceptionResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserController#handleUserNotFoundException(UserNotFoundException)}
     */
    @Test
    void testHandleUserNotFoundException2() {
        UserNotFoundException e = mock(UserNotFoundException.class);
        when(e.getMessage()).thenReturn("Not all who wander are lost");
        doNothing().when(e).printStackTrace();
        ResponseEntity<ApiFailedResponse> actualHandleUserNotFoundExceptionResult = userController
                .handleUserNotFoundException(e);
        verify(e).getMessage();
        verify(e).printStackTrace();
        assertEquals("Not all who wander are lost", actualHandleUserNotFoundExceptionResult.getBody().getData());
        assertEquals(404, actualHandleUserNotFoundExceptionResult.getStatusCodeValue());
        assertTrue(actualHandleUserNotFoundExceptionResult.hasBody());
        assertTrue(actualHandleUserNotFoundExceptionResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserController#handleException(Exception)}
     */
    @Test
    void testHandleException() {
        ResponseEntity<ApiErrorResponse> actualHandleExceptionResult = userController
                .handleException(new Exception("foo"));
        assertEquals("foo", actualHandleExceptionResult.getBody().getMessage());
        assertEquals(500, actualHandleExceptionResult.getStatusCodeValue());
        assertTrue(actualHandleExceptionResult.hasBody());
        assertTrue(actualHandleExceptionResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserController#handleMethodArgumentNotValidException(MethodArgumentNotValidException)}
     */
    @Test
    void testHandleMethodArgumentNotValidException() {
        ResponseEntity<ApiFailedResponse> actualHandleMethodArgumentNotValidExceptionResult = userController
                .handleMethodArgumentNotValidException(
                        new MethodArgumentNotValidException((Executable) null, new BindException("Target", "Object Name")));
        assertEquals(400, actualHandleMethodArgumentNotValidExceptionResult.getStatusCodeValue());
        assertTrue(
                ((Collection<Object>) actualHandleMethodArgumentNotValidExceptionResult.getBody().getData()).isEmpty());
        assertTrue(actualHandleMethodArgumentNotValidExceptionResult.hasBody());
        assertTrue(actualHandleMethodArgumentNotValidExceptionResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserController#handleMethodArgumentNotValidException(MethodArgumentNotValidException)}
     */
    @Test
    void testHandleMethodArgumentNotValidException2() {
        BeanPropertyBindingResult bindingResult = mock(BeanPropertyBindingResult.class);
        ArrayList<ObjectError> objectErrorList = new ArrayList<>();
        when(bindingResult.getAllErrors()).thenReturn(objectErrorList);
        ResponseEntity<ApiFailedResponse> actualHandleMethodArgumentNotValidExceptionResult = userController
                .handleMethodArgumentNotValidException(new MethodArgumentNotValidException((Executable) null, bindingResult));
        verify(bindingResult).getAllErrors();
        assertEquals(400, actualHandleMethodArgumentNotValidExceptionResult.getStatusCodeValue());
        assertTrue(actualHandleMethodArgumentNotValidExceptionResult.hasBody());
        assertTrue(actualHandleMethodArgumentNotValidExceptionResult.getHeaders().isEmpty());
        assertEquals(objectErrorList, actualHandleMethodArgumentNotValidExceptionResult.getBody().getData());
    }

    /**
     * Method under test: {@link UserController#handleMethodArgumentNotValidException(MethodArgumentNotValidException)}
     */
    @Test
    void testHandleMethodArgumentNotValidException3() {
        ArrayList<ObjectError> objectErrorList = new ArrayList<>();
        objectErrorList.add(
                new ObjectError("handleMethodArgumentNotValidException/e :", "handleMethodArgumentNotValidException/e :"));
        BeanPropertyBindingResult bindingResult = mock(BeanPropertyBindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(objectErrorList);
        ResponseEntity<ApiFailedResponse> actualHandleMethodArgumentNotValidExceptionResult = userController
                .handleMethodArgumentNotValidException(new MethodArgumentNotValidException((Executable) null, bindingResult));
        verify(bindingResult).getAllErrors();
        Object data = actualHandleMethodArgumentNotValidExceptionResult.getBody().getData();
        assertEquals("handleMethodArgumentNotValidException/e :", ((List<String>) data).get(0));
        assertEquals(1, ((Collection<String>) data).size());
        assertEquals(400, actualHandleMethodArgumentNotValidExceptionResult.getStatusCodeValue());
        assertTrue(actualHandleMethodArgumentNotValidExceptionResult.hasBody());
        assertTrue(actualHandleMethodArgumentNotValidExceptionResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserController#handleMethodArgumentNotValidException(MethodArgumentNotValidException)}
     */
    @Test
    void testHandleMethodArgumentNotValidException4() {
        ArrayList<ObjectError> objectErrorList = new ArrayList<>();
        objectErrorList.add(
                new ObjectError("handleMethodArgumentNotValidException/e :", "handleMethodArgumentNotValidException/e :"));
        objectErrorList.add(
                new ObjectError("handleMethodArgumentNotValidException/e :", "handleMethodArgumentNotValidException/e :"));
        BeanPropertyBindingResult bindingResult = mock(BeanPropertyBindingResult.class);
        when(bindingResult.getAllErrors()).thenReturn(objectErrorList);
        ResponseEntity<ApiFailedResponse> actualHandleMethodArgumentNotValidExceptionResult = userController
                .handleMethodArgumentNotValidException(new MethodArgumentNotValidException((Executable) null, bindingResult));
        verify(bindingResult).getAllErrors();
        Object data = actualHandleMethodArgumentNotValidExceptionResult.getBody().getData();
        assertEquals("handleMethodArgumentNotValidException/e :", ((List<String>) data).get(0));
        assertEquals("handleMethodArgumentNotValidException/e :", ((List<String>) data).get(1));
        assertEquals(2, ((Collection<String>) data).size());
        assertEquals(400, actualHandleMethodArgumentNotValidExceptionResult.getStatusCodeValue());
        assertTrue(actualHandleMethodArgumentNotValidExceptionResult.hasBody());
        assertTrue(actualHandleMethodArgumentNotValidExceptionResult.getHeaders().isEmpty());
    }

    /**
     * Method under test: {@link UserController#authenticateUser(AuthenticationRequest)}
     */
    @Test
    void testAuthenticateUser2() throws Exception {
        when(userService.loginUser(Mockito.<AuthenticationRequest>any())).thenReturn(AuthenticationResponse.builder()
                .accessToken("ABC123")
                .email("bashir.okala@hotmail.com")
                .refreshToken("ABC123")
                .role("Role")
                .userId("42")
                .username("warl0ck")
                .build());
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/api/users/authenticate");
        postResult.characterEncoding("https://example.org/example");

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("bashir.okala@hotmail.com");
        authenticationRequest.setPassword("password");
        String content = (new ObjectMapper()).writeValueAsString(authenticationRequest);
        MockHttpServletRequestBuilder requestBuilder = postResult.contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(415));
    }

    /**
     * Method under test: {@link UserController#authenticateUser(AuthenticationRequest)}
     */
    @Test
    void testAuthenticateUser3() throws Exception {
        when(userService.loginUser(Mockito.<AuthenticationRequest>any()))
                .thenThrow(new UserException("An error occurred"));

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("bashir.okala@hotmail.com");
        authenticationRequest.setPassword("password");
        String content = (new ObjectMapper()).writeValueAsString(authenticationRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/users/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"status\":\"fail\",\"data\":\"An error occurred\"}"));
    }

    /**
     * Method under test: {@link UserController#authenticateUser(AuthenticationRequest)}
     */
    @Test
    void testAuthenticateUser4() throws Exception {
        when(userService.loginUser(Mockito.<AuthenticationRequest>any()))
                .thenThrow(new UserNotFoundException("An error occurred"));

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("bashir.okala@hotmail.com");
        authenticationRequest.setPassword("password");
        String content = (new ObjectMapper()).writeValueAsString(authenticationRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/users/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"status\":\"fail\",\"data\":\"An error occurred\"}"));
    }

    /**
     * Method under test: {@link UserController#registerUser(CreateUserRequest)}
     */
    @Test
    void testRegisterUser() throws Exception {
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON);
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content((new ObjectMapper()).writeValueAsString(null));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"status\":\"error\",\"message\":\"Required request body is missing: public org.springframework.http"
                                        + ".ResponseEntity<com.example.backend.response.ApiSuccessResponse> com.example.backend.controllers"
                                        + ".UserController.registerUser(com.example.backend.models.dtos.CreateUserRequest)\"}"));
    }

    /**
     * Method under test: {@link UserController#registerUser(CreateUserRequest)}
     */
    @Test
    void testRegisterUser2() throws Exception {
        org.apache.catalina.User user = mock(org.apache.catalina.User.class);
        when(user.getName()).thenReturn("Name");
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/api/users/register");
        postResult.principal(new UserDatabaseRealm.UserDatabasePrincipal(user, new MemoryUserDatabase()));
        MockHttpServletRequestBuilder contentTypeResult = postResult.contentType(MediaType.APPLICATION_JSON);
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content((new ObjectMapper()).writeValueAsString(null));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"status\":\"error\",\"message\":\"Required request body is missing: public org.springframework.http"
                                        + ".ResponseEntity<com.example.backend.response.ApiSuccessResponse> com.example.backend.controllers"
                                        + ".UserController.registerUser(com.example.backend.models.dtos.CreateUserRequest)\"}"));
    }

    /**
     * Method under test: {@link UserController#updateUser(Long, UpdateUserRequest)}
     */
    @Test
    void testUpdateUser() throws Exception {
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.put("/api/users/{userId}", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult.content(objectMapper
                .writeValueAsString(UpdateUserRequest.builder().email("bashir.okala@hotmail.com").username("warl0ck").build()));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"status\":\"error\",\"message\":\"Type definition error: [simple type, class com.example.backend.models"
                                        + ".dtos.UpdateUserRequest]\"}"));
    }

    /**
     * Method under test: {@link UserController#uploadProfilePicture(Long, MultipartFile)}
     */
    @Test
    void testUploadProfilePicture() throws Exception {
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/api/users/42/update-profile-photo");
        MockHttpServletRequestBuilder paramResult = postResult.param("profilePicture",
                String.valueOf(new MockMultipartFile("Name", (InputStream) null)));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("user", String.valueOf(1L));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(415));
    }

    /**
     * Method under test: {@link UserController#uploadProfilePicture(Long, MultipartFile)}
     */
    @Test
    void testUploadProfilePicture2() throws Exception {
        DataInputStream contentStream = mock(DataInputStream.class);
        when(contentStream.transferTo(Mockito.<OutputStream>any())).thenReturn(1L);
        doNothing().when(contentStream).close();
        MockHttpServletRequestBuilder paramResult = MockMvcRequestBuilders.post("/api/users/42/update-profile-photo")
                .param("profilePicture", String.valueOf(new MockMultipartFile("Name", contentStream)));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("user", String.valueOf(1L));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(415));
    }
}

