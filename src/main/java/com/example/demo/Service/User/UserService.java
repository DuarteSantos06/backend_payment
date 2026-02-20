package com.example.demo.Service.User;

import com.example.demo.Exceptions.UserDoesNotExistException;
import com.example.demo.Exceptions.UserDoesNotExistException;
import com.example.demo.Repository.Entities.User;
import com.example.demo.Repository.User.UserRepository;
import com.example.demo.Service.JWT.JWTService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepo;
    private final JWTService jwtService;

    public UserService(UserRepository userRepo, JWTService jwtService) {
        this.userRepo = userRepo;
        this.jwtService = jwtService;
    }
    public String createUser(String email, String password){
        if(userRepo.findByEmail(email)!=null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User Already Exists");
        }
        User user = new User(email,password);
        userRepo.save(user);
        long id= user.getId();
        return jwtService.generateToken(id);
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
