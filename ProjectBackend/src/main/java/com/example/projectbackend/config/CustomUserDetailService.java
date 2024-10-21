package com.example.projectbackend.config;

import com.example.projectbackend.entity.User;
import com.example.projectbackend.exception.NotFoundException;
import com.example.projectbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("email", "Your Email not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(mapRolesToAuthorities(user))
                .build();
    }
    private Collection<GrantedAuthority> mapRolesToAuthorities(User user){
    List<String> roles = User.getRolesAsList();
    return roles.stream().map(role ->
            new SimpleGrantedAuthority("ROLE_"+role.getClass().getName()))
            .collect(Collectors.toList());
    }
}
