package com.example.backend.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Define the relationship to the Post to which the comment belongs
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

}
