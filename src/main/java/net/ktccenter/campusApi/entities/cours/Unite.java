package net.ktccenter.campusApi.entities.cours;

import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseAuditingEntity;
import net.ktccenter.campusApi.entities.scolarite.Niveau;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "unites_evaluation")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class Unite extends BaseAuditingEntity {

  @Column(nullable = false, unique = true)
  private String code;

  @Column(nullable = false)
  private String libelle;

  private Float noteAdmission;

  @ManyToOne
  private Niveau niveau;
}
