package net.ktccenter.campusApi.entities.scolarite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseAuditingEntity;
import net.ktccenter.campusApi.entities.administration.Campus;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "inscriptions")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class Inscription extends BaseAuditingEntity {

  @Column(nullable = false)
  private Date dateInscription;

  private Date dateDelivranceAttestation;
  private Boolean estRedoublant = false;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Session session;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Etudiant etudiant;

  @Column(name = "campus_id", nullable = false)
  private Long campusId;

  @Transient
  private Campus campus;
}
