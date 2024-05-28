package com.evanw.datebyrate.service;

import com.evanw.datebyrate.Dto.AccountCreationResponseDto;
import com.evanw.datebyrate.Dto.RegistrationDto;
import com.evanw.datebyrate.Dto.StatusDto;
import com.evanw.datebyrate.Dto.account.AccountCreationDto;
import com.evanw.datebyrate.Dto.user.UserCreationDto;
import com.evanw.datebyrate.Dto.user.UserLoginDto;
import com.evanw.datebyrate.exception.*;
import com.evanw.datebyrate.models.AuthenticationResponse;
import com.evanw.datebyrate.models.Preferences;
import com.evanw.datebyrate.models.RefreshToken;
import com.evanw.datebyrate.models.User;
import com.evanw.datebyrate.repository.RefreshTokenRepository;
import com.evanw.datebyrate.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.locks.Lock;

@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final EmailService emailService;
    private final UserDetailsImp userDetailsImp;

    private final RefreshTokenRepository refreshTokenRepository;

    public AuthenticationService(UserRepository repository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 AuthenticationManager authenticationManager,
                                 RefreshTokenService refreshTokenService,
                                 RefreshTokenRepository refreshTokenRepository,
                                 EmailService emailService,
                                 UserDetailsImp userDetailsImp
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.emailService = emailService;
        this.userDetailsImp = userDetailsImp;
    }
    public RegistrationDto register(UserCreationDto request) {
        if (repository.existsByUsername(request.username())) {
            throw new UsernameTakenException("Username is taken. Try another.");
        }
        if (repository.existsByEmail(request.email())) {
            throw new EmailTakenException("This Email is already registered.");
        }
        String verificationToken = UUID.randomUUID().toString();
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmailVerified(true);
//        user.setVerificationToken(verificationToken);
        repository.save(user);
//        emailService.sendVerificationEmail(user.getEmail(), user.getVerificationToken());

        return new RegistrationDto(
                "Registration Successful"
        );
    }
    @Transactional
    @ResponseStatus
    public ResponseEntity<StatusDto> isSessionActive(HttpServletRequest request){
        String refreshTokenString = null;
        if (request.getCookies() == null){
            System.out.println("NO REFRESH TOKEN");
        }

        if (request.getCookies() != null){
            for (Cookie cookie : request.getCookies()){
                if ("refreshToken".equals(cookie.getName())){
                    refreshTokenString = cookie.getValue();
                }
            }
        }
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenString)
                .orElseThrow(() -> new RefreshTokenNotFoundException("No refresh token found"));
        User user = refreshToken.getUser();
        String username = user.getUsername();
        Integer id = user.getId();
        StatusDto session;
        if (!refreshTokenService.isExpired(refreshToken)){
            session = new StatusDto(id, true, username);
            return new ResponseEntity<>(session, HttpStatus.OK);
        }
        session = new StatusDto(id, false, username);
        return new ResponseEntity<>(session, HttpStatus.UNAUTHORIZED);
    }
    public AccountCreationResponseDto createAccount(AccountCreationDto acc) {
        LocalDate dob = LocalDate.parse(acc.dob(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int age = getAge(acc);
        ArrayList genderPref = setGenderPref(acc);
        User user = getUserByFromAuth();
        Preferences preferences = new Preferences();
        user.setFirstName(acc.firstName());
        user.setLastName(acc.lastName());
        user.setAge(age);
        user.setGender(acc.gender());
        user.setSexualOrientation(acc.sexualOrientation());
        user.setDob(dob);
        preferences.setUser(user);
        preferences.addGenderPref(genderPref);
        user.setPreferences(preferences);
        repository.save(user);

        return new AccountCreationResponseDto(
                "Account info successfully created"
        );
    }
    public AuthenticationResponse authenticate(UserLoginDto request) {
        User user = repository.findByUsername(request.username())
                .orElseThrow(() -> new UserNotFoundException("No user found with username: " + request.username()));
        handleUserLockout(user);
        authenticateWithSecurityManager(request, user);
        if (user.getLocation() == null || user.getLocation().getX() != request.location().longitude() || user.getLocation().getY() != request.location().latitude()) {
            createOrUpdateUserLocation(user, request);
        }
        String refreshToken = manageRefreshToken(user);
        String accessToken = jwtService.generateToken(user);
        repository.save(user);
        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setAccessToken(accessToken);
        authResponse.setUserId(user.getId());
        authResponse.setRefreshToken(refreshToken);
        return authResponse;
    }

    public void logOut(Cookie[] cookies){
        String refreshTokenValue = Arrays.stream(cookies)
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RefreshTokenNotFoundException("No refresh token found in cookies"));
        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenValue)
                .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token not found."));
        User user = refreshToken.getUser();
        if (user == null) {
            throw new UserNotFoundException("No user associated with this refresh token");
        }
        user.removeRefreshToken();
        repository.save(user);
        refreshTokenService.delete(user);
    }
    public String refreshTokenAndGetNewToken(String refreshToken) throws UserNotFoundException {
        RefreshToken refreshTokenObj = refreshTokenService.findByToken(refreshToken)
                .orElseThrow(() -> new InvalidTokenException("Invalid or expired refresh token"));

        if (refreshTokenService.isExpired(refreshTokenObj)) {
            throw new InvalidTokenException("Expired Refresh token");
        }

        User user = refreshTokenObj.getUser();
        UserDetails userDetails = userDetailsImp.loadUserByUsername(user.getUsername());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
        );
        return jwtService.generateToken(user);
    }
    private ArrayList setGenderPref(AccountCreationDto acc) {
        ArrayList genderPref = new ArrayList();
        if (acc.gender().equals("male")){
            if (acc.sexualOrientation().equals("straight")){
                genderPref.add("female");
            } else {
                genderPref.add("male");
            }
        } else if (acc.gender().equals("female")) {
            if (acc.sexualOrientation().equals("straight")){
                genderPref.add("male");
            } else {
                genderPref.add("female");
            }
        } else {
            genderPref.add("male");
            genderPref.add("female");
        }
        return genderPref;
    }
    private int getAge(AccountCreationDto acc){
        LocalDate dob = LocalDate.parse(acc.dob(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int age = Period.between(dob, LocalDate.now()).getYears();
        if (dob.isAfter(LocalDate.now())){
            throw new AgeException("Date of birth cannot be in the future");
        } else if (age < 18) {
            throw new AgeException("Must be 18 to create an account");
        }
        return age;
    }
    private String manageRefreshToken(User user){
        String refresh;
        if (user.getRefreshToken() == null || user.getRefreshToken().getExpirationDate().isBefore(Instant.now())) {
            if (user.getRefreshToken() != null) {
                refreshTokenRepository.delete(user.getRefreshToken());
            }
            refresh = refreshTokenService.createRefreshToken(user.getId());
            Optional<RefreshToken> rToken = refreshTokenRepository.findByToken(refresh);
            rToken.ifPresent(user::setRefreshToken);
        } else {
            refresh = user.getRefreshToken().getToken();
        }
        return refresh;
    }
    private User getUserByFromAuth(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return repository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("No user found with username: " + username));
    }
    private void createOrUpdateUserLocation(User user, UserLoginDto request){
        GeometryFactory geometryFactory = new GeometryFactory();
        Point location = geometryFactory.createPoint(
                new Coordinate(request.location().longitude(), request.location().latitude())
        );
        user.updateLocation(location);
    }
    private void handleUserLockout(User user){
        if (user.getLockOutTime() != null){
            long minutesLockedOut = Duration.between(user.getLockOutTime(), LocalDateTime.now()).toMinutes();
            if (minutesLockedOut < 5) {
                throw new LockOutException("Locked out for " + (5 - minutesLockedOut) + " more minutes");
            }
            resetLockout(user);
        }
    }
    private void resetLockout(User user){
        user.setLockOutTime(null);
        user.setFailedLoginAttempts(0);
    }
    private void authenticateWithSecurityManager(UserLoginDto request, User user){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
            user.setFailedLoginAttempts(0);
        } catch (AuthenticationException ex) {
            incrementLoginAttempts(user);
        }
    }
    private void incrementLoginAttempts(User user) {
        int attempts = user.getFailedLoginAttempts() + 1;
        user.setFailedLoginAttempts(attempts);
        if (attempts >= 4) {
            user.setLockOutTime(LocalDateTime.now());
            repository.save(user);
            throw  new BadCredentialsException("Authentication failed for user " + user.getUsername());
        }
    }
    //        if (user.getLockOutTime() != null) {
//            long minutesLockedOut = Duration.between(user.getLockOutTime(), LocalDateTime.now()).toMinutes();
//            if (minutesLockedOut < 5) {
//                throw new LockOutException("Locked out for " + (5 - minutesLockedOut) + " more minutes");
//            } else {
//                user.setLockOutTime(null);
//                user.setFailedLoginAttempts(0);
//                repository.save(user);
//            }
//        }
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            request.username(),
//                            request.password())
//            );
//            user.setFailedLoginAttempts(0);
//        } catch (AuthenticationException ex) {
//            int attempts = user.getFailedLoginAttempts() + 1;
//            user.setFailedLoginAttempts(attempts);
//
//            if (attempts >= 4) {
//                user.setLockOutTime(LocalDateTime.now());
//            }
//            repository.save(user);
//            throw new BadCredentialsException("Authentication failed for user " + request.username());
//        }
//        // LOCATION
//        createOrUpdateUserLocation(user, request);
//
//        String refresh;
//        if (user.getRefreshToken() == null || user.getRefreshToken().getExpirationDate().isBefore(Instant.now())) {
//            if (user.getRefreshToken() != null) {
//                refreshTokenRepository.delete(user.getRefreshToken());
//            }
//            refresh = refreshTokenService.createRefreshToken(user.getId());
//            Optional<RefreshToken> rToken = refreshTokenRepository.findByToken(refresh);
//            rToken.ifPresent(user::setRefreshToken);
//        } else {
//            refresh = user.getRefreshToken().getToken();
//        }
//
//        repository.save(user);
//        String username = user.getUsername();
//        String accessToken = jwtService.generateToken(user);
//
//        return new AuthenticationResponse(accessToken, refresh, username);
}
