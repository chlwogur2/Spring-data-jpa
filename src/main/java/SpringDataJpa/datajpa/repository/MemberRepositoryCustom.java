package SpringDataJpa.datajpa.repository;

import SpringDataJpa.datajpa.entity.Member;

import java.util.List;

// 사용자 정의 레포지토리
public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();    // 스프링 데이터 jpa 가 구현한 거 말고 내가 직접 구현해서 쓰고싶음
}
