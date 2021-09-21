package study.datajpa.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 엔티티를 생성, 변경할 때 변경한 사람과 시간을 추적하고 싶으면?
 * 등록일
 * 수정일
 * 등록자
 * 수정자
 *
 * 등록일, 수정일 -> 운영시 필수!
 *
 * [순수 JPA 사용하여 Auditing 적용]
 * 엔티티 클래스에 extends 한다. (@MappedSuperclass 필수)
 */
@MappedSuperclass
@Getter
public class JpaBaseEntity {

    @Column(updatable = false)
    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    /**
     * persist 전에 실행되는 메소드
     */
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now; // 데이터를 넣어야 쿼리 보낼 때 편하다.
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
