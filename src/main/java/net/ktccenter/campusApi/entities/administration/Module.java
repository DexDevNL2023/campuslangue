package net.ktccenter.campusApi.entities.administration;

import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseAuditingEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "modules_application")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class Module extends BaseAuditingEntity {

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    private Boolean hasDroit;
}
