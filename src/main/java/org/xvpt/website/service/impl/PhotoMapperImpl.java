package org.xvpt.website.service.impl;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.xvpt.website.entity.Photo;
import org.xvpt.website.entity.User;
import org.xvpt.website.entity.vo.PhotoVO;
import org.xvpt.website.repository.UserRepository;
import org.xvpt.website.repository.VoteRepository;
import org.xvpt.website.service.PhotoMapper;

@Service
@RequiredArgsConstructor
public class PhotoMapperImpl implements PhotoMapper {
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;

    @Override
    public PhotoVO toPhotoVO(@NotNull Photo photo, @Nullable Authentication authentication) {
        User user = null;
        if (authentication != null) {
            user = userRepository.findById(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException(authentication.getName()));
        }
        PhotoVO photoVO = new PhotoVO();
        photoVO.setId(photo.getId());
        photoVO.setDescription(photo.getDescription());
        photoVO.setOwner(photo.getOwner().getId());
        photoVO.setCompetition(photo.getCompetition().getId());
        photoVO.setVotes(voteRepository.countByPhoto(photo));
        if (user != null) {
            photoVO.setVoted(voteRepository.existsByUserAndPhoto(user, photo));
        }

        return photoVO;

    }
}
