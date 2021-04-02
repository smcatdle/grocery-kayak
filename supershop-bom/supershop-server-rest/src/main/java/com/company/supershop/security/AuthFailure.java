package com.company.supershop.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;


@Component
public class AuthFailure extends SimpleUrlAuthenticationFailureHandler {

	private final Logger logger = Logger.getLogger(AuthFailure.class.getName());

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    	logger.log(Level.SEVERE, "Unauthorized Access");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
