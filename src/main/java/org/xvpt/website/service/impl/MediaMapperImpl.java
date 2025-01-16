package org.xvpt.website.service.impl;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.xvpt.website.entity.Media;
import org.xvpt.website.entity.vo.MediaVO;
import org.xvpt.website.service.MediaMapper;

@Service
public class MediaMapperImpl implements MediaMapper {
    @Override
    public MediaVO toMediaVO(@NotNull Media media) {
        MediaVO mediaVO = new MediaVO();
        mediaVO.setId(media.getId());
        return mediaVO;
    }
}
