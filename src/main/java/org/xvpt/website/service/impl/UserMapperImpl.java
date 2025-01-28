package org.xvpt.website.service.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.xvpt.website.entity.User;
import org.xvpt.website.entity.vo.UserVO;
import org.xvpt.website.service.UserMapper;

@Service
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {
    @Override
    public UserVO toUserVO(@NotNull User user) {
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setNickname(user.getNickname());
        userVO.setRoles(user.getRoles());
        if (user.getAvatar() != null) {
            userVO.setAvatar(user.getAvatar().getId());
        }
        return userVO;
    }
}
