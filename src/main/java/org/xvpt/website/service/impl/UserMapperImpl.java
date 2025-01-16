package org.xvpt.website.service.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.xvpt.website.entity.User;
import org.xvpt.website.entity.vo.UserVO;
import org.xvpt.website.repository.UserRepository;
import org.xvpt.website.service.UserMapper;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {
    private final UserRepository userRepository;

    @Override
    public User fromTokenAttributes(@NotNull Map<String, Object> attributes) {
        User user = userRepository.findByEmail(attributes.get("email").toString()).orElseGet(User::new);

        if (attributes.containsKey("sub")) {
            user.setId(attributes.get("sub").toString());
        }

        if (attributes.containsKey("given_name")) {
            user.setFirstname(attributes.get("given_name").toString());
        } else if (attributes.containsKey("nickname")) {
            user.setFirstname(attributes.get("nickname").toString());
        }

        if (attributes.containsKey("family_name")) {
            user.setLastname(attributes.get("family_name").toString());
        }

        if (attributes.containsKey("email")) {
            user.setEmail(attributes.get("email").toString());
        }
        return user;
    }

    @Override
    public UserVO toUserVO(@NotNull User user) {
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setFirstName(user.getFirstname());
        userVO.setLastName(user.getLastname());
        userVO.setRoles(user.getRoles());
        if (user.getAvatar() != null) {
            userVO.setAvatar(user.getAvatar().getId());
        }
        return userVO;
    }
}
