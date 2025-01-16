package org.xvpt.website.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import org.xvpt.website.entity.dto.EditProfileDTO;
import org.xvpt.website.entity.vo.UserVO;

import java.io.IOException;
import java.io.InputStream;

public interface UserService {
    UserVO findUser(Authentication authentication);

    UserVO editUser(Authentication authentication, EditProfileDTO dto);

    UserVO uploadAvatar(Authentication authentication, MultipartFile file) throws IOException;

    UserVO userInfo(String id);

    InputStream getAvatar(String userId) throws IOException;
}
