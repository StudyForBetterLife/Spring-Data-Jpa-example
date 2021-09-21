package study.datajpa.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * [SimpleJpaRepository]
 *
 * @Repository
 * @Transactional(readOnly = true)
 * public class SimpleJpaRepository<T, ID> ...{
 * @Transactional public <S extends T> S save(S entity) {
 * if (entityInformation.isNew(entity)) {
 * em.persist(entity);
 * return entity;
 * } else {
 * return em.merge(entity);
 * }
 * }
 * ...
 * }
 *
 * <p>
 * [새로운 엔티티를 판단하는 기본 전략]
 * - 식별자가 객체일 때 null 여부로 new/old를 판단 (Long)
 * - 식별자가 자바 기본 타입일 때 0 으로 판단 (long)
 * - Persistable 인터페이스를 구현해서 판단 로직 변경 가능
 * (implements Persistable<String>)
 * <p>
 * 1.JPA 식별자 생성 전략이 @GenerateValue 면
 * -> save() 호출 시점에 식별자가 없으므로 새로운 엔티티로 인식해서 정상 동작한다
 * <p>
 * 2. JPA 식별자 생성 전략이 @Id 만 사용해서 직접 할당이면
 * -> 이미 식별자 값이 있는 상태로 save() 를 호출한다. 따라서 이 경우 merge() 가 호출된다
 * (merge() 는 우선 DB를 호출해서 값을 확인하고, DB에 값이 없으면 새로운 엔티티로 인지하므로 매우 비효율 적이다)
 * -> 이 경우엔 Persistable 를 사용해서 새로운 엔티티 확인 여부를 직접 구현하게는 효과적이다
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item implements Persistable<String> {

    @Id
    private String id;

    @CreatedDate
    private LocalDateTime createdDate;

    public Item(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return createdDate == null;
    }
}
