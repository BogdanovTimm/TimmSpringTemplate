package com.example.hibernatetest2.security.filter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.hibernatetest2.security.exception.ExceptionUtils;
import com.example.hibernatetest2.security.jwttoken.CustomJWTTokenHandler;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom filter, that allows or denies HTTP-Requests for user, based on his permissions
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {// This means that this filter will be called only once for each HTTP_Request

    private static final String TOKEN_PREFIX = "Bearer ";

    /**
     * A list of URLs without [**] (or they will not work)
     */
    private static final String[] PUBLIC_ROUTES = {"/api/v1/user/new/password",
                                                   "/api/v1/user/login",
                                                   "/api/v1/user/verify/code",
                                                   "/api/v1/user/register",
                                                   "/api/v1/user/refresh/token",
                                                   "/api/v1/user/image",
                                                   "/login",
                                                   "/login/oauth2/code/google",
                                                   "/oauth2/authorization/google",
                                                   "/api/v1/manytomany",
                                                   // #
                                                   "/#/api/v1/user/new/password",
                                                   "/#/api/v1/user/login",
                                                   "/#/api/v1/user/verify/code",
                                                   "/#/api/v1/user/register",
                                                   "/#/api/v1/user/refresh/token",
                                                   "/#/api/v1/user/image",
                                                   "/#",
                                                   "/#/login",
                                                   "/#/register"
    };

    private static final String HTTP_OPTIONS_METHOD = "OPTIONS";

    private final CustomJWTTokenHandler customJWTTokenHandler;



    /**
     * Checks every HTTP-Request:
     * <ol>
     * <li> Calls this.getToken () to get JWT-Access Token from given HTTP-Reques
     * <li> Class this.getUserId () to get id of a User that has sent current HTTP-Request
     * <li> Asks TokenProvider to check whether user's id exist
     * AND JWT-Access Token has not expired
     * <li> Asks TokenProvider to return permissions from given JWT-Access Token
     * <li> Asks TokenProvider to return UsernamePasswordAuthenticationToken
     * <li> Asks SecurityContextHolder to authenticate user
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filter) throws ServletException, IOException {
        try {
            String token = getToken(request);
            Long userId = getUserId(request);
            if (customJWTTokenHandler.isTokenValid(userId, token)) {
                List<GrantedAuthority> authorities = customJWTTokenHandler.getAuthorities(token);
                Authentication authentication = customJWTTokenHandler.getAuthentication(userId, authorities, request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                SecurityContextHolder.clearContext();
            }
            filter.doFilter(request, response);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            ExceptionUtils.processError(request, response, exception);
        }
    }

    /**
     * Checker for every HTTP-Request: whether HTTP-Request needs to be checked further by this CustomAuthorizaitonFilter
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getHeader(HttpHeaders.AUTHORIZATION) == null ||
               !request.getHeader(HttpHeaders.AUTHORIZATION).startsWith(TOKEN_PREFIX) ||
               request.getMethod().equalsIgnoreCase(HTTP_OPTIONS_METHOD) ||
               java.util.Arrays.asList(PUBLIC_ROUTES).contains(request.getRequestURI());
    }

    /**
     * Returns User's id:
     * <ol>
     * <li> Calls this.getToken () to get JWT-Access Token
     * <li> Asks Token Provider to get user's id from given JWT-Access Token
     * <li> Returns User's id
     */
    private Long getUserId(HttpServletRequest request) {
        return customJWTTokenHandler.getSubject(getToken(request), request);
    }

    /**
     * Get JWT-Access Token from Header of HTTP-Request if it exists:
     * <ol>
     * <li> Gets value from header named Authorizaiton
     * <li> Cheks whether value of Authorization header starts with given TOKEN_PREFIX (Bearer in our case)
     * <li> Deletes TOKEN_PREFIX from value (we need only value, without prefix), so, we have only JWT-Access token
     * <li> Returns a JWT-Access Token as a String
     */
    private String getToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                       .filter(header -> header.startsWith(TOKEN_PREFIX))
                       .map(token -> token.replace(TOKEN_PREFIX, StringUtils.EMPTY))
                       .get();
    }
}