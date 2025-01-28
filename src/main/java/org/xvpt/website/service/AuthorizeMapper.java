package org.xvpt.website.service;

import org.jetbrains.annotations.NotNull;
import org.xvpt.website.entity.User;
import org.xvpt.website.entity.vo.AuthorizeVO;

public interface AuthorizeMapper {
    AuthorizeVO toAuthorizeVO(@NotNull User account, String token);
}
