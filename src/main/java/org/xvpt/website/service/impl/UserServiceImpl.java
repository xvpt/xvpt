package org.xvpt.website.service.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xvpt.website.XvptApplication;
import org.xvpt.website.entity.Media;
import org.xvpt.website.entity.User;
import org.xvpt.website.entity.dto.EditProfileDTO;
import org.xvpt.website.entity.vo.UserVO;
import org.xvpt.website.repository.UserRepository;
import org.xvpt.website.service.MediaService;
import org.xvpt.website.service.UserMapper;
import org.xvpt.website.service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final MediaService mediaService;

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
    public InputStream getAvatar(String userId) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException(userId));
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
}
