package com.example.demo.Service.User;

import com.example.demo.Exceptions.UserAlreadyExists;
import com.example.demo.Exceptions.UserDoesNotExistException;
import com.example.demo.Repository.Entities.User;
import com.example.demo.Repository.User.UserRepository;
import com.example.demo.Service.JWT.JWTService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepo;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo, JWTService jwtService,PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }
    public String createUser(String email, String password)throws UserAlreadyExists {
        if(userRepo.findByEmail(email)!=null){
            throw new UserAlreadyExists("User Already Exists");
        }
        String hashedPassword = passwordEncoder.encode(password);

        User user = new User(email, hashedPassword);
        userRepo.save(user);

        return jwtService.generateToken(user.getId());
    }

    public boolean deleteUser(String token)throws UserDoesNotExistException {
        long userId=jwtService.getIdByToken(token);
        Optional<User> optionalUser = userRepo.findById(userId);

        if(optionalUser.isEmpty()){
            throw new UserDoesNotExistException("User doesn't exist");
        }
        User user=optionalUser.get();
        userRepo.delete(user);
        return true;
    }
}
