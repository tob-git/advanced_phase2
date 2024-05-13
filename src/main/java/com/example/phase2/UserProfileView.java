package com.example.phase2;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;
public class UserProfileView {

    public static void showUserProfile(Stage primaryStage, User currentUser, SocialMediaApp app) {
        VBox profileLayout = new VBox(10);
        profileLayout.setPadding(new Insets(10));
        profileLayout.setAlignment(Pos.CENTER);

        // Creating an ImageView for the profile picture
        ImageView profileImageView = new ImageView();
        String imageUrl = currentUser.getProfilePictureUrl();
        InputStream stream = null;
        try{
         stream = new FileInputStream(imageUrl);
            }
        catch (IOException x){

            try{
                 stream = new FileInputStream("resources/l60Hf.png");
            }
            catch (IOException e){

                System.out.println("ERROR IN FINDING IMAGES");

            }
        }
        Image img = new Image(stream);
        profileImageView.setImage(img);

        profileImageView.setFitHeight(100); // Set the height of the image
        profileImageView.setFitWidth(100); // Set the width of the image
        profileImageView.setPreserveRatio(true);

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
                profileImageView,
                new Label("Username"), usernameField,
                new Label("Bio"), bioField,
                new Label("Profile Picture URL"), profilePicUrlField,
                saveButton, cancelButton
        );

        Scene profileScene = new Scene(profileLayout, 300, 250);
        primaryStage.setScene(profileScene);
    }
}
