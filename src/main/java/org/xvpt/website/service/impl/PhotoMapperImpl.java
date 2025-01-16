package org.xvpt.website.service.impl;

import org.springframework.stereotype.Service;
import org.xvpt.website.entity.Photo;
import org.xvpt.website.entity.vo.PhotoVO;
import org.xvpt.website.service.PhotoMapper;

@Service
public class PhotoMapperImpl implements PhotoMapper {
    @Override
    public PhotoVO toPhotoVO(Photo photo) {
        return PhotoVO.builder()
                .id(photo.getId())
                .description(photo.getDescription())
                .owner(photo.getOwner().getId())
                .competition(photo.getCompetition().getId())
                .build();
    }
}
