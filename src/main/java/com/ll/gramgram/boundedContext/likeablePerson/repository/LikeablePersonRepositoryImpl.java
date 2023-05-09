package com.ll.gramgram.boundedContext.likeablePerson.repository;

import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.ll.gramgram.boundedContext.likeablePerson.entity.QLikeablePerson.likeablePerson;

@RequiredArgsConstructor
public class LikeablePersonRepositoryImpl implements LikeablePersonRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<LikeablePerson> findQslByFromInstaMemberIdAndToInstaMember_username(long fromInstaMemberId, String toInstaMemberUsername) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(likeablePerson)
                        .where(
                                likeablePerson.fromInstaMember.id.eq(fromInstaMemberId)
                                        .and(
                                                likeablePerson.toInstaMember.username.eq(toInstaMemberUsername)
                                        )
                        )
                        .fetchOne()
        );
    }

    //toInstaMemberId 값, fromInstaMember의 gender, fromInstaMember의 attractiveTypeCode로 리스트 반환
    @Override
    public List<LikeablePerson> findQslByGenderAndAttractiveTypeCode(long toInstaMemberId, String gender, Integer attractiveTypeCode) {
        List<LikeablePerson> result = jpaQueryFactory
                .selectFrom(likeablePerson)
                .where(
                        eqToInstaMemberId(toInstaMemberId),
                        eqGender(gender),
                        eqAttractiveTypeCode(attractiveTypeCode)
                )
                .fetch();

        return result;
    }

    private BooleanExpression eqToInstaMemberId(long toInstaMemberId) {
        return likeablePerson.toInstaMember.id.eq(toInstaMemberId);
    }

    private BooleanExpression eqGender(String gender) {
        if (StringUtils.isNullOrEmpty(gender)) {
            return null;
        }

        return likeablePerson.fromInstaMember.gender.eq(gender);
    }

    private BooleanExpression eqAttractiveTypeCode(Integer attractiveTypeCode) {
        if (attractiveTypeCode == null) {
            return null;
        }

        return likeablePerson.attractiveTypeCode.eq(attractiveTypeCode);
    }
}
