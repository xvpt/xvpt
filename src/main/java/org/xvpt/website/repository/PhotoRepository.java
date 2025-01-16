package org.xvpt.website.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.xvpt.website.entity.*;

import java.util.List;

@Repository
public interface PhotoRepository extends MongoRepository<Photo, String> {
    int countByOwnerAndCompetition(User owner, Competition competition);

    int countByCompetition(Competition competition);

    Page<Photo> findAllByCompetitionAndStatus(Competition competition, PhotoStatus status, Pageable pageable);

    Page<Photo> findAllByStatus(PhotoStatus status, PageRequest pageable);

    List<Photo> findAllByStatus(PhotoStatus photoStatus);
}
