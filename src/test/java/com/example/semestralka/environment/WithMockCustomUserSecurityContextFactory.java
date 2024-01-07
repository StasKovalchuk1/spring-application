package com.example.semestralka.environment;

import com.example.semestralka.model.User;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

/**
 * Sets up security context according to the mock user annotation on test.
 */
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {

    @Override
    public SecurityContext createSecurityContext(WithCustomMockUser annotation) {
        final User user = new User();
        user.setId(annotation.id());
        user.setRole(annotation.role());
        user.setUsername(annotation.username());
        return Environment.setCurrentUser(user);
    }
}
