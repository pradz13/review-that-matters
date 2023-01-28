package com.proj.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String redirectURL = request.getContextPath();
        List<String> listOfAuthorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        if (listOfAuthorities.contains("ROLE_USER")) {
            redirectURL = redirectURL + "/user/dashboard";
        } else if (listOfAuthorities.contains("ROLE_ADMIN")) {
            redirectURL = redirectURL + "/admin/admin-dashboard";
        }
        response.sendRedirect(redirectURL);
    }
}
