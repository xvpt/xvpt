package org.xvpt.website.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.xvpt.website.entity.Photo;
import org.xvpt.website.entity.User;
import org.xvpt.website.entity.Vote;

@Repository
public interface VoteRepository extends MongoRepository<Vote, String> {
    boolean existsByUserAndPhoto(User user, Photo photo);

    int countByPhoto(Photo photo);

    void deleteByUserAndPhoto(User user, Photo photo);
}
