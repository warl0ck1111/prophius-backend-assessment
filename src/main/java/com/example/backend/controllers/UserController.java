package com.example.backend.controllers;


import com.example.backend.exceptions.UserException;
import com.example.backend.exceptions.UserNotFoundException;
import com.example.backend.models.dtos.AuthenticationRequest;
import com.example.backend.models.dtos.AuthenticationResponse;
import com.example.backend.models.dtos.CreateUserRequest;
import com.example.backend.models.dtos.UpdateUserRequest;
import com.example.backend.models.entities.User;
import com.example.backend.response.ApiErrorResponse;
import com.example.backend.response.ApiFailedResponse;
import com.example.backend.response.ApiSuccessResponse;
import com.example.backend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiSuccessResponse> register(@Valid @RequestBody CreateUserRequest request) {
        return new ResponseEntity<>(new ApiSuccessResponse(userService.createUser(request)),HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiSuccessResponse> authenticate(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse authenticationResponse = userService.loginUser(request);
        return new ResponseEntity<>(new ApiSuccessResponse(authenticationResponse), HttpStatus.OK);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/list")
    public ResponseEntity<Page<User>> getUser(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "sortField", defaultValue = "creationDate") String sortField,
            @RequestParam(name = "sortDirection", defaultValue = "DESC") Sort.Direction sortDirection
    ) {
        return ResponseEntity.ok(userService.getAllUsers(page, pageSize, sortField, sortDirection));
    }

    @GetMapping("/search/users")
    public ResponseEntity<Page<User>> searchUsers(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "sortField", defaultValue = "creationDate") String sortField,
            @RequestParam(name = "sortDirection", defaultValue = "DESC") Sort.Direction sortDirection
    ) {
        return ResponseEntity.ok(userService.searchUsers(keyword, page, pageSize, sortField, sortDirection));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody @Valid UpdateUserRequest userRequest) {
        return ResponseEntity.ok(userService.updateUser(userId, userRequest));
    }

    @PostMapping("/{userId}/update-profile-photo")
    public ResponseEntity<User> uploadProfilePicture(
            @RequestParam("user") Long userId,
            @RequestParam("profilePicture") MultipartFile profilePicture) throws IOException {

        User user = userService.uploadProfilePicture(userId, profilePicture);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{userId}/follow/{followeeId}")
    public ResponseEntity<User> followUser(@PathVariable Long userId, @PathVariable Long followeeId) {
        userService.followUser(userId, followeeId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/unfollow/{followeeId}")
    public ResponseEntity<User> unFollowUser(@PathVariable Long userId, @PathVariable Long followeeId) {
        userService.unFollowUser(userId, followeeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<Set<User>> getAllUserFollowing(@PathVariable long userId){
        Set<User> userFollowing = userService.getAllUserFollowing(userId);
        return ResponseEntity.ok(userFollowing);
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<Set<User>> getAllUserFollowers(@PathVariable long userId){
        Set<User> userFollowers = userService.getAllUserFollowers(userId);
        return ResponseEntity.ok(userFollowers);
    }


    @PostMapping("/{email}/enable_acc")
    public ResponseEntity<ApiSuccessResponse> enableUser(@PathVariable String email) {
        userService.enableUser(email);
        return new ResponseEntity<>(new ApiSuccessResponse(null), HttpStatus.OK);
    }


    @PostMapping("/{email}/disable_acc")
    public ResponseEntity<ApiSuccessResponse> disableUser(@PathVariable String email) {
        userService.disableUser(email);
        return new ResponseEntity<>(new ApiSuccessResponse(null), HttpStatus.OK);
    }


    @PostMapping("/{email}/lock_acc")
    public ResponseEntity<ApiSuccessResponse> lockUserAccount(@PathVariable String email) {
        userService.lockUser(email);
        return new ResponseEntity<>(new ApiSuccessResponse(null), HttpStatus.OK);
    }


    @PostMapping("/{email}/unlock_acc")
    public ResponseEntity<ApiSuccessResponse> unlockUserAccount(@PathVariable String email) {
        userService.unlockUser(email);
        return new ResponseEntity<>(new ApiSuccessResponse(null), HttpStatus.OK);
    }



    /**
     * Exceptions Handlers
     */


    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiFailedResponse> handleUserException(UserException e) {
        log.info("handleUserException/");
        log.info("handleUserException/e="+e);
        e.printStackTrace();
        return new ResponseEntity<>(new ApiFailedResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiFailedResponse> handleUserNotFoundException(UserNotFoundException e) {
        log.info("handleUserNotFoundException/");
        log.info("handleUserNotFoundException/e="+e);
        e.printStackTrace();
        return new ResponseEntity<>(new ApiFailedResponse(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception e) {
        log.info("handleException/");
        log.error("handleException/e="+e);
        e.printStackTrace();
        return new ResponseEntity<>(new ApiErrorResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
