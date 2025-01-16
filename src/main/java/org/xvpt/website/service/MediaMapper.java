package org.xvpt.website.service;

import org.xvpt.website.entity.Media;
import org.xvpt.website.entity.vo.MediaVO;

public interface MediaMapper {

    MediaVO toMediaVO(Media media);
}
