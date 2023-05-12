package com.ll.gramgram.boundedContext.likeablePerson.repository;

import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeablePersonRepository extends JpaRepository<LikeablePerson, Long>, LikeablePersonRepositoryCustom {
    List<LikeablePerson> findByFromInstaMemberId(Long fromInstaMemberId);

    List<LikeablePerson> findByToInstaMember_username(String username);

    LikeablePerson findByFromInstaMemberIdAndToInstaMember_username(long fromInstaMemberId, String username);

    Optional<LikeablePerson> findByFromInstaMember_usernameAndToInstaMember_username(String fromInstaMemberUsername, String toInstaMemberUsername);

    List<LikeablePerson> findByToInstaMemberIdAndFromInstaMember_Gender(long toInstaMemberId, String gender);






}
