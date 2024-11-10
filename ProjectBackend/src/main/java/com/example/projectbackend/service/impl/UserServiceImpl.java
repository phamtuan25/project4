package com.example.projectbackend.service.impl;

import com.example.projectbackend.bean.request.PasswordRequest;
import com.example.projectbackend.bean.request.UserRequest;
import com.example.projectbackend.bean.response.UserResponse;
import com.example.projectbackend.entity.User;
import com.example.projectbackend.exception.ExistException;
import com.example.projectbackend.exception.InvalidException;
import com.example.projectbackend.exception.NotFoundException;
import com.example.projectbackend.mapper.UserMapper;
import com.example.projectbackend.repository.UserRepository;
import com.example.projectbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserResponse> getAllUsers(@PageableDefault(size = 10, page = 0) Pageable pageable,
                                          @RequestParam(required = false) String keyword) {
        Specification<User> spec = searchByKeyword(keyword);
        Page<User> userPage = userRepository.findAll(spec, pageable);

        List<UserResponse> userResponses = userPage.getContent().stream()
                .map(UserMapper::convertToResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(userResponses, pageable, userPage.getTotalElements());
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
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new ExistException("email","Email is exist");
        }
        User user = UserMapper.convertFromRequest(userRequest);
        user.setUpdatedAt(LocalDateTime.now());
        String encodedPassword = passwordEncoder.encode(userRequest.getPassword());
        user.setPassword(encodedPassword);
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
    public UserResponse changePassword(Long userId, PasswordRequest passwordRequest) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new NotFoundException("UserNotFound", "User not found");
        }
        if (!passwordEncoder.matches(passwordRequest.getCurrentPassword(), user.getPassword())) {
            throw new InvalidException("CurrentPasswordInvalid", "Current password is incorrect");
        }
        if (passwordEncoder.matches(passwordRequest.getNewPassword(), user.getPassword())) {
            throw new InvalidException("NewPasswordSameAsCurrent", "The new password must not be the same as the current password");
        }
        if (!passwordRequest.getConfirmPassword().equals(passwordRequest.getNewPassword())) {
            throw new InvalidException("ConfirmPasswordInvalid", "Confirmation password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
        userRepository.save(user);

        return UserMapper.convertToResponse(user);
    }

    @Override
    public User getUserName(String email) {
       User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("email", "Your Email not found"));
       return user;
    }

    public void setUser(User userUpdate, User userInput) {
        userUpdate.setPhoneNumber(userInput.getPhoneNumber());
        userUpdate.setAddress(userInput.getAddress());
        userUpdate.setEmail(userInput.getEmail());
        userUpdate.setRole(userInput.getRole());
        userUpdate.setUpdatedAt(LocalDateTime.now());
    }

    public static Specification<User> searchByKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + keyword + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName").as(String.class)), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email").as(String.class)), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("phoneNumber").as(String.class)), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("address").as(String.class)), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("role").as(String.class)), likePattern)
            );
        };
    }
}
