package com.ll.gramgram.boundedContext.likeablePerson.repository;

import com.ll.gramgram.boundedContext.likeablePerson.dto.ToListSearchForm;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;

import java.util.List;
import java.util.Optional;

public interface LikeablePersonRepositoryCustom {
    Optional<LikeablePerson> findQslByFromInstaMemberIdAndToInstaMember_username(long fromInstaMemberId, String toInstaMemberUsername);

    List<LikeablePerson> findQslByAllParameters(long toInstaMemberId, ToListSearchForm toListSearchForm);
}
