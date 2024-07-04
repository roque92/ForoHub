package org.example.forohub.repositories;

import java.util.List;
import java.util.Optional;

import org.example.forohub.entities.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<TopicEntity, Long> {

    List<TopicEntity> findByTitleTopic(String titleTopic);

    List<TopicEntity> findByBodyTopic(String bodyTopic);

    Optional<TopicEntity> findByIdAndAuthorId(Long id, Long authorId);

    List<TopicEntity> findAllByAuthorId(Long id);

}
