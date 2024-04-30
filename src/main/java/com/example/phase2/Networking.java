package com.example.phase2;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Networking {
        private Map<String, User> users = new HashMap<>();

        public Map<String, User> getUsers() {
            return users;
        }


        public boolean registerUser(String username, String bio, String profilePicUrl, String Password) {
            if (!users.containsKey(username)) {
                users.put(username, new User(username, bio, profilePicUrl, Password));
                return true;
            }
            return false; // User already exists
        }

        public User loginUser(String username, String Password) {
            User user = users.get(username);
            if (user != null && user.getPassword().equals(Password)) {
                return user;
            }
            throw new IllegalArgumentException("wrong username or password"); // Login failed
        }
        public User getUser(String username){
            if (!users.containsKey(username)) {
                throw new IllegalArgumentException("no user found");
            }
            return users.get(username);
        }


        public Set<String> showUsers(){
           return users.keySet();
        }

    }

    