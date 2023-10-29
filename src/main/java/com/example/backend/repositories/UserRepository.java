package com.example.backend.repositories;



import com.example.backend.models.dtos.Followers;
import com.example.backend.models.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', ?1, '%'))")
    Page<User> searchUsers(String keyword, PageRequest pageRequest);

    Optional<User> findByEmail(String email);


    /**
     * unfollow user
     * @param userId user being followed
     * @param unfollowerId id of the user performing the unfollow action
     */
    @Modifying
    @Query(value = "DELETE FROM user_followers WHERE user_id = :userId AND follower_id = :unfollowerId", nativeQuery = true)
    void deleteByUserIdAndFollowerId(@Param("userId") Long userId, @Param("unfollowerId") Long unfollowerId);


    /**
     * follow user
     * @param userId user being followed
     * @param followerId id of the user performing the follow action
     */
    @Modifying
    @Query(value = "INSERT INTO user_followers (user_id, follower_id) VALUES (:userId, :followerId)", nativeQuery = true)
    void insertUserFollower(@Param("userId") Long userId, @Param("followerId") Long followerId);


    @Query(value = "SELECT u.id as userId,  u.email, username FROM USERS U LEFT JOIN public.user_followers UF on U.id = UF.follower_id where uf.user_id = :userId", nativeQuery = true)
    Set<Followers> getUsersFollowers(@Param("userId") long userId);


    @Query(value = "SELECT u.id as userId,  u.email, username FROM USERS U LEFT JOIN public.user_followers UF on U.id = UF.user_id where uf.follower_id = :followerId", nativeQuery = true)
    Set<Followers> getUsersFollowing(@Param("followerId") long followerId);
}
