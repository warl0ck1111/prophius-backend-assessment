package com.example.backend.services;

import com.example.backend.config.JwtService;
import com.example.backend.exceptions.UserException;
import com.example.backend.exceptions.UserNotFoundException;
import com.example.backend.models.dtos.AuthenticationRequest;
import com.example.backend.models.dtos.AuthenticationResponse;
import com.example.backend.models.dtos.CreateUserRequest;
import com.example.backend.models.dtos.UpdateUserRequest;
import com.example.backend.models.entities.Comment;
import com.example.backend.models.entities.Post;
import com.example.backend.models.entities.User;
import com.example.backend.models.enums.Role;
import com.example.backend.repositories.CommentRepository;
import com.example.backend.repositories.PostRepository;
import com.example.backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public AuthenticationResponse createUser(CreateUserRequest userRequest) {
        log.info("createUser/userRequest = {}", userRequest);
        try {

            if (userRequest != null) {
                //check if user exists
                Boolean userExists = userRepository.existsByEmail(userRequest.getEmail().toLowerCase());
                Boolean userNameExists = userRepository.existsByUsername(userRequest.getUsername().toLowerCase());
                if (userExists) {
                    log.error("user with email {} already exists", userRequest.getEmail());
                    throw new UserException(String.format("user with email: %s already exists", userRequest.getEmail()));
                } else if (userNameExists) {
                    log.error("username already exists");
                    throw new UserException("username already exists");
                } else { // create new user
                    String encodedPassword = passwordEncoder.encode((userRequest.getPassword()));

                    User newUser = User.builder()
                            .email(userRequest.getEmail().toLowerCase().trim())
                            .username(userRequest.getUsername().toLowerCase().trim())
                            .password(encodedPassword)
                            .role(Role.USER)
                            .profilePicture(null)
                            .enabled(true)
                            .locked(false)
                            .followers(Collections.emptySet())
                            .following(Collections.emptySet())
                            .build();
                    User user = userRepository.save(newUser);
                    log.info("createUser/ user {} created successfully", userRequest.getEmail());

                    //todo: send activation mail

                    var jwtToken = jwtService.generateToken(user);
                    var refreshToken = jwtService.generateRefreshToken(user);
                    return AuthenticationResponse.builder()
                            .accessToken(jwtToken)
                            .refreshToken(refreshToken)
                            .userId(String.valueOf(user.getId()))
                            .email(user.getEmail())
                            .username(user.getUsername())
                            .role(user.getRole().name())
                            .build();
                }
            }
        } catch (Exception e) {
            log.error("createUser/there was an exception=" + e);
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    public AuthenticationResponse loginUser(@NotNull AuthenticationRequest request) {
        final String email = request.getEmail().trim().toLowerCase();
        log.info("loginUser/email:{}", email);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.getPassword()));


        final User user = findUserByEmail(request.getEmail().toLowerCase());
        if (user != null) {
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);

            return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).userId(String.valueOf(user.getId())).email(user.getEmail()).build();
        } else log.info("loginUser/user:{} not found", email);
        return null;
    }


    public User getUser(long userId) {
        log.info("getUser/userId = {}", userId);
        return findUserById(userId);
    }

    public User updateUser(long userId, UpdateUserRequest userRequest) {
        log.info("updateUser/userRequest = {}", userRequest);
        User user = findUserById(userId);
        //todo: ideally email should be verified before updating but...
        user.setEmail(userRequest.getEmail());
        user.setUsername(userRequest.getUsername());
        return userRepository.save(user);
    }


    public void followUser(long followerId, long followeeId) {
        User followee = findUserById(followeeId);

        User newFollower = findUserById(followerId);

        // Add newFollower to the following list of user
        followee.getFollowing().add(newFollower);
        User save = userRepository.save(followee);


    }


    public void unFollowUser(long unFollowerId, long followeeId) {
        User unFollower = findUserById(unFollowerId);
        User followee = findUserById(followeeId);

        // Remove userToUnfollow from the following list of user
        unFollower.getFollowing().remove(unFollower);
        userRepository.save(unFollower);

    }

    public Set<User> getAllUserFollowers(long userId) {
        User user = findUserById(userId);
        return user.getFollowers();
    }

    public Set<User> getAllUserFollowing(long userId) {
        User user = findUserById(userId);
        return user.getFollowing();
    }

    public User uploadProfilePicture(long userId, MultipartFile profilePicture) throws IOException {
        User userProfile = getUser(userId);
        userProfile.setProfilePicture(profilePicture.getBytes());
        return userRepository.save(userProfile);
    }

    public void deleteUser(long userId) {
        log.info("deleteUser/userId = {}", userId);
        User user = findUserById(userId);

        // Remove the user from posts
        List<Post> userPosts = postRepository.findAllByUser(user);
        for (Post post : userPosts) {
            post.setUser(null);
            postRepository.save(post);
        }

        // Remove the user from comments
        List<Comment> userComments = commentRepository.findAllByUser(user);
        for (Comment comment : userComments) {
            comment.setUser(null);
            commentRepository.save(comment);
        }

        // Delete the user
        userRepository.delete(user);
    }

    public Page<User> getAllUsers(int page, int pageSize, String sortField, Sort.Direction sortDirection) {
        log.info("getAllUsers/page = {}", page);
        log.info("getAllUsers/pageSize = {}", pageSize);
        log.info("getAllUsers/sortField = {}", sortField);
        log.info("getAllUsers/sortDirection = {}", sortDirection);
        Sort sort = Sort.by(sortDirection, sortField);
        return userRepository.findAll(PageRequest.of(page, pageSize, sort));
    }

    public Page<User> searchUsers(String keyword, int page, int pageSize, String sortField, Sort.Direction sortDirection) {
        log.info("searchUsers/keyword = {}", keyword);
        log.info("searchUsers/page = {}", page);
        log.info("searchUsers/pageSize = {}", pageSize);
        log.info("searchUsers/sortField = {}", sortField);
        log.info("searchUsers/sortDirection = {}", sortDirection);
        Sort sort = Sort.by(sortDirection, sortField);
        return userRepository.searchUsers(keyword, PageRequest.of(page, pageSize, sort));
    }


    @Transactional
    public void enableUser(String email) {
        log.info("enableUser/email: {}", email);
        User user = findUserByEmail(email);
        user.setEnabled(true);
        userRepository.save(user);
        log.info("enableUser/user enabled successfully");

    }


    @Transactional
    public void disableUser(String email) {
        log.info("disableUser/email: {}", email);
        User user = findUserByEmail(email);
        user.setEnabled(false);
        userRepository.save(user);
        log.info("disableUser/user disabled successfully");

    }


    @Transactional
    public void lockUser(String email) {
        log.info("lockUser/email: {}", email);
        User user = findUserByEmail(email);
        user.setLocked(true);
        userRepository.save(user);
        log.info("lockUser/user locked successfully");
    }


    @Transactional
    public void unlockUser(String email) {
        log.info("unlockUser/email: {}", email);
        User user = findUserByEmail(email);
        user.setLocked(false);
        userRepository.save(user);
        log.info("unlockUser/user locked successfully");
    }


    private User findUserById(long userId) {
        log.info("findUserById/userId = {}", userId);
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("user not found"));
    }

    private User findUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("user not found"));
        return user;
    }

}
