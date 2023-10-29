package com.example.backend.controllers;


import com.example.backend.exceptions.UserException;
import com.example.backend.exceptions.UserNotFoundException;
import com.example.backend.models.dtos.*;
import com.example.backend.models.entities.User;
import com.example.backend.response.ApiErrorResponse;
import com.example.backend.response.ApiFailedResponse;
import com.example.backend.response.ApiSuccessResponse;
import com.example.backend.services.UserService;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ApiOperation(value = "create new user ", response = ApiSuccessResponse.class)
    public ResponseEntity<ApiSuccessResponse> registerUser(@Valid @RequestBody CreateUserRequest request) {
        return new ResponseEntity<>(new ApiSuccessResponse(userService.createUser(request)),HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    @ApiOperation(value = "login / authenticate user ", response = ApiSuccessResponse.class)
    public ResponseEntity<ApiSuccessResponse> authenticateUser(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse authenticationResponse = userService.loginUser(request);
        return new ResponseEntity<>(new ApiSuccessResponse(authenticationResponse), HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    @ApiOperation(value = "update user details", response = ApiSuccessResponse.class)
    public ResponseEntity<ApiSuccessResponse> updateUser(@PathVariable Long userId, @RequestBody @Valid UpdateUserRequest userRequest) {
        User user = userService.updateUser(userId, userRequest);
        return ResponseEntity.ok(new ApiSuccessResponse(user));
    }

    @GetMapping("/{userId}")
    @ApiOperation(value = "get user by ID ", response = User.class)
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/list")
    @ApiOperation(value = "get all users (paginated) ", response = ApiSuccessResponse.class)
    public ResponseEntity<Page<User>> getUser(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "sortField", defaultValue = "creationDate") String sortField,
            @RequestParam(name = "sortDirection", defaultValue = "DESC") Sort.Direction sortDirection
    ) {
        return ResponseEntity.ok(userService.getAllUsers(page, pageSize, sortField, sortDirection));
    }

    @GetMapping("/search/users")
    @ApiOperation(value = "search Users ", response = ApiSuccessResponse.class)
    public ResponseEntity<Page<User>> searchUsers(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "sortField", defaultValue = "creationDate") String sortField,
            @RequestParam(name = "sortDirection", defaultValue = "DESC") Sort.Direction sortDirection
    ) {
        return ResponseEntity.ok(userService.searchUsers(keyword, page, pageSize, sortField, sortDirection));
    }



    @PostMapping(value = "/{userId}/update-profile-photo",consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "upload profile photo ", response = ApiSuccessResponse.class)
    public ResponseEntity<ApiSuccessResponse> uploadProfilePicture(
            @RequestParam("user") Long userId,
            @RequestPart("profilePicture") MultipartFile profilePicture) throws IOException {

        User user = userService.uploadProfilePicture(userId, profilePicture);
        return ResponseEntity.ok(new ApiSuccessResponse(null));
    }

    @DeleteMapping("/{userId}")
    @ApiOperation(value = "delete user", response = ApiSuccessResponse.class)
    public ResponseEntity<ApiSuccessResponse> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new ApiSuccessResponse(null));
    }


    @PutMapping("/{userId}/follow/{followerId}")
    @ApiOperation(value = "follow user ", response = ApiSuccessResponse.class)
    public ResponseEntity<ApiSuccessResponse> followUser(@PathVariable Long userId, @PathVariable Long followerId) {
        userService.followUser(userId, followerId);
        return ResponseEntity.ok(new ApiSuccessResponse(null));
    }

    @PutMapping("/{userId}/unfollow/{unFollowerId}")
    @ApiOperation(value = "unfollow user ", response = ApiSuccessResponse.class)
    public ResponseEntity<ApiSuccessResponse> unFollowUser(@PathVariable Long userId, @PathVariable Long unFollowerId) {
        userService.unFollowUser(userId, unFollowerId);
        return ResponseEntity.ok(new ApiSuccessResponse(null));
    }

    @GetMapping("/{userId}/following")
    @ApiOperation(value = "get all people a user is following ", response = ApiSuccessResponse.class)
    public ResponseEntity<ApiSuccessResponse> getAllUserFollowing(@PathVariable long userId){
        Set<Followers> userFollowing = userService.getUserFollowing(userId);
        return ResponseEntity.ok(new ApiSuccessResponse(userFollowing));
    }

    @GetMapping("/{userId}/followers")
    @ApiOperation(value = "get all people following a user ", response = ApiSuccessResponse.class)
    public ResponseEntity<ApiSuccessResponse> getAllUserFollowers(@PathVariable long userId){
        Set<Followers> followers = userService.getUserFollowers(userId);
        return ResponseEntity.ok(new ApiSuccessResponse(followers));
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


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiFailedResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        log.error("handleMethodArgumentNotValidException/e :", e);
        List<String> errorList = new ArrayList<>();
        e.getAllErrors().forEach(objectError -> errorList.add(objectError.getDefaultMessage()));
        return new ResponseEntity<>(new ApiFailedResponse(errorList), HttpStatus.BAD_REQUEST);
    }
}
