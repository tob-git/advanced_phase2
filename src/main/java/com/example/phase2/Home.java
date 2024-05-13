package com.example.phase2;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Comparator;
import java.util.List;


public class Home {

    public static VBox build(Stage primaryStage, SocialMediaApp app, User currentUser, VBox postsLayout) {
        postsLayout.setPadding(new Insets(10));
        postsLayout.getChildren().clear();


        TextField postContentField = new TextField();
        postContentField.setPromptText("What's on your mind?");

        Button createPostButton = new Button("Create Post");
        createPostButton.setOnAction(e -> handleCreatePost(postContentField, postsLayout, currentUser, app,primaryStage));

        TextField friendUsernameField = new TextField();
        friendUsernameField.setPromptText("Enter friend's username");

        Button addFriendButton = new Button("Add Friend");
        addFriendButton.setOnAction(e -> handleAddFriend(friendUsernameField, postsLayout, currentUser, app, primaryStage));


        VBox postAndFriendBox = new VBox(10, postContentField, createPostButton, friendUsernameField, addFriendButton);
        postAndFriendBox.setAlignment(Pos.CENTER);

        // Setting up the scrollable area for posts
        ScrollPane scrollPane = new ScrollPane(postsLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox container = new VBox(postAndFriendBox, scrollPane);
        container.setPadding(new Insets(10));
        container.setAlignment(Pos.CENTER);

        // Inside build method or any other relevant place in FriendsPostsViewBuilder
        Button profileButton = new Button("Profile");
        profileButton.setOnAction(e -> UserProfileView.showUserProfile(primaryStage, currentUser, app));
        container.getChildren().add(profileButton);


        // Logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> app.logout(primaryStage));
        container.getChildren().add(logoutButton);  // Add the logout button at the bottom


        // Initial loading of posts
        updateFriendsPostsView(postsLayout, currentUser, app, primaryStage);

        return container;
    }

    private static void handleCreatePost(TextField postContentField, VBox postsLayout, User currentUser, SocialMediaApp app, Stage primaryStage) {
        String content = postContentField.getText();
        if (!content.isEmpty()) {
            currentUser.createPost(content);
            postContentField.clear();
            updateFriendsPostsView(postsLayout, currentUser, app, primaryStage);
        }
    }

    private static void handleAddFriend(TextField friendUsernameField, VBox postsLayout, User currentUser, SocialMediaApp app, Stage primaryStage) {
        String friendUsername = friendUsernameField.getText();
        if (!friendUsername.isEmpty() && !friendUsername.equals(currentUser.getUsername())) {
            try {
                User friend = SocialMediaApp.getNetworking().getUser(friendUsername);
                currentUser.addFriend(friend);
                friendUsernameField.clear();
                updateFriendsPostsView(postsLayout, currentUser, app, primaryStage);
                app.showAlert(Alert.AlertType.INFORMATION, "Friend Added", "Friend added successfully!");
            } catch (IllegalArgumentException ex) {
                app.showAlert(Alert.AlertType.ERROR, "Add Friend Error", ex.getMessage());
            }
        } else {
            app.showAlert(Alert.AlertType.ERROR, "Invalid Input", "Invalid username or cannot add yourself.");
        }
    }


    private static void updateFriendsPostsView(VBox postsLayout, User currentUser, SocialMediaApp app, Stage primaryStage) {
        postsLayout.getChildren().clear();  // Clear previous posts

        currentUser.getFriends().stream()  // Stream of friends
                .flatMap(friend -> friend.getPosts().stream())  // Stream of all posts from all friends
                .sorted(Comparator.comparing(Post::getTime).reversed())  // Sort by time, newest first
                .forEach(post -> addPostUI(postsLayout, post.getAuthor(), post, currentUser, app, primaryStage));  // Add posts to UI
    }

    private static void addPostUI(VBox postsLayout, User friend, Post post, User currentUser, SocialMediaApp app,Stage primaryStage) {
        Hyperlink postLink = new Hyperlink(friend.getUsername() + ": " + post.getContent());
        postLink.setOnAction(event -> {
            // Assuming FriendProfileView.showUserProfile takes the primary stage and a user object
            FriendProfileView.showUserProfile(primaryStage, friend, app);
        });
        TextField commentField = new TextField();
        commentField.setPromptText("Enter a comment");

        Button commentButton = new Button("Comment");
        commentButton.setOnAction(e -> {
            String commentContent = commentField.getText();
            post.addComment(currentUser, commentContent);
            commentField.clear();
            updateFriendsPostsView(postsLayout, currentUser, app, primaryStage);
        });

        Button likeButton = new Button();
        post.isLiked(currentUser, likeButton);
        likeButton.setOnAction(e -> {
            post.toggleLike(currentUser, likeButton);
            updateFriendsPostsView(postsLayout, currentUser, app, primaryStage);  // Refresh to show like status change
        });

        VBox commentsBox = new VBox(5);
        List<Comment> sortedComments = post.getComments().stream()
                .sorted(Comparator.comparing(Comment::getTime).reversed())
                .toList();

        for (Comment comment : sortedComments) {
            Label commentLabel = new Label(comment.getCommenter().getUsername() + ": " + comment.getText());
            commentsBox.getChildren().add(commentLabel);
        }

        HBox commentBox = new HBox(5, commentField, commentButton);
        commentBox.setAlignment(Pos.CENTER_LEFT);

        HBox postBox = new HBox(10, postLink, likeButton);
        postBox.setAlignment(Pos.CENTER_LEFT);

        VBox postWithComments = new VBox(10, postBox, commentsBox, commentBox);
        postWithComments.setPadding(new Insets(10));
        postWithComments.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-padding: 10;");

        postsLayout.getChildren().add(postWithComments);
    }

}