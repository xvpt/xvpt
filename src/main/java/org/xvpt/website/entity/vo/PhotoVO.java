package org.xvpt.website.entity.vo;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhotoVO {
    private String id;

    private String description;
    private String owner;
    private String competition;
    private int votes;
    private boolean isVoted;
}
