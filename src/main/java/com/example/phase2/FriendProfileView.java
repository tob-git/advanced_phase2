package com.example.phase2;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;

public class FriendProfileView {

    public static void showUserProfile(Stage primaryStage, User Friend, SocialMediaApp app) {
        VBox profileLayout = new VBox(10);
        profileLayout.setPadding(new Insets(10));
        profileLayout.setAlignment(Pos.CENTER);

        // Creating an ImageView for the profile picture
        ImageView profileImageView = new ImageView();
        String imageUrl = Friend.getProfilePictureUrl();
        InputStream stream = null;
        try {
            stream = new FileInputStream(imageUrl);
        } catch (IOException x) {
            try {
                stream = new FileInputStream("resources/l60Hf.png");
            } catch (IOException e) {
                System.out.println("ERROR IN FINDING IMAGES");
            }
        }
        Image img = new Image(stream);
        profileImageView.setImage(img);

        profileImageView.setFitHeight(100); // Set the height of the image
        profileImageView.setFitWidth(100); // Set the width of the image
        profileImageView.setPreserveRatio(true);
        Button Return = new Button("Return");
        Return.setOnAction(e -> primaryStage.setScene(app.postsScene));
        Label usernameLabel = new Label("Username: " + Friend.getUsername());
        Label bioLabel = new Label("Bio: " + Friend.getBio());

        profileLayout.getChildren().addAll(
                profileImageView,
                usernameLabel,
                bioLabel,
                Return
        );

        Scene profileScene = new Scene(profileLayout, 300, 250);
        primaryStage.setScene(profileScene);
    }
}
