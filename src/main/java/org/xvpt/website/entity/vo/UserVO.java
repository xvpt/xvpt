package org.xvpt.website.entity.vo;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
    private String id;
    private String nickname;
    private String bio;
    private String avatar;
    private List<String> roles;
}
