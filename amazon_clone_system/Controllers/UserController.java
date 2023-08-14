package com.example.amazon_clone_system.Controllers;

import com.example.amazon_clone_system.ApiResponse.ApiResponse;
import com.example.amazon_clone_system.Models.User;
import com.example.amazon_clone_system.Services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    final private UserService userService;

    @GetMapping("/get")
    public ResponseEntity getUsers(){
        ArrayList<User> users = userService.getUser();
        return ResponseEntity.status(200).body(users);
    }

    @PostMapping("/add")
    public ResponseEntity addUsers(@RequestBody @Valid User user, Errors errors){
        if(errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(message);
        }
        userService.addUsers(user);
        return ResponseEntity.status(200).body(new ApiResponse("User Added !!"));
    }

    @PutMapping("/update-users/{id}")
    public ResponseEntity updateUsers(@PathVariable int id, @RequestBody @Valid User user,Errors errors){
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        boolean isUpdated = userService.updateUsers(id,user);
        if (isUpdated) {
            return ResponseEntity.status(200).body(new ApiResponse("User updated !"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("User not fount"));
    }

    @DeleteMapping("/remove-user/{id}")
    public ResponseEntity deleteUsers(@PathVariable int id){
        boolean isDeleted = userService.deleteUsers(id);
        if (isDeleted){
            return ResponseEntity.status(200).body(new ApiResponse("User deleted !"));
        }
        return ResponseEntity.status(400).body(new ApiResponse("User Id Not found !"));
    }


    @PutMapping("/buy-product/{userId}/{productId}/{merchId}")
    public ResponseEntity buyProduct(@PathVariable int userId,@PathVariable int productId, @PathVariable int merchId) {

        boolean checkIds = userService.checkAllIds(userId, productId, merchId);
        boolean checkStock = userService.CheckStock(productId, merchId);
        boolean checkUserBalance = userService.checkUserBalance(userId, productId);
        if (!checkIds){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("UnValid user or product or merchant Id "));
        }

        if (!checkStock){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("out of stock"));
        }

        if (!checkUserBalance){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Not enough balance !"));
        }

        return ResponseEntity.status(200).body(new ApiResponse("Product purchased successfully"));

    }

    // Extra endpoint
    @PutMapping("/check-user/{userId}")
    public ResponseEntity addToCart(@PathVariable int userId){
        boolean isUserId = userService.checkUserId(userId);

        if(isUserId){
            return ResponseEntity.status(200).body(new ApiResponse("User is verfied"));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("user not verified !"));

    }

}




