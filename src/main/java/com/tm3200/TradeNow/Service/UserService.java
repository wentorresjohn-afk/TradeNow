package com.tm3200.TradeNow.Service;

import com.tm3200.TradeNow.Model.DTO.UserLoginDTO;
import com.tm3200.TradeNow.Model.DTO.UserRegistrationDTO;
import com.tm3200.TradeNow.Model.DTO.UserStatusDTO;
import com.tm3200.TradeNow.Model.DTO.UserUpdateDTO;
import com.tm3200.TradeNow.Model.Trade;
import com.tm3200.TradeNow.Model.User;
import com.tm3200.TradeNow.Model.Enum.TradeStatus;
import com.tm3200.TradeNow.Model.Enum.UserType;
import com.tm3200.TradeNow.Repository.TradeJpaRepository;
import com.tm3200.TradeNow.Repository.UserJpaRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserJpaRepository userRepository;

    @Autowired
    TradeJpaRepository tradeRepository;

    public User register(UserRegistrationDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));
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


        if (!BCrypt.checkpw(dto.getPassword(), user.getPassword())) {
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


    public List<Trade> getHistory(Integer id) {
        Optional<User> optional = userRepository.findById(id);
        if (!optional.isPresent()) {
            throw new RuntimeException("User not found");
        }

        List<Trade> asUser1 = tradeRepository.findByUser1_IdAndStatus(id, TradeStatus.COMPLETED);
        List<Trade> asUser2 = tradeRepository.findByUser2_IdAndStatus(id, TradeStatus.COMPLETED);

        List<Trade> history = new ArrayList<>();
        history.addAll(asUser1);
        history.addAll(asUser2);

        return history;
    }

    public User updateStatus(Integer id, UserStatusDTO dto, Integer adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (admin.getUserType() != UserType.ADMINISTRATOR) {
            throw new RuntimeException("Only administrators can change account status");
        }

        Optional<User> optional = userRepository.findById(id);
        if (!optional.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = optional.get();

        user.setActive(dto.getActive());

        return userRepository.save(user);
    }

    public void deleteUser(Integer id, Integer adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (admin.getUserType() != UserType.ADMINISTRATOR) {
            throw new RuntimeException("Only administrators can delete accounts");
        }

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

}



