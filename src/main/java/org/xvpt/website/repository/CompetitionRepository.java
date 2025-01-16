package org.xvpt.website.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.xvpt.website.entity.Competition;
import org.xvpt.website.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompetitionRepository extends MongoRepository<Competition, String> {
    List<Competition> findAllByHost(User host);

    Optional<Competition> findByIdAndHost(String id, User host);
}
