package org.xvpt.website.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;
import org.xvpt.website.entity.User;
import org.xvpt.website.entity.dto.EditProfileDTO;
import org.xvpt.website.entity.dto.RegisterDTO;
import org.xvpt.website.entity.vo.UserVO;

import java.io.IOException;
import java.io.InputStream;

public interface UserService extends UserDetailsService {
    UserVO findUser(Authentication authentication);

    UserVO editUser(Authentication authentication, EditProfileDTO dto);

    UserVO uploadAvatar(Authentication authentication, MultipartFile file) throws IOException;

    UserVO userInfo(String id);

    InputStream getAvatar(User user) throws IOException;

    InputStream getAvatar(String id) throws IOException;

    boolean assertPassword(User user, String password);

    UserVO updatePassword(User user, String password);

    User findUser(UserDetails user);

    UserVO register(RegisterDTO dto);

}
