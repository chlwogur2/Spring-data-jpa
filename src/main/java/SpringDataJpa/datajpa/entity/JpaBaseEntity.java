package SpringDataJpa.datajpa.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;


// 순수 Jpa를 사용한 Auditing
@MappedSuperclass
public class JpaBaseEntity {

    @Column(updatable = false)  // 업데이트가 안되게 막음
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @PrePersist     // 영속하기 전에 발생하는 이벤트
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createDate = now;   // 최초 등록시 시간
        updateDate = now;   // 얘도 일단 처음에는 등록시간과 똑같이
    }

    @PreUpdate  // 업데이트 전 발생하는 이벤트
    public void preUpdate() {
        updateDate = LocalDateTime.now();
    }
}
