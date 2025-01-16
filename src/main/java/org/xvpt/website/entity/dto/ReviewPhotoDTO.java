package org.xvpt.website.entity.dto;

import lombok.Getter;
import lombok.Setter;
import org.xvpt.website.entity.PhotoStatus;

@Getter
@Setter
public class ReviewPhotoDTO {
    private String photo;
    private PhotoStatus status;
}
