package com.tm3200.TradeNow.Service;

import com.tm3200.TradeNow.Model.DTO.UserLoginDTO;
import com.tm3200.TradeNow.Model.DTO.UserRegistrationDTO;
import com.tm3200.TradeNow.Model.DTO.UserStatusDTO;
import com.tm3200.TradeNow.Model.DTO.UserUpdateDTO;
import com.tm3200.TradeNow.Model.User;
import com.tm3200.TradeNow.Model.Enum.UserType;
import com.tm3200.TradeNow.Repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserJpaRepository userRepository;

    public User register(UserRegistrationDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setGeographicZone(dto.getGeographicZone());
        user.setUserType(UserType.GENERAL);
        user.setActive(true);
        user.setCompletedTrades(0);
        user.setAverageRating(0.0);

        return userRepository.save(user);
    }

    public User login(UserLoginDTO dto) {
        Optional<User> optional = userRepository.findByEmail(dto.getEmail());
        if (!optional.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = optional.get();


        if (!user.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        if (!user.getActive()) {
            throw new RuntimeException("Account is disabled");
        }

        return user;
    }

    public User getProfile(Integer id) {
        Optional<User> optional = userRepository.findById(id);
        if (!optional.isPresent()) {
            throw new RuntimeException("User not found");
        }
        return optional.get();
    }

    public User updateProfile(Integer id, UserUpdateDTO dto) {
        Optional<User> optional = userRepository.findById(id);
        if (!optional.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = optional.get();

        user.setPhoto(dto.getPhoto());
        user.setDescription(dto.getDescription());
        user.setGeographicZone(dto.getGeographicZone());

        return userRepository.save(user);
    }


    public User getHistory(Integer id) {
        Optional<User> optional = userRepository.findById(id);
        if (!optional.isPresent()) {
            throw new RuntimeException("User not found");
        }
        return optional.get();
    }

    public User updateStatus(Integer id, UserStatusDTO dto) {
        Optional<User> optional = userRepository.findById(id);
        if (!optional.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = optional.get();

        user.setActive(dto.getActive());

        return userRepository.save(user);
    }



}
