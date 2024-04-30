package com.example.phase2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Post {
    private final User author;
    private String content;
    private List<Comment> comments;
    private Set<User> likes;
    private final LocalDateTime time;

    public Post(User author, String content) {
        validate(author, content);
        this.author = author;
        this.content = content;
        this.comments = new ArrayList<>();
        this.likes = new HashSet<>();
        this.time = LocalDateTime.now();
    }
    private void validate(User author, String content) {
        if (author == null) {
            throw new IllegalArgumentException("author must not be null");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("content must not be null or empty");
        }
    }

    // Method to add a comment
    public void addComment(User commenter, String text) {
        Comment newComment = new Comment(commenter, text);
        comments.add(newComment);
    }

    // Method to like a post
    public void toggleLike(User user) {
        if (!likes.add(user)) {
            likes.remove(user);
        }
    }

    public boolean isLiked(User user) {
        return likes.contains(user);
    }

    // Getters
    public User getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public List<Comment> getComments() {
        return new ArrayList<>(comments); // Return a copy to protect encapsulation
    }

    public List<User> getLikes() {
        return new ArrayList<>(likes); // Return a copy to protect encapsulation
    }
    public int getLikeCount() {
        return likes.size();
    }

    public int getCommentCount() {
        return comments.size();
    }
    public LocalDateTime getTime() {
        return time;
    }
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedTime = time.format(formatter);
        return "Post{" +
                "author=" + author +
                ", content='" + content + '\'' +
                ", comments=" + comments.size() +
                ", likes=" + likes.size() +
                ", time=" + formattedTime +
                '}';
    }
}
