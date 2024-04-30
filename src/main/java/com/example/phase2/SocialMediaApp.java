package com.example.phase2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SocialMediaApp extends Application {
    private Networking networking = new Networking(); // Networking instance

    private GridPane createLoginForm(Stage primaryStage, VBox mainLayout) {
        GridPane loginForm = new GridPane();
        loginForm.setVgap(10);
        loginForm.setHgap(10);

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Button switchToSignUp = new Button("Sign Up");

        loginButton.setOnAction(e -> {
            try {
                User user = networking.loginUser(usernameField.getText(), passwordField.getText());
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Login Successful! Welcome, " + user.getUsername());
                alert.show();
            } catch (IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
                alert.show();
            }
        });

        switchToSignUp.setOnAction(e -> mainLayout.getChildren().setAll(createSignUpForm(primaryStage, mainLayout)));

        loginForm.addRow(0, new Label("Username:"), usernameField);
        loginForm.addRow(1, new Label("Password:"), passwordField);
        loginForm.addRow(2, loginButton, switchToSignUp);

        return loginForm;
    }

    private GridPane createSignUpForm(Stage primaryStage, VBox mainLayout) {
        GridPane signUpForm = new GridPane();
        signUpForm.setVgap(10);
        signUpForm.setHgap(10);

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        TextField bioField = new TextField();
        TextField profilePicUrlField = new TextField();
        Button signUpButton = new Button("Sign Up");
        Button switchToLogin = new Button("Login");

        signUpButton.setOnAction(e -> {
            try {
                System.out.println("11111111111111111111");
                boolean registered = networking.registerUser(usernameField.getText(), bioField.getText(), profilePicUrlField.getText(), passwordField.getText());
                System.out.println("11111111111111111111");
                if (registered) {
                    System.out.println("22222222222222222222");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Registration Successful!");
                    alert.show();
                } else {
                    System.out.println("33333333333333333333");
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Registration Failed: User already exists.");
                    alert.show();
                }
            }
            catch (IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
                alert.show();
            }
        });

        switchToLogin.setOnAction(e -> mainLayout.getChildren().setAll(createLoginForm(primaryStage, mainLayout)));

        signUpForm.addRow(0, new Label("Username:"), usernameField);
        signUpForm.addRow(1, new Label("Password:"), passwordField);
        signUpForm.addRow(2, new Label("Bio:"), bioField);
        signUpForm.addRow(3, new Label("Profile Picture URL:"), profilePicUrlField);
        signUpForm.addRow(4, signUpButton, switchToLogin);

        return signUpForm;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Social Media Platform");
        VBox mainLayout = new VBox();
        mainLayout.getChildren().add(createLoginForm(primaryStage, mainLayout));
        primaryStage.setScene(new Scene(mainLayout, 350, 250));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
