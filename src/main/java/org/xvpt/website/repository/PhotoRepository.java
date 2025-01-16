package org.xvpt.website.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.xvpt.website.entity.Photo;

@Repository
public interface PhotoRepository extends MongoRepository<Photo, String> {
}
