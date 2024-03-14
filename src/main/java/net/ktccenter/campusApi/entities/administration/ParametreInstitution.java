package net.ktccenter.campusApi.entities.administration;

import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseAuditingEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "parametres_institutions")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class ParametreInstitution extends BaseAuditingEntity {
    private int bareme;
    private String devise;
    private double dureeCours;
}
