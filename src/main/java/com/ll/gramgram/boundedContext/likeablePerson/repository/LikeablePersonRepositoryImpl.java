package com.ll.gramgram.boundedContext.likeablePerson.repository;

import com.ll.gramgram.boundedContext.likeablePerson.dto.ToListSearchForm;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ll.gramgram.boundedContext.likeablePerson.entity.QLikeablePerson.likeablePerson;

@RequiredArgsConstructor
@Slf4j
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
    public List<LikeablePerson> findQslByAllParameters(long toInstaMemberId, ToListSearchForm toListSearchForm) {

        String gender = toListSearchForm.getGender();
        Integer attractiveTypeCode = toListSearchForm.getAttractiveTypeCode();
        Integer sortCode = toListSearchForm.getSortCode();

        List<LikeablePerson> likeablePeople = jpaQueryFactory
                .selectFrom(likeablePerson)
                .where(
                        eqToInstaMemberId(toInstaMemberId),
                        eqGender(gender),
                        eqAttractiveTypeCode(attractiveTypeCode)
                )
                .orderBy((sortLikeablePeople(sortCode)))
                .fetch();

        return likeablePeople;
    }

    private OrderSpecifier[] sortLikeablePeople(Integer sortCode) {

        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();


        switch (sortCode != null ? sortCode : 1) {
            case 1:
                orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, likeablePerson.id));
                break;
            case 2:
                orderSpecifiers.add(new OrderSpecifier<>(Order.ASC, likeablePerson.id));
                break;
                //인기 많은 순
            case 3:
                orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, likeablePerson.fromInstaMember.likes));
                break;
                //인기 적은 순
            case 4:
                orderSpecifiers.add(new OrderSpecifier<>(Order.ASC, likeablePerson.fromInstaMember.likes));
                break;
            case 5:
                //여자 - 남자
                orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, likeablePerson.fromInstaMember.gender));
                orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, likeablePerson.id));
                break;
            case 6:
                //외모 - 성격 - 능력
                orderSpecifiers.add(new OrderSpecifier<>(Order.ASC, likeablePerson.attractiveTypeCode));
                orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, likeablePerson.id));
        }

        return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
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
