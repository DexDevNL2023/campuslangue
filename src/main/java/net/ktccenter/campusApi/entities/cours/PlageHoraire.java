package net.ktccenter.campusApi.entities.cours;

import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseAuditingEntity;
import net.ktccenter.campusApi.enums.Jour;

import javax.persistence.*;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "plages_horaire")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class PlageHoraire extends BaseAuditingEntity {

  @Column(nullable = false, unique = true)
  private String code;
  private LocalTime startTime;
  private LocalTime endTime;
  @Enumerated(EnumType.STRING)
  private Jour jour;
}
