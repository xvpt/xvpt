package org.xvpt.website.service;

import org.jetbrains.annotations.NotNull;
import org.xvpt.website.entity.User;
import org.xvpt.website.entity.vo.UserVO;

import java.util.Map;

public interface UserMapper {
    User fromTokenAttributes(Map<String, Object> attributes);

    UserVO toUserVO(@NotNull User user);
}
