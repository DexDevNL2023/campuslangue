package net.ktccenter.campusApi.entities.scolarite;

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
@Table(name = "modules_formation")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class ModuleFormation extends BaseAuditingEntity {

  @Column(nullable = false, unique = true)
  private String code;

  @Column(nullable = false, unique = true)
  private String libelle;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Niveau niveau;
}
