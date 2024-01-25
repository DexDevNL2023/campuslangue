package net.ktccenter.campusApi.entities.administration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseAuditingEntity;
import net.ktccenter.campusApi.entities.cours.PlageHoraire;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "occupations")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class OccupationSalle extends BaseAuditingEntity {

  @Column(nullable = false, unique=true)
  private String code;

  private Boolean estOccupee = false;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private PlageHoraire plageHoraire;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Salle salle;
}
