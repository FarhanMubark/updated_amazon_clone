package com.example.amazon_clone_system.Services;


import com.example.amazon_clone_system.Models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class UserService {

    ArrayList<User> users = new ArrayList<>();
    final private ProductService productService;
    final private MerchantService merchantService;
    final private UserService userService;
    final private MerchantStockService merchantStockService;

    public ArrayList<User> getUser(){
        return users;
        }
    public void addUsers(User user){
        users.add(user);
    }

    public Boolean updateUsers(int id, User user){
        for (int i =0;i<users.size();i++){
            if (users.get(i).getId()==id){
                users.set(i,user);
                return true;
            }
        }
        return false;
    }

    public User getUserId(int userId){
        for (int i=0; i<users.size();i++){
            User user = users.get(i);
            if (user.getId()==userId){
                return user;
            }
        }
        return null;
    }

    public int getUserBalance(int userId){
        for (int i =0; i<users.size();i++){
            User user = users.get(i);
            if (user.getId() == userId){
               int userBalance = user.getBalance();
               return userBalance;

            }
        }
        return 0;
    }

    public Boolean updateUserBalance(int userId, int remaingBalance){
        for (int i=0; i<users.size();i++){
            User user = users.get(i);
            if (user.getId()== userId){
                user.setBalance(remaingBalance);
                return true;
            }
        }
        return false;
    }


    public Boolean deleteUsers(int id){
        for (int i=0;i<users.size();i++){
            if (users.get(i).getId()==id){
                users.remove(i);
                return true;
            }
        }
        return false;
    }

    public Boolean checkUserId(int userId){
        for (User user:users){
            if (user.getId()==userId){
                return true;
            }
        }
        return false;
    }

    public Boolean checkAllIds(int userId, int productId, int merchId){
        boolean isUid = userService.checkUserId(userId);
        boolean isPid = productService.checkProductId(productId);
        boolean isMid = merchantService.checkMerchId(merchId);

        if (isUid && isPid && isMid){
            return true;
        }
        return false;
    }

    public Boolean CheckStock( int productId, int merchId){
        int merchanStock = merchantStockService.fetchStock(productId, merchId);
        if ( merchanStock<0){
            return false;
        }
        merchantStockService.reduceStock(productId, merchId);
        return true;
    }

    public Boolean checkUserBalance(int userId,int productId){
        int userBalance = userService.getUserBalance(userId);
        int productPrice = productService.getProductPrice(productId);

        if (userBalance < productPrice){
            return  false;
        }
        int remainingBalance = userBalance - productPrice;
        userService.updateUserBalance(userId, remainingBalance);
        return true;
    }





}
