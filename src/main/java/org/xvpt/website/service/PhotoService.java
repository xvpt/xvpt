package org.xvpt.website.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.xvpt.website.entity.dto.DeletePhotoDTO;
import org.xvpt.website.entity.dto.EnterCompetitionDTO;
import org.xvpt.website.entity.dto.ReviewPhotoDTO;
import org.xvpt.website.entity.vo.PhotoVO;

import java.io.IOException;
import java.io.InputStream;

public interface PhotoService {
    InputStream getFile(String photoId, boolean origin) throws IOException;

    PhotoVO enterCompetition(EnterCompetitionDTO dto, Authentication authentication) throws IOException;

    void deletePhoto(DeletePhotoDTO dto, Authentication authentication);

    PhotoVO vote(String photoId, Authentication authentication);

    PhotoVO cancelVote(String photoId, Authentication authentication);

    Page<PhotoVO> listPhotos(PageRequest of, String competitionId, Authentication authentication);

    PhotoVO review(ReviewPhotoDTO dto);

    Page<PhotoVO> listPending(PageRequest page);

    Page<PhotoVO> listRejected(PageRequest page);
}
