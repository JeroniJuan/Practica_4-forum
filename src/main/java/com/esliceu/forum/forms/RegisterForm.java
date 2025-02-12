package com.esliceu.forum.forms;

public record RegisterForm(String email, String name, String password,
                           String role, String moderateCategory) {
}
