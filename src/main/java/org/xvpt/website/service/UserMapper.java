package org.xvpt.website.service;

import org.jetbrains.annotations.NotNull;
import org.xvpt.website.entity.User;
import org.xvpt.website.entity.vo.UserVO;

public interface UserMapper {

    UserVO toUserVO(@NotNull User user);
}
