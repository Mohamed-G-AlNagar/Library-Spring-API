package com.SecurityDemo.jwttest.config;

import com.SecurityDemo.jwttest.entity.User;
import com.SecurityDemo.jwttest.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor // used as constructor for any internal attributes like jwtService
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService; // internal interface have method to get by username
//    private UserService userService;

    @Override
    protected void doFilterInternal(
           @NonNull HttpServletRequest request,
           @NonNull HttpServletResponse response,
           @NonNull FilterChain filterChain)
            throws ServletException, IOException {

                if (request.getServletPath().contains("/auth")) {
                    filterChain.doFilter(request, response);
                    return;
                }
                final String authHeader = request.getHeader("Authorization");

                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    filterChain.doFilter(request, response);
                    return;
                };
//                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"); // used if need to restrict and send error

                String token = authHeader.substring(7); // The token starts with "Bearer "

//                extract the email from the token using the jwtService
                final String email = jwtService.extractUserName(token);
                final String jwt = authHeader.substring(7);

//                Check if the user email is exist & not already authenticated (signed in) to proceed with get user by email
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null){

//                    get the user by email (java called it username because it the primary)
//                    used the UserDetailsService interface method to get the user
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
//                    check if the token is valid using the token and userData
//                    if the token is valid --> set the Auth field incise the token as visited and verified -< setAuthentication
                    if (jwtService.isTokenValid(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
        filterChain.doFilter(request, response);


    }
}
