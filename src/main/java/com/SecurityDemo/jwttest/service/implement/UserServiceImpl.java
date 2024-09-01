package com.SecurityDemo.jwttest.service.implement;

import com.SecurityDemo.jwttest.repository.UserRepository;
import com.SecurityDemo.jwttest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepo;

//    public Optional<User> findByEmail(String email){
//        return userRepo.findByEmail(email);
//    }
}
