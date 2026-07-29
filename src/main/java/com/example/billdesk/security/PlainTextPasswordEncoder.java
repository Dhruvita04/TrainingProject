package com.example.billdesk.security;

import org.springframework.security.crypto.password.PasswordEncoder;

// LEARNING ONLY. Compares passwords as plain strings - no hashing, no salt.
// Spring Security's AuthenticationManager requires *some* PasswordEncoder to
// function, so this exists to satisfy that contract without hashing anything.
// Do not use this in any app that stores real user data.
public class PlainTextPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.toString().equals(encodedPassword);
    }
}