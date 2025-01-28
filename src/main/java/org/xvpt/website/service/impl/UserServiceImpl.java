package org.xvpt.website.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xvpt.website.XvptApplication;
import org.xvpt.website.entity.Media;
import org.xvpt.website.entity.User;
import org.xvpt.website.entity.dto.EditProfileDTO;
import org.xvpt.website.entity.dto.RegisterDTO;
import org.xvpt.website.entity.vo.UserVO;
import org.xvpt.website.repository.UserRepository;
import org.xvpt.website.service.MediaService;
import org.xvpt.website.service.UserMapper;
import org.xvpt.website.service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final MediaService mediaService;
    private final PasswordEncoder passwordEncoder;

    @Value("${xvpt.account.username}")
    private String username;

    @Value("${xvpt.account.password}")
    private String password;

    @PostConstruct
    private void init() {
        if (userRepository.count() == 0) {
            log.info("Registering the default admin account...");
            this.register(username, password, true);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(user.getPassword())
                .roles(user.getRoles().toArray(String[]::new))
                .build();
    }

    @Override
    public UserVO findUser(@NotNull Authentication authentication) {
        User user = userRepository.findById(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(authentication.getName()));
        return userMapper.toUserVO(user);
    }

    @Override
    public UserVO editUser(@NotNull Authentication authentication, @NotNull EditProfileDTO dto) {
        User user = userRepository.findById(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(authentication.getName()));
        user.setBio(dto.getBio());
        User saved = userRepository.save(user);
        return userMapper.toUserVO(saved);
    }

    @Override
    public UserVO uploadAvatar(@NotNull Authentication authentication, @NotNull MultipartFile file) throws IOException {
        if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
            throw new IllegalArgumentException("Invalid image type: " + file.getContentType());
        }
        Media media = mediaService.compressImage(file);
        User user = userRepository.findById(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException(authentication.getName()));
        user.setAvatar(media);
        User saved = userRepository.save(user);
        return userMapper.toUserVO(saved);
    }

    @Override
    public UserVO userInfo(String id) {
        return userMapper.toUserVO(userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(id)));
    }

    @Override
    public InputStream getAvatar(@Nullable User user) throws IOException {
        if (user == null) {
            return null;
        }
        // get avatar
        Media avatar = user.getAvatar();
        InputStream inputStream = null;
        if (avatar != null) {
            inputStream = mediaService.getFile(avatar.getId());
        }
        if (inputStream == null) {
            // return the default avatar
            return XvptApplication.class.getResourceAsStream("/default-avatar.webp");
        }
        return inputStream;
    }

    @Override
    public InputStream getAvatar(String id) throws IOException {
        return this.getAvatar(userRepository.findById(id).orElse(null));
    }

    private @NotNull UserVO register(String username, String password, boolean admin) {
        // find exists
        Optional<User> existAccount = userRepository.findByUsername(username);
        if (existAccount.isPresent()) {
            throw new IllegalArgumentException("Account already exists");
        }
        // check username
        if (!isValidUsername(username)) {
            throw new IllegalArgumentException("Invalid username");
        }
        User account = new User();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password));

        if (admin) {
            account.setRoles(List.of("USER", "ADMIN"));
        } else {
            account.setRoles(List.of("USER"));
        }
        log.info("Account {} was registered", account.getUsername());
        return userMapper
                .toUserVO(userRepository.save(account));
    }

    @Override
    public UserVO updatePassword(@NotNull User account, String newPassword) {
        account.setPassword(passwordEncoder.encode(newPassword));
        return userMapper
                .toUserVO(userRepository.save(account));
    }

    @Override
    public User findUser(@NotNull UserDetails user) {
        return userRepository.findByUsername(user.getUsername()).orElse(null);
    }

    @Override
    public UserVO register(@NotNull RegisterDTO dto) {
        return this.register(dto.getUsername(), dto.getPassword(), false);
    }

    @Override
    public boolean assertPassword(@NotNull User account, String password) {
        return passwordEncoder.matches(password, account.getPassword());
    }

    private boolean isValidUsername(String username) {
        if (username == null || username.length() < 5) {
            return false;
        }
        return username.matches("^[a-zA-Z0-9_-]+$");
    }
}
