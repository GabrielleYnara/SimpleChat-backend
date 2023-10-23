package com.example.simplechatbackend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class JwtRequestFilter extends OncePerRequestFilter {

    Logger logger = Logger.getLogger(JwtRequestFilter.class.getName());

    private MyUserDetailsService myUserDetailsService;

    private JWTUtils jwtUtils;

    /**
     * Injects a MyUserDetailsService instance into JwtRequestFilter
     * @param myUserDetailsService Service used to get the current logged in user.
     */
    @Autowired
    public void setMyUserDetailsService(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    /**
     * Injects a JWTUtils instance into JwtRequestFilter
     * @param jwtUtils Utility class used to parse and validate JWTs.
     */
    @Autowired
    public void setJwtUtils(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    /**
     * Extracts the JWT from a Http request.
     * @param request The request sent by the user.
     * @return The raw JWT string.
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasLength(headerAuth) && headerAuth.startsWith("Bearer")) {
            return headerAuth.substring(7);
        } else {
            logger.info("No header");
            return null;
        }
    }

    /**
     * Parses and validates the JWT passed from a HTTP request. Then loads the current logged in
     * User's details into memory. Generates Authentication token so that User can use website
     * without needing to re-login.
     * @param request Http request from User.
     * @param response Http response to send back to User containing the generated JWT.
     * @param filterChain The custom properties with whitelisted URLS and enabled/disabled
     * browser security features we defined in SecurityConfiguration
     * @throws ServletException If something goes wrong with the server communication.
     * @throws IOException If the details sent by the User are invalid text or dangerous text.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = this.myUserDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            logger.info("Cannot set user authentication token");
        }
        filterChain.doFilter(request, response);
    }
}
