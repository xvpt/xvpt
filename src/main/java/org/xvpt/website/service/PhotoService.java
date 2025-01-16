package org.xvpt.website.service;

import org.springframework.security.core.Authentication;
import org.xvpt.website.entity.dto.JoinCompetitionDTO;
import org.xvpt.website.entity.vo.PhotoVO;

import java.io.IOException;
import java.io.InputStream;

public interface PhotoService {
    InputStream getFile(String photoId, boolean origin) throws IOException;

    PhotoVO joinCompetition(JoinCompetitionDTO dto, Authentication authentication) throws IOException;
}
