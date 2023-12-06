package com.example.semestralka.security;

import com.example.semestralka.security.model.LoginStatus;
import com.example.semestralka.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class AuthenticationFailure implements AuthenticationFailureHandler {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationFailure.class);

    private final ObjectMapper mapper;

    public AuthenticationFailure(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        LOG.debug("Login failed for user {}.", request.getParameter(Constants.USERNAME_PARAM));
        final LoginStatus status = new LoginStatus(false, false, null, exception.getMessage());
        mapper.writeValue(response.getOutputStream(), status);
    }
}
