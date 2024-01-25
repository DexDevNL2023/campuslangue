package net.ktccenter.campusApi.entities.scolarite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseAuditingEntity;
import net.ktccenter.campusApi.entities.administration.Campus;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "versements")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class Paiement extends BaseAuditingEntity {

  @Column(nullable = false)
  private String refPaiement;

  @DecimalMin(value = "0")
  private BigDecimal montant;

  @Column(nullable = false)
  private Instant datePaiement;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private ModePaiement modePaiement;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Rubrique rubrique;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Compte compte;

  @Column(name = "campus_id", nullable = false)
  private Long campusId;

  @Transient
  private Campus campus;
}
