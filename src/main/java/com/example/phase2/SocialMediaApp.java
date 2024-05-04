package com.example.phase2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.Serializable;
import java.util.*;

public class SocialMediaApp extends Application implements Serializable {
    private static final Networking networking = new Networking(); // Networking instance
    private User currentUser;

    // Fields for post and friend operations
    private TextField postContentField = new TextField();
    private TextField friendUsernameField = new TextField();
    private VBox postsLayout = new VBox(15);

    public static Networking getNetworking() {
        return networking;
    }

    private VBox createLoginForm(Stage primaryStage, VBox mainLayout) {
        VBox loginForm = new VBox(10);
        loginForm.setPadding(new Insets(20, 20, 20, 20));
        loginForm.setAlignment(Pos.CENTER);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(100);
        Button switchToSignUp = new Button("Sign Up");
        switchToSignUp.setPrefWidth(100);

        loginButton.setOnAction(e -> {
            try {
                currentUser = networking.loginUser(usernameField.getText(), passwordField.getText());
                showFriendsPosts(primaryStage, mainLayout);
            } catch (IllegalArgumentException ex) {
                showAlert(Alert.AlertType.ERROR, "Login Error", ex.getMessage());
            }
        });

        switchToSignUp.setOnAction(e -> mainLayout.getChildren().setAll(createSignUpForm(primaryStage, mainLayout)));

        loginForm.getChildren().addAll(new Label("Username:"), usernameField,
                new Label("Password:"), passwordField, loginButton, switchToSignUp);

        return loginForm;
    }

    private VBox createSignUpForm(Stage primaryStage, VBox mainLayout) {
        VBox signUpForm = new VBox(10);
        signUpForm.setPadding(new Insets(20, 20, 20, 20));
        signUpForm.setAlignment(Pos.CENTER);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Choose a username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Choose a password");
        TextField bioField = new TextField();
        bioField.setPromptText("Tell us about yourself");
        TextField profilePicUrlField = new TextField();
        profilePicUrlField.setPromptText("Link to your profile picture");

        Button signUpButton = new Button("Sign Up");
        signUpButton.setPrefWidth(100);
        Button switchToLogin = new Button("Login");
        switchToLogin.setPrefWidth(100);

        signUpButton.setOnAction(e -> {
            try {
                boolean registered = networking.registerUser(usernameField.getText(), bioField.getText(), profilePicUrlField.getText(), passwordField.getText());
                if (registered) {
                    showAlert(Alert.AlertType.INFORMATION, "Registration Success", "Registration Successful!");
                    mainLayout.getChildren().setAll(createLoginForm(primaryStage, mainLayout));
                } else {
                    showAlert(Alert.AlertType.ERROR, "Registration Failed", "User already exists.");
                }
            } catch (IllegalArgumentException ex) {
                showAlert(Alert.AlertType.ERROR, "Registration Error", ex.getMessage());
            }
        });

        switchToLogin.setOnAction(e -> mainLayout.getChildren().setAll(createLoginForm(primaryStage, mainLayout)));

        signUpForm.getChildren().addAll(new Label("Username:"), usernameField,
                new Label("Password:"), passwordField, new Label("Bio:"), bioField,
                new Label("Profile Picture URL:"), profilePicUrlField, signUpButton, switchToLogin);

        return signUpForm;
    }

    private VBox createFriendsPostsView(Stage primaryStage, VBox mainLayout) {
        postsLayout.setPadding(new Insets(20, 20, 20, 20));

        // Create Post components
        postContentField.setPromptText("What's on your mind?");
        postContentField.setTooltip(new Tooltip("Enter your post content"));
        Button createPostButton = new Button("Create Post");

        createPostButton.setOnAction(e -> {
            try {
                String content = postContentField.getText();
                currentUser.createPost(content);
                postContentField.clear();
                updateFriendsPostsView(postsLayout);
                showAlert(Alert.AlertType.INFORMATION, "Post Success", "Post created successfully!");
            } catch (IllegalArgumentException ex) {
                showAlert(Alert.AlertType.ERROR, "Post Error", ex.getMessage());
            }
        });

        // Add components for adding friends
        friendUsernameField.setPromptText("Enter friend's username");
        friendUsernameField.setTooltip(new Tooltip("Enter the username of the friend you want to add"));
        Button addFriendButton = new Button("Add Friend");

        addFriendButton.setOnAction(e -> {
            try {
                User friend = networking.getUser(friendUsernameField.getText());
                if (friendUsernameField.getText().matches(currentUser.getUsername())){
                    throw new IllegalArgumentException("You cannot add yourself as a friend.");
                }
                currentUser.addFriend(friend);
                updateFriendsPostsView(postsLayout);
                friendUsernameField.clear();
                showAlert(Alert.AlertType.INFORMATION, "Friend Added", "Friend added successfully!");
            } catch (IllegalArgumentException ex) {
                showAlert(Alert.AlertType.ERROR, "Add Friend Error", ex.getMessage());
            }
        });

        VBox postAndFriendBox = new VBox(10, postContentField, createPostButton, friendUsernameField, addFriendButton);
        postAndFriendBox.setAlignment(Pos.CENTER);
        postsLayout.getChildren().add(0, postAndFriendBox);

        // Update and display posts
        updateFriendsPostsView(postsLayout);

        // Logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> mainLayout.getChildren().setAll(createLoginForm(primaryStage, mainLayout)));
        postsLayout.getChildren().add(logoutButton);

        return postsLayout;
    }

    private void updateFriendsPostsView(VBox postsLayout) {
        postsLayout.getChildren().removeIf(node -> node instanceof VBox && !node.equals(postContentField.getParent()));
        List<User> friends = currentUser.getFriends();

        for (User friend : friends) {
            for (Post post : friend.getPosts()) {
                addPostUI(postsLayout, friend, post);
            }
        }
    }

    private void addPostUI(VBox postsLayout, User user, Post post) {
        Label postLabel = new Label(user.getUsername() + ": " + post.getContent());

        // Comment button and field
        TextField commentField = new TextField();
        commentField.setPromptText("Enter a comment");
        Button commentButton = new Button("Comment");
        commentButton.setOnAction(e -> {
            String commentContent = commentField.getText();
            post.addComment(currentUser, commentContent);
            commentField.clear();
            showAlert(Alert.AlertType.INFORMATION, "Comment Success", "Comment added successfully!");
        });

        Button likeButton = new Button();
        post.isLiked(currentUser, likeButton);
        likeButton.setOnAction(e -> post.toggleLike(currentUser, likeButton));

        HBox commentBox = new HBox(5, commentField, commentButton);
        commentBox.setAlignment(Pos.CENTER_LEFT);
        HBox postBox = new HBox(10, postLabel, likeButton);
        postBox.setAlignment(Pos.CENTER_LEFT);
        VBox postWithComments = new VBox(10, postBox, commentBox);
        postWithComments.setPadding(new Insets(10, 10, 10, 10));
        postWithComments.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-padding: 10;");
        postsLayout.getChildren().add(postWithComments);
    }

    private void showFriendsPosts(Stage primaryStage, VBox mainLayout) {
        VBox postsLayout = createFriendsPostsView(primaryStage, mainLayout);
        Scene postsScene = new Scene(postsLayout, 500, 500);
        primaryStage.setScene(postsScene);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Social Media Platform");
        VBox mainLayout = new VBox();
        mainLayout.getChildren().add(createLoginForm(primaryStage, mainLayout));
        primaryStage.setScene(new Scene(mainLayout, 400, 300));
        primaryStage.show();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
