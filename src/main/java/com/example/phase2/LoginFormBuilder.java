package com.example.phase2;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginFormBuilder {
    static VBox build(Stage primaryStage, SocialMediaApp app) {
        VBox loginForm = new VBox(10);
        loginForm.setPadding(new Insets(20));
        loginForm.setAlignment(Pos.CENTER);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> app.loginUser(usernameField.getText(), passwordField.getText(), primaryStage));

        Button switchToSignUp = new Button("Sign Up");
        switchToSignUp.setOnAction(e -> switchToSignUp(primaryStage, app));

        loginForm.getChildren().addAll(new Label("Username:"), usernameField,
                new Label("Password:"), passwordField, loginButton, switchToSignUp);

        return loginForm;
    }

    private static void switchToSignUp(Stage primaryStage, SocialMediaApp app) {
        VBox signUpForm = SignUpFormBuilder.build(primaryStage, app);
        Scene signUpScene = new Scene(signUpForm, 400, 300);
        primaryStage.setScene(signUpScene);
    }
}

