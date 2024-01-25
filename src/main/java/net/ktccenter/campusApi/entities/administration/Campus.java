package net.ktccenter.campusApi.entities.administration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseAuditingEntity;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "campus")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class Campus extends BaseAuditingEntity {
  @Column(nullable = false, unique = true)
  private String code;
  @Column(nullable = false)
  private String libelle;
  private String adresse;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Branche branche;
}
