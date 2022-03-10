package com.example.crowdfunding.user;

import com.example.crowdfunding.interfaces.ServiceInterface;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.security.auth.login.CredentialNotFoundException;
import java.util.List;
import java.util.Map;

@Service
public class UserService implements ServiceInterface<User> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public String create(User user) {
        if ( emailExists(user.getEmail()) ) { return "Email already exists"; }

        String encodedPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.insert(user);
        return "User created successfully";
    }

    public ResponseEntity<Object> login(Map<String, String> emailAndPassword) throws CredentialNotFoundException {
        var email = emailAndPassword.get("email");
        var password = emailAndPassword.get("password");

        if (!emailExists(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email is not registered");
        }

        User user = userRepository.findByEmail(email);

        if ( passwordEncoder.matches(password, user.getPassword()) ){
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid password");
    }

    @Override
    public List<User> getAll() { return userRepository.findAll(); }

    public User getUserById(String userId) throws Exception {
        ObjectId userIdToObjectId = new ObjectId(userId);

        User user = userRepository.findById(userIdToObjectId);

        if (user == null) {
            throw new Exception("User does not exist");
        }

        return user;
    }
    @Override
    public String update(String userId, User newUserInfo) {
        ObjectId userIdToObjectId = new ObjectId(userId);

        User currentUserInfo = userRepository.findById(userIdToObjectId);

        if (currentUserInfo == null) {  return "user with id " + userId + " does not exists"; }

        currentUserInfo.setName(newUserInfo.getName());
        currentUserInfo.setUserName(newUserInfo.getUserName());
        currentUserInfo.setDob(newUserInfo.getDob());
        currentUserInfo.setEmail(newUserInfo.getEmail());
        currentUserInfo.setPassword(newUserInfo.getPassword());

        userRepository.save(currentUserInfo);
        return "User has been successfully updated";
    }

    @Override
    public String delete(String userId) {
        var userIdToObjectId = new ObjectId(userId);

        User user = userRepository.findById(userIdToObjectId);

        if (user == null){ return "user with id " + userId + " does not exists"; }

        userRepository.delete(user);
        return "Your account has been deleted";
    }

    private boolean emailExists(String email) {
        User user = userRepository.findByEmail(email);
        return user != null ? true : false;
    }
}
