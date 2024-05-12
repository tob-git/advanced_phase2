package com.example.phase2;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserProfileView {

    public static void showUserProfile(Stage primaryStage, User currentUser, SocialMediaApp app) {
        VBox profileLayout = new VBox(10);
        profileLayout.setPadding(new Insets(10));
        profileLayout.setAlignment(Pos.CENTER);

        TextField usernameField = new TextField(currentUser.getUsername());
        usernameField.setEditable(false);

        TextField bioField = new TextField(currentUser.getBio());
        bioField.setPromptText("Enter your bio");

        TextField profilePicUrlField = new TextField(currentUser.getProfilePictureUrl());
        profilePicUrlField.setPromptText("Profile Picture URL");

        Button saveButton = new Button("Save Changes");
        saveButton.setOnAction(e -> {
            currentUser.setBio(bioField.getText());
            currentUser.setProfilePictureUrl(profilePicUrlField.getText());
            Database.update(SocialMediaApp.getNetworking().getUsers());
            app.showAlert(Alert.AlertType.INFORMATION, "Profile Updated", "Your profile was updated successfully!");
            primaryStage.setScene(app.postsScene); // Assume getter for posts scene
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> primaryStage.setScene(app.postsScene)); // Assume getter for posts scene

        profileLayout.getChildren().addAll(
                new Label("Username"), usernameField,
                new Label("Bio"), bioField,
                new Label("Profile Picture URL"), profilePicUrlField,
                saveButton, cancelButton
        );

        Scene profileScene = new Scene(profileLayout, 300, 250);
        primaryStage.setScene(profileScene);
    }
}
