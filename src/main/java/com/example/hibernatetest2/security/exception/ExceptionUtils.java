package com.example.hibernatetest2.security.exception;

import java.io.OutputStream;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom Exception Handler that will send HTTP-Response about some Error back to user
 */
@Slf4j
public class ExceptionUtils {

    public static void processError(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Exception exception) {
        if (
            exception instanceof DisabledException ||
            exception instanceof LockedException ||
            exception instanceof BadCredentialsException //||
        //exception instanceof InvalidClaimException //? Works only for oatuth2
        ) {
            String httpResponse = getHttpResponse(response,
                                                  exception.getMessage(),
                                                  HttpStatus.BAD_REQUEST);
            writeResponse(response, httpResponse);
        }
        /* else if (exception instanceof TokenExpiredException) //? For oauth02
        {
            HttpResponse httpResponse = getHttpResponse (response, exception.getMessage (), UNAUTHORIZED);
            writeResponse (response, httpResponse);
        } */
        else {
            String httpResponse = getHttpResponse(response,
                                                  "An error occurred. Please try again.",
                                                  HttpStatus.INTERNAL_SERVER_ERROR
            );
            writeResponse(response, httpResponse);
        }
        log.error(exception.getMessage());
    }



    private static void writeResponse(HttpServletResponse response, String httpResponse) {
        OutputStream out;
        try {
            out = response.getOutputStream();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(out, httpResponse);
            out.flush();
        } catch (Exception exception) {
            log.error(exception.getMessage());
            exception.printStackTrace();
        }
    }



    private static String getHttpResponse(HttpServletResponse response, String message, HttpStatus httpStatus) {
        String httpResponse = response + message + httpStatus.value();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        return httpResponse;
    }

}