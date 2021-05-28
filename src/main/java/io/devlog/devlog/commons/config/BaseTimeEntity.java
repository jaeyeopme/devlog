package io.devlog.devlog.commons.config;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * MappedSuperclass - entity 들의 공통 매핑 정보가 필요할 때 사용합니다.
 * EntityListeners - entity 에 사용할 콜백 리스너 클래스를 지정합니다.
 * <p>
 * AuditingEntityListener
 * entity 의 저장 및 업데이트에 대한 감사 정보를 캡쳐합니다.
 * 내부적으로 PrePersist, PreUpdate 두가지 이벤트를 가지고 있습니다.
 * PrePersist - persist() 메서드를 호출해서 entity 를 persistence context 에 관리하기 직전에 호출됩니다.
 * PreUpdate - flush 나 commit 을 호출해서 entity 를 데이터베이스에 수정하기 직전에 호출됩니다.
 */

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

}
