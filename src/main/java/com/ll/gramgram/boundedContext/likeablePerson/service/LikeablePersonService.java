package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.appConfig.AppConfig;
import com.ll.gramgram.base.event.EventAfterLike;
import com.ll.gramgram.base.event.EventAfterModifyAttractiveType;
import com.ll.gramgram.base.event.EventBeforeCancelLike;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeablePersonService {
    private final LikeablePersonRepository likeablePersonRepository;
    private final InstaMemberService instaMemberService;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public RsData<LikeablePerson> like(Member member, String username, int attractiveTypeCode) {
        if (member.hasConnectedInstaMember() == false) {
            return RsData.of("F-2", "먼저 본인의 인스타그램 아이디를 입력해야 합니다.");
        }

        if (member.getInstaMember().getUsername().equals(username)) {
            return RsData.of("F-1", "본인을 호감상대로 등록할 수 없습니다.");
        }

        InstaMember fromInstaMember = member.getInstaMember();
        InstaMember toInstaMember = instaMemberService.findByUsernameOrCreate(username).getData();

        LikeablePerson likeablePerson = getLikeablePerson(fromInstaMember, toInstaMember);

        if (!isRegisteredToInstaMember(likeablePerson)) {
            return RsData.of("F-5", username + "는 이미 호감표시를 등록한 인스타 유저입니다.");
        }

        if (!canRegisterToInstaMember(fromInstaMember)) {
            return RsData.of("F-6", AppConfig.getLikeablePersonFromMax() + "명 이상의 호감상대를 등록 할 수 없습니다.");
        }

        likeablePerson = LikeablePerson
                .builder()
                .fromInstaMember(fromInstaMember) // 호감을 표시하는 사람의 인스타 멤버
                .fromInstaMemberUsername(fromInstaMember.getUsername()) // 중요하지 않음
                .toInstaMember(toInstaMember) // 호감을 받는 사람의 인스타 멤버
                .toInstaMemberUsername(toInstaMember.getUsername()) // 중요하지 않음
                .attractiveTypeCode(attractiveTypeCode) // 1=외모, 2=능력, 3=성격
                .modifyUnlockDate(AppConfig.genLikeablePersonModifyUnlockDate())
                .build();

        likeablePersonRepository.save(likeablePerson); // 저장

        // 너가 좋아하는 호감표시 생겼어.
        fromInstaMember.addFromLikeablePerson(likeablePerson);

        // 너를 좋아하는 호감표시 생겼어.
        toInstaMember.addToLikeablePerson(likeablePerson);
        publisher.publishEvent(new EventAfterLike(this, likeablePerson));

        return RsData.of("S-1", "입력하신 인스타유저(%s)를 호감상대로 등록되었습니다.".formatted(username), likeablePerson);
    }

    private LikeablePerson getLikeablePerson(InstaMember fromInstaMember, InstaMember toInstaMember) {
        return likeablePersonRepository.findByFromInstaMemberIdAndToInstaMemberId(fromInstaMember.getId(), toInstaMember.getId()).orElse(null);
    }

    public List<LikeablePerson> findByFromInstaMemberId(Long fromInstaMemberId) {
        return likeablePersonRepository.findByFromInstaMemberId(fromInstaMemberId);
    }

    public Optional<LikeablePerson> findById(Long id) {
        return likeablePersonRepository.findById(id);
    }


    public RsData<LikeablePerson> canDelete(LikeablePerson likeablePerson, Member member) {

        long diff = ChronoUnit.SECONDS.between(likeablePerson.getModifyDate(), LocalDateTime.now());

        if (!isCoolTimeOver(diff)) {
            return remainCoolTimeRsData(diff);
        }

        if (likeablePerson == null) {
            return RsData.of("F-3", "해당 항목은 존재하지 않는 데이터입니다.");
        }

        Long currentInstaMemberId = member.getInstaMember().getId();

        Long fromInstaMemberId = likeablePerson.getFromInstaMember().getId();

        if (currentInstaMemberId != fromInstaMemberId) {
            return RsData.of("F-4", "해당 항목을 삭제할 권한이 없습니다.");
        }

        return RsData.of("S-1", "삭제 가능합니다.");
    }

    @Transactional
    public RsData<LikeablePerson> delete(LikeablePerson likeablePerson) {

        String toInstaUsername = likeablePerson.getToInstaMember().getUsername();

        likeablePerson.getFromInstaMember().removeFromLikeablePerson(likeablePerson);

        likeablePerson.getToInstaMember().removeToLikeablePerson(likeablePerson);
        publisher.publishEvent(new EventBeforeCancelLike(this, likeablePerson));

        likeablePersonRepository.delete(likeablePerson);

        return RsData.of("S-1", toInstaUsername + "님이 당신의 호감목록에서 제외됐습니다.");
    }

    @Transactional
    public RsData<LikeablePerson> modifyAttractive(Member member, Long id, int attractiveTypeCode) {
        LikeablePerson likeablePerson = findById(id).orElse(null);

        if (likeablePerson == null) {
            return RsData.of("F-3", "해당 항목은 존재하지 않는 데이터입니다.");
        }

        if (member.getInstaMember().getId() != likeablePerson.getFromInstaMember().getId()) {
            return RsData.of("F-2", "해당 호감표시를 수정할 권한이 없습니다.");
        }

        RsData<LikeablePerson> canModifyRsData = canModifyLike(member, likeablePerson);

        if (canModifyRsData.isFail()) {
            return canModifyRsData;
        }

        RsData<LikeablePerson> modifyRsData = modifyAttractionTypeCode(likeablePerson, attractiveTypeCode);

        return modifyRsData;
    }

    public RsData<LikeablePerson> modifyAttractionTypeCode(LikeablePerson likeablePerson, int attractiveTypeCode) {
        int oldAttractiveTypeCode = likeablePerson.getAttractiveTypeCode();
        String oldAttractiveTypeDisplayName = likeablePerson.getAttractiveTypeDisplayName();
        String username = likeablePerson.getToInstaMemberUsername();
        RsData rsData = likeablePerson.updateAttractionTypeCode(attractiveTypeCode);

        if (rsData.isSuccess()) {
            publisher.publishEvent(new EventAfterModifyAttractiveType(this, likeablePerson, oldAttractiveTypeCode, attractiveTypeCode));
        }

        String newAttractiveTypeDisplayName = likeablePerson.getAttractiveTypeDisplayName();

        return RsData.of("S-3", "%s님에 대한 호감사유를 %s에서 %s(으)로 변경합니다.".formatted(username, oldAttractiveTypeDisplayName, newAttractiveTypeDisplayName), likeablePerson);
    }

    public RsData<LikeablePerson> canModifyLike(Member member, LikeablePerson likeablePerson) {

        long diff = ChronoUnit.SECONDS.between(likeablePerson.getModifyDate(), LocalDateTime.now());

        if (!isCoolTimeOver(diff)) {
            return remainCoolTimeRsData(diff);
        }

        if (!member.hasConnectedInstaMember()) {
            return RsData.of("F-1", "먼저 본인의 인스타그램 아이디를 입력해주세요.");
        }

        if (likeablePerson == null) {
            return RsData.of("F-3", "해당 항목은 존재하지 않는 데이터입니다.");
        }

        InstaMember fromInstaMember = member.getInstaMember();

        if (!Objects.equals(likeablePerson.getFromInstaMember().getId(), fromInstaMember.getId())) {
            return RsData.of("F-2", "해당 호감표시를 수정할 권한이 없습니다.");
        }

        return RsData.of("S-1", "호감표시 수정이 가능합니다.");
    }


    public boolean isRegisteredToInstaMember(LikeablePerson likeablePerson) {
        return likeablePerson == null;
    }

    public boolean canRegisterToInstaMember(InstaMember fromInstaMember) {
        Long likeablePersonFromMax = AppConfig.getLikeablePersonFromMax();
        Long size = likeablePersonRepository.countByFromInstaMemberId(fromInstaMember.getId());

        return size < likeablePersonFromMax;
    }

    private boolean isCoolTimeOver(long diff) {

        Long coolTime = AppConfig.getLikeablePersonModifyCoolTime();

        if (diff < coolTime) {
            return false;
        }

        return true;
    }

    private RsData<LikeablePerson> remainCoolTimeRsData(long diff) {
        diff = 10800 - diff;
        long hour = diff / 3600;
        diff %= 3600;
        long min = diff / 60;
        diff %= 60;
        long sec = diff;

        return RsData.of("F-5", "변경하려면 " + hour + "시간 " + min + "분 " + sec + "초 남았습니다!");
    }

}
