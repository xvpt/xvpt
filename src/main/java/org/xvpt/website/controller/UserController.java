package org.xvpt.website.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xvpt.website.entity.RestBean;
import org.xvpt.website.entity.dto.EditProfileDTO;
import org.xvpt.website.entity.vo.UserVO;
import org.xvpt.website.service.UserService;

import java.io.InputStream;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RestBean<UserVO>> getUser(Authentication authentication) {
        return ResponseEntity.ok(RestBean.success(userService.findUser(authentication)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RestBean<UserVO>> editProfile(Authentication authentication, @RequestBody EditProfileDTO dto) {
        return ResponseEntity.ok(RestBean.success(userService.editUser(authentication, dto)));
    }

    @PostMapping("avatar")
    public ResponseEntity<RestBean<UserVO>> uploadAvatar(Authentication authentication, @RequestParam("file") MultipartFile file) throws Exception {
        try {
            return ResponseEntity.ok(RestBean.success(userService.uploadAvatar(authentication, file)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(RestBean.failure(400, e.getMessage()));
        }
    }

    @GetMapping("avatar")
    public void getUserAvatar(@RequestParam(required = false) String id, Authentication authentication, HttpServletResponse response) throws Exception {
        if (id == null) {
            id = authentication.getName();
        }
        InputStream avatar = userService.getAvatar(id);
        // copy the input stream into response
        response.setContentType("image/webp");
        IOUtils.copy(avatar, response.getOutputStream());
    }

    @GetMapping("info")
    public ResponseEntity<RestBean<UserVO>> getUserInfo(@RequestParam String id) {
        return ResponseEntity.ok(RestBean.success(userService.userInfo(id)));
    }

}
