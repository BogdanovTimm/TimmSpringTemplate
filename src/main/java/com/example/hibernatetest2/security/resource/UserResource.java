package com.example.hibernatetest2.security.resource;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.hibernatetest2.security.dto.LoginForm;
import com.example.hibernatetest2.security.dto.NewPasswordForm;
import com.example.hibernatetest2.security.dto.SettingsForm;
import com.example.hibernatetest2.security.dto.UpdateForm;
import com.example.hibernatetest2.security.dto.UpdatePasswordForm;
import com.example.hibernatetest2.security.dto.UserDTO;
import com.example.hibernatetest2.security.entities.User;
import com.example.hibernatetest2.security.entities.UserPlusItsRoles;
import com.example.hibernatetest2.security.entities.VerificationType;
import com.example.hibernatetest2.security.event.EventService;
import com.example.hibernatetest2.security.event.EventType;
import com.example.hibernatetest2.security.event.NewUserEvent;
import com.example.hibernatetest2.security.exception.ExceptionUtils;
import com.example.hibernatetest2.security.jwttoken.CustomJWTTokenHandler;
import com.example.hibernatetest2.security.mapper.UserMapper;
import com.example.hibernatetest2.security.service.RoleService;
import com.example.hibernatetest2.security.service.UserService;
import com.example.hibernatetest2.security.util.UserUtils;

import java.net.URI;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

// import static io.getarrays.securecapita.utils.UserUtils.getLoggedInUser;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

/**
 * REST Controller for all web pages under website.com/user URLs
 */
@RestController
@RequestMapping(path = "/api/v1/user")
@RequiredArgsConstructor
public class UserResource {

    private static final String TOKEN_PREFIX = "Bearer ";

    private final UserService userService;

    private final RoleService roleService;

    private final EventService eventService;

    private final AuthenticationManager authenticationManager;

    private final CustomJWTTokenHandler tokenProvider;

    private final HttpServletRequest request;

    private final HttpServletResponse response;

    /**
     * Class that creates a new NewUserEvent in such a way that NewUserEventListener will know about it
     */
    private final ApplicationEventPublisher publisher;

    /**
     * Handles POST-HTTP-Request for signing the user in, that is sent to website.com/login:
     * <ol>
     * <li>Checks given user's email and password by calling this.authenticate () function and returns UserDTO if all good
     * <li>Checks whether user uses 2 factor authentication:
     * <ul>
     * <li>if user uses it - calls this.sendVerificationCode to send verification code via SMS to user's phone
     * <li>if user not uses it - calls this.sendResponse () to return user his JWT access and refresh tokens as JSON HTTP-Response
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
                                                     @RequestBody
                                                     @Valid
                                                     LoginForm loginForm) {
        UserDTO user = authenticate(loginForm.getEmail(),
                                    loginForm.getPassword());
        return user.isUsingMfa() ?// 
                sendVerificationCode(user) ://
                ResponseEntity.ok()
                             .body(Map.of("user", // Returns JSON representation of given [UserDto] (it is not needed in real appolications)
                                          user,
                                          "access_token",
                                          tokenProvider.createAccessToken(getUserPrincipal(user)),
                                          "refresh_token",
                                          tokenProvider.createRefreshToken(getUserPrincipal(user))));
    }



    /**
     * Handles POST-HTTP-Request for registering user, that is sent to website.com/register and returns a HTTP-Response:
     * <ol>
     * <li> Maps given JSON variables and their values to User java class
     * <li>Asks UserService to add given User to the database
     * <li>Asks ResponseEntity to create and return JSON variables and their values back to user as HTTP-Response
     */
    @PostMapping("/register")
    public ResponseEntity<String> saveUser(
                                           @RequestBody
                                           @Valid
                                           User user) throws InterruptedException {
        UserDTO userDto = userService.createUser(user);
        return ResponseEntity.created(getUri())
                             .body(String.format("User account created for user %s", user.getFirstName()));
    }



    /**
     * Handles GET-HTTP-Request that asks for user's account information, that is sent to website.com/profile by taking user's information from authentication and returns a HTTP-Response:
     * <ol>
     * <li>CustomAuthorizationFilter filter checks that user has the right properties
     * <li>Takes user's email from authentication information
     * <li>Asks UserService to get User from the database
     * <li>Asks ResponseEntity to create and return JSON variables and their values back to user as HTTP-Response
     */
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> profile(Authentication authentication) {
        UserDTO user = UserUtils.getAuthenticatedUser(authentication);
        return ResponseEntity.ok()
                             .body(Map.of("user",
                                          user,
                                          "events",
                                          eventService.getEventsByUserId(user.getId()),
                                          "roles",
                                          roleService.getRoles()));
    }

    @PatchMapping("/update")
    public ResponseEntity<Map<String, Object>> updateUser(
                                                          @RequestBody
                                                          @Valid
                                                          UpdateForm user) {
        UserDTO updatedUser = userService.updateUserDetails(user);
        publisher.publishEvent(new NewUserEvent(updatedUser.getEmail(), EventType.PROFILE_UPDATE));
        return ResponseEntity.ok()
                             .body(
                                   Map.of("user",
                                          user,
                                          "events",
                                          eventService.getEventsByUserId(user.getId()),
                                          "roles",
                                          roleService.getRoles()));
    }

    //? START - To reset password when user is not logged in
    /**
     * Handles GET-HTTP-Request for authenticate user if he has enabled 2 factor authentication,
     * that is sent to website.com/verify/code/%user_email%/%generated_code% and returns a HTTP-Response:
     * <ol>
     * <li>Maps %user_email% from URL to [email] variable
     * <li>Maps %generated_code% from URL to [code] variable
     * <li>Asks UserService to verify given verifying code by checking [TwoFactorVerifications] tables in the database
     * <li>Asks ResponseEntity to create and return JSON variables and their values back to user as HTTP-Response. 2 of this variables will contain JWT Acess Token and JWT Refresh Token
     */
    @GetMapping("/verify/code/{email}/{code}")
    public ResponseEntity<Map<String, Object>> verifyCode(
                                                          @PathVariable("email")
                                                          String email,
                                                          @PathVariable("code")
                                                          String code) {
        UserDTO user = userService.verifyCode(email, code);
        publisher.publishEvent(new NewUserEvent(user.getEmail(), EventType.LOGIN_ATTEMPT_SUCCESS));
        return ResponseEntity.ok()
                             .body(Map.of("user",
                                          user,
                                          "access_token",
                                          tokenProvider.createAccessToken(getUserPrincipal(user)),
                                          "refresh_token",
                                          tokenProvider.createRefreshToken(getUserPrincipal(user))
                             ));
    }

    /**
     * Handles GET-HTTP-Request when somebody unauthorized asks to change password for some user based on a given email:
     * <ol>
     * <li>Asks a UserService to:
     * <ol>
     * <li>Set expiration time for URL for changing a user's password
     * <li>Generate a URL for changing the password
     * <li>Deletes previous row that represents URL for changing a password from [resetpasswordverifications] table for given user
     * <li>Create a new row that represents URL for changing a password in [resetpasswordverifications] table for given user
     * <li>Send an email with a URL to reset user's password to user's email
     * </ol>
     * <li>Asks ResponseEntity to create and return JSON variables and their values back to user as HTTP-Response
     */
    @GetMapping("/resetpassword/{email}")
    public ResponseEntity<String> resetPassword(@PathVariable("email")
    String email) {
        userService.resetPassword(email);
        return ResponseEntity.ok()
                             .body("Email sent. Please check your email to reset your password.");
    }

    /**
     * Handles GET-HTTP-Request for URL that verifies account after registration:
     * <ol>
     * <li>Asks UserService to:
     * <ol>
     * <li>Change row in [user] table by set [enabled] column to true
     * <li>Return updated UserDTO with its roles
     * </ol>
     * <li>Asks ResponseEntity to create and return JSON variables and their values back to user as HTTP-Response
     */
    @GetMapping("/verify/account/{key}")
    public ResponseEntity<String> verifyAccount(
                                                @PathVariable("key")
                                                String key) throws InterruptedException {
        return ResponseEntity.ok()
                             .body(userService.verifyAccountKey(key).isEnabled() ? "Account already verified" : "Account verified");
    }

    /**
     * Handles GET-HTTP-Request when somebody try to go to URL that changes a password for some user:
     * <ol>
     * <li>Asks UserService to:
     * <ol>
     * <li>Checks whether URL for changing a password was expired
     * <li>Return User
     * <li>Asks MapUserDTO to return UserDTO based on given User
     * </ol>
     * <li>Asks ResponseEntity to create and return JSON variables and their values back to user as HTTP-Response
     */
    @GetMapping("/verify/password/{key}")
    public ResponseEntity<String> verifyPasswordUrl(
                                                    @PathVariable("key")
                                                    String key) throws InterruptedException {
        UserDTO user = userService.verifyPasswordKey(key);
        return ResponseEntity.ok()
                             .body("Please enter a new password");
    }

    /**
     * Asks UserRepository to:
     * <ol>
     * <li>Asks UserService to:
     * <ol>
     * <li>Check that confirm password equals to original one
     * <li>Change a row in [users] table by changing [password] column value
     * </ol>
     * <li>Asks ResponseEntity to create and return JSON variables and their values back to user as HTTP-Response
     */
    @PutMapping("/new/password")
    public ResponseEntity<String> resetPasswordWithKey(
                                                       @RequestBody
                                                       @Valid
                                                       NewPasswordForm form) throws InterruptedException {
        userService.updatePassword(form.getUserId(), form.getPassword(), form.getConfirmPassword());
        return ResponseEntity.ok()
                             .body("Password reset successfully");
    }
    //? END - To reset password when user is not logged in

    /**
     * <ol>
     * <li>Asks UserUtils to receive UserDTO of current logged user
     * <li>Asks UserServcie to:
     * <ol>
     * <li>Check that new password and duplicate of it is equal
     * <li>Check that old password, provided by user, is equal to password received from database
     * <li>Change row in the [users] table by saving new password to database
     * </ol>
     * <li>Asks ResponseEntity to create and return JSON variables and their values back to user/frontend server as HTTP-Response
     */
    @PatchMapping("/update/password")
    public ResponseEntity<Map<String, Object>> updatePassword(Authentication authentication,
                                                              @RequestBody @Valid
                                                              UpdatePasswordForm form) {
        UserDTO userDTO = UserUtils.getAuthenticatedUser(authentication);
        userService.updatePassword(userDTO.getId(),
                                   form.getCurrentPassword(),
                                   form.getNewPassword(),
                                   form.getConfirmNewPassword());
        publisher.publishEvent(new NewUserEvent(userDTO.getEmail(),
                                                EventType.PASSWORD_UPDATE));
        return ResponseEntity.ok()
                             .body(Map.of("user",
                                          userService.getUserById(userDTO.getId()),
                                          "events",
                                          eventService.getEventsByUserId(userDTO.getId()),
                                          "roles",
                                          roleService.getRoles()));
    }

    @PatchMapping("/update/role/{roleName}")
    public ResponseEntity<Map<String, Object>> updateUserRole(Authentication authentication,
                                                              @PathVariable("roleName")
                                                              String roleName) {
        UserDTO userDTO = UserUtils.getAuthenticatedUser(authentication);
        userService.updateUserRole(userDTO.getId(), roleName);
        publisher.publishEvent(new NewUserEvent(userDTO.getEmail(), EventType.ROLE_UPDATE));
        return ResponseEntity.ok()
                             .body(Map.of("user",
                                          userService.getUserById(userDTO.getId()),
                                          "events",
                                          eventService.getEventsByUserId(userDTO.getId()),
                                          "roles",
                                          roleService.getRoles()));
    }

    /**
     * Updating whether user's account is enabled and non-locked.
     */
    @PatchMapping("/update/settings")
    public ResponseEntity<Map<String, Object>> updateAccountSettings(Authentication authentication,
                                                                     @RequestBody @Valid
                                                                     SettingsForm form) {
        UserDTO userDTO = UserUtils.getAuthenticatedUser(authentication);
        userService.updateAccountSettings(userDTO.getId(), form.getEnabled(), form.getNotLocked());
        publisher.publishEvent(new NewUserEvent(userDTO.getEmail(), EventType.ACCOUNT_SETTINGS_UPDATE));
        return ResponseEntity.ok()
                             .body(Map.of("user",
                                          userService.getUserById(userDTO.getId()),
                                          "events",
                                          eventService.getEventsByUserId(userDTO.getId()),
                                          "roles",
                                          roleService.getRoles()));
    }

    /**
     * Handles enabling 2-Factor authorization by user
     */
    @PatchMapping("/togglemfa")
    public ResponseEntity<Map<String, Object>> toggleMfa(Authentication authentication) throws InterruptedException {
        // TimeUnit.SECONDS.sleep (3);
        UserDTO user = userService.toggleMfa(UserUtils.getAuthenticatedUser(authentication).getEmail());
        publisher.publishEvent(new NewUserEvent(user.getEmail(), EventType.MFA_UPDATE));
        return ResponseEntity.ok()
                             .body(Map.of("user",
                                          user,
                                          "events",
                                          eventService.getEventsByUserId(user.getId()),
                                          "roles",
                                          roleService.getRoles()));
    }

    @PatchMapping("/update/image")
    public ResponseEntity<Map<String, Object>> updateProfileImage(Authentication authentication,
                                                                  @RequestParam("image")
                                                                  MultipartFile image) throws InterruptedException {
        UserDTO user = UserUtils.getAuthenticatedUser(authentication);
        userService.updateImage(user, image);
        publisher.publishEvent(new NewUserEvent(user.getEmail(), EventType.PROFILE_PICTURE_UPDATE));
        return ResponseEntity.ok()
                             .body(Map.of("user",
                                          userService.getUserById(user.getId()),
                                          "events",
                                          eventService.getEventsByUserId(user.getId()),
                                          "roles",
                                          roleService.getRoles()));
    }

    @GetMapping(value = "/image/{fileName}",
                produces = IMAGE_PNG_VALUE)
    public byte[] getProfileImage(
                                  @PathVariable("fileName")
                                  String fileName) throws Exception {
        return Files.readAllBytes(Paths.get(System.getProperty("user.home") + "/Downloads/images/" + fileName));
    }

    /**
     * Handles GET-HTTP-Request for requestin a new JWT-Access Token using JWT-Refresh Token:
     * <ol>
     * <li>Runs this.isHeaderAndTokenValid () to:
     * <ol>
     * <li>Check that there is an Authrorization header in HTTP-Request
     * <li>Check that value od the Authorization header starts with given token prefix ("Bearer " in our case)
     * <li>Check that given userId is not null
     * <li>Checks that token is not expired
     * </li>
     * </ol>
     * </li>
     * <li>Deletes given token prefix from the value from the Authorization header
     * <li>Asks UserService to:
     * <ol>
     * <li>Get an existed row from [users] table using given user's id and return created User back
     * <li>Returns UserDTO java class instance
     * </li>
     * </ol>
     * </li>
     * <li>Asks ResponseEntity to create and return JSON variables and their values back to user as HTTP-Response. 2 of this variables will contain JWT Acess Token and JWT Refresh Token
     */
    @GetMapping("/refresh/token")
    public ResponseEntity<Map<String, Object>> refreshToken(HttpServletRequest request) {
        if (isHeaderAndTokenValid(request)) {
            String token = request.getHeader(AUTHORIZATION)
                                  .substring(TOKEN_PREFIX.length()); // Deletes "Bearer "
            UserDTO user = userService.getUserById(tokenProvider.getSubject(token, request));
            return ResponseEntity.ok()
                                 .body(Map.of("user",
                                              user,
                                              "access_token",
                                              tokenProvider.createAccessToken(getUserPrincipal(user)),
                                              "refresh_token",
                                              token));
        } else {
            return ResponseEntity.badRequest()
                                 .body(Map.of("Message", "Refresh Token missing or invalid"));
        }
    }

    /**
     * Checks that JWT-Refresh Token is valid:
     * <ol>
     * <li>Checks that there is an Authrorization header in HTTP-Request
     * <li>Checks that value od the Authorization header starts with given token prefix ("Bearer " in our case)
     * <li>Asks TokenProvider to:
     * <ol>
     * <li>Check that given userId is not null
     * <li>Checks that token is not expired
     * </ol>
     * </li>
     * <li>Returns true if all above is true
     */
    private boolean isHeaderAndTokenValid(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION) != null &&
               request.getHeader(AUTHORIZATION).startsWith(TOKEN_PREFIX) &&
               tokenProvider.isTokenValid(tokenProvider.getSubject(request.getHeader(AUTHORIZATION)
                                                                          .substring(TOKEN_PREFIX.length()),
                                                                   request),
                                          request.getHeader(AUTHORIZATION)
                                                 .substring(TOKEN_PREFIX.length())
               );
    }

    /**
     * Handles all HTTP-Requests that are sent to a page on a wepsite that does not exist and returns a HTTP-Response:
     * <ol>
     * <li>Asks ResponseEntity to create and return JSON variables and their values back to user as HTTP-Response
     */
    @RequestMapping("/error")
    public ResponseEntity<String> handleError(HttpServletRequest request) {
        return ResponseEntity.badRequest()
                             .body("There is no mapping for a " + request.getMethod() + " request for this path on the server");
    }

    /*
    @RequestMapping("/error")
    public ResponseEntity<HttpResponse> handleError(HttpServletRequest request) {
    return new ResponseEntity<>(HttpResponse.builder()
            .timeStamp(now().toString())
            .reason("There is no mapping for a " + request.getMethod() + " request for this path on the server")
            .status(NOT_FOUND)
            .statusCode(NOT_FOUND.value())
            .build(), NOT_FOUND);
    }
    */

    /**
     * Checks given user's email and password and returns UserDTO if all good:
     * <ol>
     * <li>Safes login attempt
     * <li>Asks Authentication Manager to call UserRepostiory.loadByUsername () to create AuthenticationToken (not to be confused with JWT-Access and JWT-Refresh tokens)
     * <li>Returns UserDTO with its Role from AuthenticationToken
     */
    private UserDTO authenticate(String email, String password) {
        UserDTO userDTO = userService.getUserByEmail(email); // It is needed only to safe login attempt
        try {
            if (null != userDTO) {
                publisher.publishEvent(new NewUserEvent(email, EventType.LOGIN_ATTEMPT));
            }
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password)); // Calls UserRepositoryImpl.loadUserByUsername()
            UserDTO userDTOFPlusItsRolesFomAuthenticationToken = ((UserPlusItsRoles)authentication.getPrincipal()).getUserDTOPlusItsRole();
            if (!userDTOFPlusItsRolesFomAuthenticationToken.isUsingMfa()) {
                publisher.publishEvent(new NewUserEvent(email, EventType.LOGIN_ATTEMPT_SUCCESS));
            }
            return userDTOFPlusItsRolesFomAuthenticationToken;
        } catch (Exception exception) {
            if (null != userDTO) {
                publisher.publishEvent(new NewUserEvent(email, EventType.LOGIN_ATTEMPT_FAILURE));
            }
            ExceptionUtils.processError(request, response, exception);
            throw new RuntimeException(exception.getMessage());
        }
    }

    private URI getUri() {
        return URI.create(fromCurrentContextPath().path("/api/v1/user/get/<userId>")
                                                  .toUriString());
    }


    /**
     * Creates UserPrincipal java class instance
     */
    private UserPlusItsRoles getUserPrincipal(UserDTO user) {
        return new UserPlusItsRoles(UserMapper.toUser(userService.getUserByEmail(user.getEmail())),
                                    roleService.getRoleByUserId(user.getId())
        );
    }

    /**
     * Sends verification code to user via SMS
     * <ol>
     * <li>Asks UserService to send verifictaion code to user via SMS
     * <li>Returns an HTTP-Response as JSON code back to user
     */
    private ResponseEntity<Map<String, Object>> sendVerificationCode(UserDTO user) {
        userService.sendVerificationCode(user);
        return ResponseEntity.ok()
                             .body(Map.of("user", user, "message", "Verification Code Sent")); // Returns JSON representation of given [UserDto]
    }
}
