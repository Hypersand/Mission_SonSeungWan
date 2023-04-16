package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.appConfig.AppConfig;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class LikeablePersonServiceTest {

    @Autowired
    LikeablePersonService likeablePersonService;
    @Autowired
    LikeablePersonRepository likeablePersonRepository;

    @Autowired
    MemberService memberService;


    @Test
    @DisplayName("케이스 4 - 중복 호감 표시 X")
    void t001() {

        //given
        Member member = memberService.findByUsername("user3").get();

        //when
        RsData<LikeablePerson> likeRsData = likeablePersonService.like(member, "insta_user4", 1);

        //then
        assertThat(likeRsData.getResultCode()).isEqualTo("F-5");
        assertThat(likeRsData.getMsg()).isEqualTo("insta_user4는 이미 호감표시를 등록한 인스타 유저입니다.");

    }

    @Test
    @DisplayName("케이스 5 - max 인원 이상의 호감 상대 등록 X")
    void t002() {

        //given
        Long max = AppConfig.getLikeablePersonFromMax();
        Member member = memberService.findByUsername("user2").get();
        for (int i = 1; i <= max; i++) {
            likeablePersonService.like(member, "인스타유저"+i, 1);
        }

        //when
        RsData<LikeablePerson> likeRsData = likeablePersonService.like(member, "인스타유저" + (max+1), 2);

        //then
        assertThat(likeRsData.getResultCode()).isEqualTo("F-6");
        assertThat(likeRsData.getMsg()).isEqualTo(max+"명 이상의 호감상대를 등록 할 수 없습니다.");
    }

    @Test
    @DisplayName("케이스 6 - 기존의 사유와 다른 사유로 호감을 표시하는 경우에는 성공 처리")
    void t003() {

        //given
        Member member = memberService.findByUsername("user3").get();

        //when
        RsData<LikeablePerson> likeRsData = likeablePersonService.like(member, "insta_user4", 2);

        //then
        assertThat(likeRsData.getResultCode()).isEqualTo("S-2");
        assertThat(likeRsData.getMsg()).isEqualTo( "insta_user4님에 대한 호감사유를 외모에서 성격으로 변경합니다.");
    }


}