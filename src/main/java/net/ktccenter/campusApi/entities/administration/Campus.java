package net.ktccenter.campusApi.entities.administration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseAuditingEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

  @Transient
  private int nombreSalle = 0;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Branche branche;
  @OneToMany(mappedBy = "campus", fetch = FetchType.LAZY)
  @JsonIgnore
  private List<Salle> salles = new ArrayList<>();

  public int getNombreSalle() {
    return this.salles.size();
  }
}
