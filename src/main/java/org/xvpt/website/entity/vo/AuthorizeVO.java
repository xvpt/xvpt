package org.xvpt.website.entity.vo;


import lombok.Data;

import java.util.List;

@Data
public class AuthorizeVO {
    private String username;
    private String token;
    private List<String> roles;
    private long expire;
}

