package SpringDataJpa.datajpa.repository;

import SpringDataJpa.datajpa.dto.MemberDto;
import SpringDataJpa.datajpa.entity.Member;
import SpringDataJpa.datajpa.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sound.midi.Soundbank;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;  // 인터페이스
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();
//        Optional<Member> findMember = memberRepository.findById(savedMember.getId());
//        Member member1 = findMember.get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);
        // 단건 조회 검증
        Member findmember1 = memberRepository.findById(member1.getId()).get();
        Member findmember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findmember1).isEqualTo(member1);
        assertThat(findmember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 15);
        Member m2 = new Member("AAA", 25);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(25);
        assertThat(result.size()).isEqualTo(1);
    }
    // 리포지토리 메소드에 @Query 정의하는 테스트
    @Test
    public void testQuery() {
        Member m1 = new Member("AAA", 15);
        Member m2 = new Member("AAA", 25);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 15);

        assertThat(result.get(0)).isEqualTo(m1);
    }

    // 값 가져오는 테스트
    @Test
    public void testUsernameList(){
        Member m1 = new Member("AAA", 15);
        Member m2 = new Member("BBB", 25);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> result = memberRepository.findUsernameList();

        for(String s : result){
            System.out.println("s = " + s);
        }
    }

    // DTO 테스트
    @Test
    public void findMemberDto(){
        Member m1 = new Member("AAA", 15);
        Member m2 = new Member("BBB", 25);
        memberRepository.save(m1);
        memberRepository.save(m2);

        Team team1 = new Team("Liverpool");
        m1.setTeam(team1);
        teamRepository.save(team1);

        List<MemberDto> result = memberRepository.findMemberDto();

        for(MemberDto m : result){
            System.out.println("dto = " + m);
        }
    }

    // 컬렉션 파라미터 바인딩 테스트
    @Test
    public void findByNames(){
        Member m1 = new Member("AAA", 15);
        Member m2 = new Member("BBB", 25);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> findmembers = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));

        for(Member m : findmembers){
            System.out.println("member = " + m);
        }
    }

    // 반환 타입 여러가지인거 테스트
    @Test
    public void returnType() {
        Member m1 = new Member("AAA", 15);
        Member m2 = new Member("AAA", 25);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> list = memberRepository.findListByUsername("wqeqeqweq");
        System.out.println("list.size() = " + list.size()); // 이상한 유저로 검색해도 NULL 이 뜨지않고 빈 컬렉션을 반환

        Member member = memberRepository.findMemberByUsername("AwewqeAA");
        System.out.println("member = " + member);   // 얘는 NULL 뜸


        Optional<Member> optional = memberRepository.findOptionalByUsername("AAA");

    }
    // 페이징 테스트
    @Test
    public void paging() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));

        //when
        int age = 10;
        // Pageable 인터페이스의 구현체로 PageRequeset 를 많이 쓴다
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        // 정렬 조건이 이름 내림차순, 페이지는 1이 아니고 0부터해야함
        // 한 페이지 당 content 수는 3개

        Page<Member> page = memberRepository.findByAge(age, pageRequest); // 파라미터로 넘기는 Pageable 인터페이스 구현체를
                                                                          // pageRequest
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));


        Slice<Member> slice = memberRepository.findByAge(age, pageRequest);


        //then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();   // TotalCount

        for (Member member : content) {
            System.out.println("member = " + member);
        }
        System.out.println("totalElements = " + totalElements);

        assertThat(content.size()).isEqualTo(3);    // 현재 페이지 content 개수
        assertThat(page.getTotalElements()).isEqualTo(6);   // 총 totalCount 개수
        assertThat(page.getNumber()).isEqualTo(0);      // 현재 페이지 넘버
        assertThat(page.getTotalPages()).isEqualTo(2);  // 총 페이지 개수 (이거는 계산 해야지)
        assertThat(page.isFirst()).isTrue();    // 이게 첫번째 페이지야?
        assertThat(page.hasNext()).isTrue();    // 다음 페이지가 있어?
    }

    // 벌크성 업데이트 테스트
    @Test
    public void bulkUpdate() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        // when
        int resultCount = memberRepository.bulkAgePlus(20);
        // 스무살 이상인 사람들은 1살 더함. result는 3개가 나와야함
//        em.flush();
//        em.clear();   @Modifying(clearAutomatically = true) 이걸로 대체 가능

        // then
        assertThat(resultCount).isEqualTo(3);
    }

    // LAZY N+1 문제 테스트
    @Test
    public void findMemberLAZY() {
        //given
        //member1 -> teamA
        //member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1",10,teamA);
        Member member2 = new Member("member2",10,teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 영속성 컨텍스트에 있는 정보들 DB에 전부 업데이트
        em.flush();
        em.clear();

        // when
        // N+1 문제 발생
        // List<Member> members = memberRepository.findAll();
//        for (Member member : members) {
//            System.out.println("member = " + member.getUsername()); // 여기까지는 Team에 select 쿼리 안날림 (근데 Team 을 Null 로 둘 순 없으니 프록시로 가짜 클래스 만들어둠)
//            System.out.println("team = " + member.getTeam().getClass());  // 이거 찍어보면 알수없는 프록시 가짜 클래스 (Team$HibernateProxy$ZAP8aDV0)
//            System.out.println("member.team = " + member.getTeam().getName()); // 여기서 실제 Team 에 select 쿼리 날림
//        }

        // 해결방법
        List<Member> membersFetch = memberRepository.findMemberFetchJoin();
        // 이렇게 하면 프록시 가짜 클래스 안만들고 멤버 select 시에 진짜 팀이 만들어짐
        for (Member member : membersFetch) {
            System.out.println("member = " + member.getUsername());
            System.out.println("team = " + member.getTeam().getClass());
            System.out.println("member.team = " + member.getTeam().getName());
        }
    }

    @Test
    public void queryHint() {
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");      // 얘는 readOnly 라서 update 문 안나감

        em.flush();
        // 이러면 변경 감지를 해서 update 쿼리가 나감
    }

    // 사용자 정의 인터페이스 테스트
    @Test
    public void findMemberCustom() {
        Member member = new Member("member1", 10);
        List<Member> memberCustom = memberRepository.findMemberCustom();
        System.out.println("memberCustom = " + memberCustom);
    }



}