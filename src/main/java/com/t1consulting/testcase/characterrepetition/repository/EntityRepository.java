package com.t1consulting.testcase.characterrepetition.repository;

import com.t1consulting.testcase.characterrepetition.model.CharacterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntityRepository extends JpaRepository<CharacterEntity, Long> {
    Optional<CharacterEntity> findByText(String text);
}
