package net.ktccenter.campusApi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import javax.persistence.*;
import java.time.Instant;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseAuditingEntity extends BaseEntity {

    private static final long serialVersionUID = 4681401402666658611L;

    @CreatedBy
    @Column(updatable = false)
    private String createdByUser;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdDate;

    @LastModifiedBy
    private String lastModifiedByUser;

    @LastModifiedDate
    private Instant lastModifiedDate;
}
