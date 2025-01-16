package org.xvpt.website.entity.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PhotoVO {
    private String id;

    private String description;
    private String owner;
    private String competition;
}
