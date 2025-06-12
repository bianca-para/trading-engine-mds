// src/main/java/org/dev/server/service/impl/UserServiceImpl.java
package org.dev.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.dev.server.dto.user.UserRequestRegisterDto;
import org.dev.server.dto.user.UserResponseDto;
import org.dev.server.exception.EmailAlreadyExistsException;
import org.dev.server.exception.UsernameAlreadyExistsException;
import org.dev.server.mapper.UserMapper;
import org.dev.server.model.User;
import org.dev.server.repository.UserRepository;
import org.dev.server.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserResponseDto createUser(UserRequestRegisterDto userRequestRegisterDto) {
        // Hash the password
        String hashedPassword = bCryptPasswordEncoder.encode(userRequestRegisterDto.password());
        User user = UserMapper.toEntity(userRequestRegisterDto, hashedPassword);

        // Check for existing email/username
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "A user with this email already exists: " + user.getEmail());
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyExistsException(
                    "A user with this username already exists: " + user.getUsername());
        }

        // Save and return DTO
        User savedUser = userRepository.save(user);
        return UserMapper.toDto(savedUser);
    }

    @Override
    public UserResponseDto getUserById(UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User with id: " + id + " not found");
        }
        return UserMapper.toDto(userOptional.get());
    }

    @Override
    public UserResponseDto findByUsername(String username) {
        Optional<User> optUser = userRepository.findByUsername(username);
        if (optUser.isEmpty()) {
            throw new UsernameNotFoundException("User with username: " + username + " not found");
        }
        return UserMapper.toDto(optUser.get());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> opt = userRepository.findByUsername(username);
        if (opt.isEmpty()) {
            throw new UsernameNotFoundException("User with username: " + username + " not found");
        }

        User user = opt.get();
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (String role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
