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
@Table(name = "salles")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class Salle extends BaseAuditingEntity {

  @Column(nullable = false, unique=true)
  private String code;

  @Column(nullable = false, unique = true)
  private String libelle;

  @Column(nullable = false)
  private Integer capacite;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Campus campus;
}
