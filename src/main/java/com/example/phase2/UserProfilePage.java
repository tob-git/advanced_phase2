package com.example.phase2;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class UserProfilePage {
    private User user;
    private TextField bioField;
    private TextField profilePicField;
    private PasswordField passwordField;
    private Label usernameLabel;
    private ImageView profileImageView;

    public UserProfilePage(User user, Stage primaryStage) {
        this.user = user;
        start(primaryStage);
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Profile Page - " + user.getUsername());

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        usernameLabel = new Label("Username: " + user.getUsername());
        grid.add(usernameLabel, 0, 0);

        bioField = new TextField(user.getBio());
        bioField.setPromptText("Enter new bio");
        grid.add(new Label("Bio:"), 0, 1);
        grid.add(bioField, 1, 1);

        profilePicField = new TextField(user.getProfilePictureUrl());
        profilePicField.setPromptText("Enter new profile picture URL");
        grid.add(new Label("Profile Picture URL:"), 0, 2);
        grid.add(profilePicField, 1, 2);

        profileImageView = new ImageView(new Image(user.getProfilePictureUrl(), 100, 100, false, false));
        grid.add(profileImageView, 2, 2);

        passwordField = new PasswordField();
        passwordField.setPromptText("Enter new password");
        grid.add(new Label("Password:"), 0, 3);
        grid.add(passwordField, 1, 3);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> updateUserInfo());
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(saveButton);
        grid.add(hbBtn, 1, 4);

        Scene scene = new Scene(grid, 500, 450);
        primaryStage.setScene(scene);
        primaryStage.show();  // Make sure to show the stage
    }

    private void updateUserInfo() {
        user.setBio(bioField.getText());
        user.setProfilePictureUrl(profilePicField.getText());
        user.setPassword(passwordField.getText());
        // Assuming Database.update() is correctly implemented
        Database.update(SocialMediaApp.getNetworking().getUsers());

        profileImageView.setImage(new Image(profilePicField.getText(), 100, 100, false, false));
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Profile Updated Successfully!");
        alert.showAndWait();
    }
}
