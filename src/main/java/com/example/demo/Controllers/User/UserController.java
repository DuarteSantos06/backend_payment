package com.example.demo.Controllers.User;

import com.example.demo.Controllers.InputModels.UserInput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.Service.User.UserService;

@RestController()
@RequestMapping("/users")
public class UserController{
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<?> addUser(@RequestBody UserInput user){
        return ResponseEntity.ok(userService.createUser(user.email(),user.password()));
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization")String authHeader){
        if(authHeader==null||!authHeader.startsWith("Bearer ")){
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);

        if(userService.deleteUser(token)){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.status(401).body("Invalid token");
        }
    }
}
