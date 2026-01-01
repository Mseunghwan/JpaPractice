package jpabook.service;

import jpabook.domain.Member;
import jpabook.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
// @Transactional readOnly 속성? 데이터 변경 없는 일긱 전용 메서드에 사용
// 영속성 컨텍스트를 플러시 하지 않으므로 약간의 성능 향상(읽기 전용에선 다 적용)
// DB 드라이버가 지원하면 DB에서 성능 향상
@RequiredArgsConstructor
public class MemberService {

    // @AutoWired로 필드 주입보단 생성자 주입 방식을 권장
    private final MemberRepository memberRepository;

    // 회원가입
    @Transactional // 변경하므로
    public Long join(Member member){

        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    @Transactional
    public void update(Long id, String name){
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 전체 회원 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

}
