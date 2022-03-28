package SpringDataJpa.datajpa.repository;

import SpringDataJpa.datajpa.dto.MemberDto;
import SpringDataJpa.datajpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

    @Query("select m from Member m where m.username = :username321313fewf and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new SpringDataJpa.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String name); //컬렉션

    Member findMemberByUsername(String name); //단건

    Optional<Member> findOptionalByUsername(String name); //단건 Optional

    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);


    // 벌크성 업데이트
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    // 패치 조인
    // 지금부터 4개 메소드는 전부 같은 기능
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    // EntityGraph 를 이용한 패치 조인
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    // 쿼리도 짜고 EntityGraph도 사용 (패치 조인 사용)
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    // 메소드 명으로 짠 메소드에 EntityGraph사용 (패치 조인 사용)
    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    // JPA 쿼리 힌트
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    // JPA가 제공하는 lock 을 어노테이션으로 이렇게 편리하게 쓸 수 있다?
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);
}
