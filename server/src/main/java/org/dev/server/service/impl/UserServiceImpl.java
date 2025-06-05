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
        //parola hash-uita
        String hashedPassword = (bCryptPasswordEncoder.encode(userRequestRegisterDto.password()));
        //mapper pt conversie dto -> entity
        User user = UserMapper.toEntity(userRequestRegisterDto, hashedPassword);

        //verificam ca username si email sa fie unice
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "A user with this email already exists " + user.getEmail());
        }
        if(userRepository.existsByUsername(user.getUsername())){
            throw new UsernameAlreadyExistsException(
                    "A user with this username already exists " + user.getUsername());
        }
        //totul ok, salvam user in bd
        User savedUser = userRepository.save(user);

        //mapper pt a returna tuilizatorul (fara parola)
        return UserMapper.toDto(savedUser);
    }
    //pt spring security sa genereze tokenul, nu trb modificat
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> opt = userRepository.findByUsername(username);

        org.springframework.security.core.userdetails.User springUser = null;

        if (opt.isEmpty()) {
            throw new UsernameNotFoundException("User with username: " + username + " not found");
        } else {
            User user = opt.get();    //retrieving user from DB
            Set<String> roles = user.getRoles();
            Set<GrantedAuthority> ga = new HashSet<>();
            for (String role : roles) {
                ga.add(new SimpleGrantedAuthority(role));
            }

            springUser = new org.springframework.security.core.userdetails.User(
                    username,
                    user.getPassword(),
                    ga);
        }

        return springUser;
    }
    @Override
    public UserResponseDto getUserById(UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User with id: " + id + " not found");
        }
        return UserMapper.toDto(userOptional.get());
    }
}
