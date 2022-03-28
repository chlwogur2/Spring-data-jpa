package SpringDataJpa.datajpa.repository;

import SpringDataJpa.datajpa.entity.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }
    // 회원 삭제
    public void delete(Member member) {
        em.remove(member);
    }

    // 전체 조회 (반환을 getResultList() 사용)
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
    // 단건 조회지만, 얘는 리턴 값이 NULL 이 될 수 있음 ------- Optional 사용
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    // 단건을 조회할 떄는 getSingleResult() 사용
    public long count(){
        return em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age) {
        return em.createQuery("select m from Member m where m.username= :username and m.age > :age")
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    // 페이징과 정렬
    public List<Member> findByPage(int age, int offset, int limit) {    // offset 과 limit 은 어디서부터 어디까지 찾을 건지
            return em.createQuery("select m from Member m where m.age= :age order by m.username desc") // age는 파라미터로, 정렬 기준은 유저네임 내림차순
                .setParameter("age", age)
                .setFirstResult(offset)     // 맨 처음 result 를 가져올 곳을 설정
                .setMaxResults(limit)       // result 를 가져올 갯수 설정
                .getResultList();
    }

    // Total count
    // 내 페이지가 몇 번쨰의 몇인지
    public long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age= :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }

    public int bulkAgePlus(int age) {   // 파라미터로 넘어온 나이보다 나이가 같거나 큰 사람은 1살 추가
        int resultCount = em.createQuery("update Member m set m.age = m.age + 1 where m.age >= :age")
                .setParameter("age", age)
                .executeUpdate();
        return resultCount;
    }
}
