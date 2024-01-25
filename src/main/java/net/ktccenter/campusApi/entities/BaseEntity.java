package net.ktccenter.campusApi.entities;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Setter
@Getter
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @Column(name = "id")
    protected Long id;

    private Boolean isDefault = false;
}
