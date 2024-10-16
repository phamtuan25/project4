package com.example.projectbackend.service.impl;

import com.example.projectbackend.bean.request.UserRequest;
import com.example.projectbackend.bean.response.BookingResponse;
import com.example.projectbackend.bean.response.UserResponse;
import com.example.projectbackend.entity.Booking;
import com.example.projectbackend.entity.User;
import com.example.projectbackend.exception.EmptyListException;
import com.example.projectbackend.exception.NotFoundException;
import com.example.projectbackend.mapper.BookingMapper;
import com.example.projectbackend.mapper.UserMapper;
import com.example.projectbackend.repository.BookingRepository;
import com.example.projectbackend.repository.UserRepository;
import com.example.projectbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

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
            throw new NotFoundException("BookNotFound","Not found Booking with " + userId);
        }
        return UserMapper.convertToResponse(userRepository.findById(userId).orElse(null));
    }

    @Override
    public User createUser(UserRequest userRequest) {
        User user = UserMapper.convertFromRequest(userRequest);
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
    public void deleteUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new NotFoundException("UserNotFound","User with ID " + userId + " not found.");
        }
        userRepository.deleteById(userId);
    }
    public void setUser(User userUpdate, User userInput) {
        userUpdate.setFirstName(userInput.getFirstName());
        userUpdate.setLastName(userInput.getLastName());
        userUpdate.setEmail(userInput.getEmail());
        userUpdate.setPhoneNumber(userInput.getPhoneNumber());
        userUpdate.setAddress(userInput.getAddress());
        userUpdate.setPassword(userInput.getPassword());
    }
}
