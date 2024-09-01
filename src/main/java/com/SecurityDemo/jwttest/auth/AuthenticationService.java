package com.SecurityDemo.jwttest.auth;

import com.SecurityDemo.jwttest.config.JwtService;
import com.SecurityDemo.jwttest.email.EmailService;
import com.SecurityDemo.jwttest.email.EmailTemplateName;
import com.SecurityDemo.jwttest.entity.Role;
import com.SecurityDemo.jwttest.entity.Token;
import com.SecurityDemo.jwttest.entity.User;
import com.SecurityDemo.jwttest.exception.UserAlreadyExistsException;
import com.SecurityDemo.jwttest.repository.TokenRepository;
import com.SecurityDemo.jwttest.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final  UserRepository userRepository;
    private final EmailService emailService;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl ;


    public void register(RegisterRequest request) throws MessagingException {
        // Implementation to register user and generate JWT token
        var userCheck = repository.findByEmail(request.getEmail());
        if(userCheck.isPresent()){
            throw new UserAlreadyExistsException("User email already exists");
        }
        // 1- create the user object user entity Builder.
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
//                .role(request.getRole())
                .role(Role.USER)
                .accountLocked(false)
                .enabled(false)
                .build();

        // 2- save the user to the database
        var savedUser = repository.save(user);
        // 3- send account activation mail.
        sendValidationEmail(user);
    }

    /*depeand on auth manager bean which has method to authenticate the user based on the mail & pass*/

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // before authentication will throw error if the user mail or pass is not correct
//        var user = ((User) auth.getPrincipal()); // use the auth object to get the user
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Could not find the user"));

        // check if account activated
        if (!user.isEnabled()){
            throw new RuntimeException("Please activate your mail first");
        };

        // create return the created JWT token with the auth response builder
//        1- extra data (Claim to added to the token)
//        var claims = new HashMap<String, Object>();
//        claims.put("fullName", user.getFullName());
//        var jwtToken = jwtService.generateToken(claims, (User) auth.getPrincipal());

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    };


//    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                    // todo exception has to be defined
                    .orElseThrow(() -> new RuntimeException("Invalid token"));
            if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
                sendValidationEmail(savedToken.getUser());
                throw new RuntimeException("Activation token has expired. A new token has been send to the same email address");
            }

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }

    /*---------------- Validate email using sending activation token------------------*/

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }

    private String generateAndSaveActivationToken(User user) {
        // Generate a token
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);

        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }
}
