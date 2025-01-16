package org.xvpt.website.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.xvpt.website.entity.Media;

@Repository
public interface MediaRepository extends MongoRepository<Media, String> {
}
