package com.ll.gramgram.boundedContext.likeablePerson.repository;

import com.ll.gramgram.boundedContext.likeablePerson.dto.LikeablePersonDto;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static com.ll.gramgram.boundedContext.likeablePerson.entity.QLikeablePerson.likeablePerson;
import static com.querydsl.core.types.OrderSpecifier.*;

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

    @Override
    public List<LikeablePerson> findQslByAllParameters(long toInstaMemberId, LikeablePersonDto likeablePersonDto) {

        String gender = likeablePersonDto.getGender();
        Integer attractiveTypeCode = likeablePersonDto.getAttractiveTypeCode();
        Integer sortCode = likeablePersonDto.getSortCode();

        List<LikeablePerson> result = jpaQueryFactory
                .selectFrom(likeablePerson)
                .where(
                        eqToInstaMemberId(toInstaMemberId),
                        eqGender(gender),
                        eqAttractiveTypeCode(attractiveTypeCode)
                )
                .orderBy((sortLikeablePeople(sortCode)))
                .fetch();

        return result;
    }

    private OrderSpecifier<?> sortLikeablePeople(Integer sortCode) {

        if (sortCode == null) {
            return new OrderSpecifier(Order.ASC, NullExpression.DEFAULT, NullHandling.Default);
        }

        switch (sortCode) {
            case 1:
                return new OrderSpecifier<>(Order.DESC, likeablePerson.id);
            case 2:
                return new OrderSpecifier<>(Order.ASC, likeablePerson.createDate);
//            case 3: //인기가 많다 = 호감표시를 많이 받았다
//                return new OrderSpecifier<>(Order.DESC, likeablePerson.fromInstaMember.)
            default:
                return new OrderSpecifier<>(Order.DESC, likeablePerson.id);
        }
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
