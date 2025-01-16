package org.xvpt.website.service;

import org.xvpt.website.entity.Photo;
import org.xvpt.website.entity.vo.PhotoVO;

public interface PhotoMapper {
    PhotoVO toPhotoVO(Photo photo);
}
