package com.example.projectbackend.service.impl;

import com.example.projectbackend.bean.request.LoginRequest;
import com.example.projectbackend.bean.request.UserRequest;
import com.example.projectbackend.bean.response.UserResponse;
import com.example.projectbackend.entity.User;
import com.example.projectbackend.exception.EmptyListException;
import com.example.projectbackend.exception.InvalidException;
import com.example.projectbackend.exception.NotFoundException;
import com.example.projectbackend.mapper.UserMapper;
import com.example.projectbackend.repository.UserRepository;
import com.example.projectbackend.service.UserService;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Argon2 argon2 = Argon2Factory.create();

    @Override
    public List<UserResponse> getAllUsers() {
        if(userRepository.findAll().isEmpty()) {
            throw new EmptyListException("EmptyUser","This list User is empty");
        }
        return userRepository.findAll().stream().map(UserMapper::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public UserResponse getDetailUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(Objects.isNull(user)){
            throw new NotFoundException("UserNotFound","Not found User with " + userId);
        }
        return UserMapper.convertToResponse(userRepository.findById(userId).orElse(null));
    }

    @Override
    public User createUser(UserRequest userRequest) {
        User user = UserMapper.convertFromRequest(userRequest);
        user.setUpdatedAt(LocalDateTime.now());
        user.setPassword(argon2.hash(10, 65536, 1, userRequest.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long userId, User user) {
        User userUpdate = userRepository.findById(userId).orElse(null);
        if(userUpdate != null){
            setUser(userUpdate, user);
        }
        return userRepository.save(userUpdate);
    }

    @Override
    public UserResponse Login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if(user == null) {
            throw new NotFoundException("UserNotFound","Not found User with email " + loginRequest.getEmail());
        }
        if(!argon2.verify(loginRequest.getPassword(),user.getPassword())){
            throw new InvalidException("IncorrectPassword", "Password is incorrect");
        }else{
            return UserMapper.convertToResponse(user);
        }
    }

    public void setUser(User userUpdate, User userInput) {
        userUpdate.setFirstName(userInput.getFirstName());
        userUpdate.setLastName(userInput.getLastName());
        userUpdate.setEmail(userInput.getEmail());
        userUpdate.setPhoneNumber(userInput.getPhoneNumber());
        userUpdate.setAddress(userInput.getAddress());
        userUpdate.setPassword(userInput.getPassword());
        userUpdate.setUpdatedAt(LocalDateTime.now());
    }
}
