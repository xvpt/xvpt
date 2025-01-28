package org.xvpt.website.service.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.xvpt.website.entity.User;
import org.xvpt.website.entity.vo.AuthorizeVO;
import org.xvpt.website.service.AuthorizeMapper;
import org.xvpt.website.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class AuthorizeMapperImpl implements AuthorizeMapper {
    private final JwtUtil jwtUtil;

    @Override
    public AuthorizeVO toAuthorizeVO(@NotNull User account, String token) {
        AuthorizeVO vo = new AuthorizeVO();
        vo.setUsername(account.getUsername());
        vo.setRoles(account.getRoles());
        vo.setExpire(jwtUtil.getExpireDate().getTime());
        vo.setToken(token);
        return vo;
    }
}

