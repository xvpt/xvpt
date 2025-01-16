package org.xvpt.website.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.xvpt.website.entity.Vote;

@Repository
public interface VoteRepository extends MongoRepository<Vote, String> {
}
