package com.example.phase2;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SignUpFormBuilder {
    static VBox build(Stage primaryStage, SocialMediaApp app) {
        VBox signUpForm = new VBox(10);
        signUpForm.setPadding(new Insets(20));
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
        signUpButton.setOnAction(e -> app.registerUser(
                usernameField.getText(), bioField.getText(), profilePicUrlField.getText(),
                passwordField.getText(), primaryStage));

        Button switchToLogin = new Button("Login");
        switchToLogin.setOnAction(e -> app.setupLoginScene(primaryStage));

        signUpForm.getChildren().addAll(new Label("Username:"), usernameField,
                new Label("Password:"), passwordField, new Label("Bio:"), bioField,
                new Label("Profile Picture URL:"), profilePicUrlField, signUpButton, switchToLogin);

        return signUpForm;
    }
}

