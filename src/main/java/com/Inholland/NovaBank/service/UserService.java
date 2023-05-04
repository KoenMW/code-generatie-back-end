package com.Inholland.NovaBank.service;

import com.Inholland.NovaBank.model.User;
import com.Inholland.NovaBank.repositorie.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends BaseService{
    @Autowired
    private UserRepository userRepository;

    public User add(User user){

        return userRepository.save(user);
    }

    public User getById(long id){
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAll(){
        return (List<User>) userRepository.findAll();
    }

    public User update(User user){
        return userRepository.save(user);
    }
}
