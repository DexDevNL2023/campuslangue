package net.ktccenter.campusApi.entities.scolarite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseEntity;
import net.ktccenter.campusApi.entities.administration.Branche;
import net.ktccenter.campusApi.entities.administration.OccupationSalle;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "sessions_formation")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class Session extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String code;

  private Date dateDebut;

  private Date dateFin;

  private Boolean estTerminee = false;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Branche branche;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Niveau niveau;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Vague vague;

  @ManyToMany(fetch = FetchType.LAZY)
  @JsonIgnore
  @JoinTable(name = "session_occupation",
          joinColumns = {@JoinColumn(name = "session_id")},
          inverseJoinColumns = {@JoinColumn(name = "occupation_id")})
  public Set<OccupationSalle> occupations = new HashSet<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Formateur formateur;
}
