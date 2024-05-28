package com.evanw.datebyrate.controller;

import com.evanw.datebyrate.Dto.AccountCreationResponseDto;
import com.evanw.datebyrate.Dto.LoginResponse;
import com.evanw.datebyrate.Dto.RegistrationDto;
import com.evanw.datebyrate.Dto.StatusDto;
import com.evanw.datebyrate.Dto.account.AccountCreationDto;
import com.evanw.datebyrate.Dto.refreshToken.RefreshTokenDto;
import com.evanw.datebyrate.Dto.user.UserCreationDto;
import com.evanw.datebyrate.Dto.user.UserLoginDto;
import com.evanw.datebyrate.config.CookieUtils;
import com.evanw.datebyrate.exception.RefreshTokenNotFoundException;
import com.evanw.datebyrate.exception.UserNotFoundException;
import com.evanw.datebyrate.models.AuthenticationResponse;
import com.evanw.datebyrate.models.RefreshToken;
import com.evanw.datebyrate.models.User;
import com.evanw.datebyrate.repository.UserRepository;
import com.evanw.datebyrate.service.AuthenticationService;
import com.evanw.datebyrate.service.JwtService;
import com.evanw.datebyrate.service.RefreshTokenService;
import com.evanw.datebyrate.service.UserDetailsImp;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.SessionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@RestController
public class AuthenticationController {

    private final AuthenticationService authService;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsImp userDetailsImp;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthenticationController(AuthenticationService authService,
                                    RefreshTokenService refreshTokenService,
                                    UserDetailsImp userDetailsImp,
                                    UserRepository userRepository,
                                    JwtService jwtService
    ) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.userDetailsImp = userDetailsImp;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    @ResponseStatus
    public ResponseEntity<RegistrationDto> register(
            @Valid @RequestBody UserCreationDto request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }
    @PostMapping("/create-account")
    @ResponseStatus(HttpStatus.OK)
    public AccountCreationResponseDto createAccount(@Valid @RequestBody AccountCreationDto acc) {
        return authService.createAccount(acc);
    }
    @PostMapping("/login")
    @ResponseStatus
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody UserLoginDto request,
            HttpServletResponse response
    ) {
        AuthenticationResponse authResponse = authService.authenticate(request);
        Cookie refreshTokenCookie = new Cookie("refreshToken", authResponse.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 365);
        refreshTokenCookie.setSecure(true);
        response.addCookie(refreshTokenCookie);

        CookieUtils.addCookieWithSameSite(response, refreshTokenCookie, "None");
        LoginResponse loginResponse = new LoginResponse(authResponse.getUserId(), authResponse.getAccessToken());

        return ResponseEntity.ok(loginResponse);
    }
    @PostMapping(value = "/refresh", produces = "application/json")
    @ResponseStatus
    public ResponseEntity<?> refreshToken(HttpServletRequest request
    ) {
        String refreshToken = getCookieValue(request, "refreshToken");
        if (refreshToken == null) {
            return ResponseEntity.badRequest().body("Refresh token is misisng");
        }

//        if (request.getCookies() != null) {
//            for (Cookie cookie : request.getCookies()) {
//                if ("refreshToken".equals(cookie.getName())) {
//                    refreshToken = cookie.getValue();
//                    break;
//                }
//            }
//        }

        if (refreshToken == null) {
            return ResponseEntity.badRequest().body("Refresh token is missing");
        }

        Optional<RefreshToken> refreshTokenObj = refreshTokenService.findByToken(refreshToken);
        if (refreshTokenObj.isEmpty() || refreshTokenService.isExpired(refreshTokenObj.get())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired refresh token");
        }

        String username = refreshTokenObj.get().getUser().getUsername();
        UserDetails userDetails = userDetailsImp.loadUserByUsername(username);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails,
                null,
                userDetails.getAuthorities()));
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("No user found");
        }

        String jwtToken = jwtService.generateToken(user.get());

        return ResponseEntity.ok(new AuthenticationResponse(user.get().getId(), jwtToken));
    }
    @GetMapping("/session-status")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<StatusDto> isSessionActive(HttpServletRequest request) {
        return authService.isSessionActive(request);
    }

    @PostMapping("/logout-user")
    @ResponseStatus(HttpStatus.OK)
    public void logOut(HttpServletRequest request){
        authService.logOut(request.getCookies());
    }

    private String getCookieValue(HttpServletRequest request, String cookieName){
        if (request.getCookies() != null){
            for (Cookie cookie : request.getCookies()){
                if (cookieName.equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
//        String refreshToken = null;
//        if (refreshToken == null) {
//            throw new SessionException("No active token");
//        }
//        Optional<RefreshToken> rToken = refreshTokenService.findByToken(refreshToken);
//        if (rToken.isEmpty()) {
//            throw new RuntimeException("No refresh token found");
//        }
//        User user = rToken.get().getUser();
//        String username = user.getUsername();
//        Integer userId = user.getId();
//        if (!refreshTokenService.isExpired(rToken.get())) {
//            StatusDto session = new StatusDto(userId,true, username);
//            return new ResponseEntity<>(session, HttpStatus.OK);
//        }
//        StatusDto sessionFalse = new StatusDto(userId, false, username);
//        return new ResponseEntity<>(sessionFalse, HttpStatus.UNAUTHORIZED);
//    }
