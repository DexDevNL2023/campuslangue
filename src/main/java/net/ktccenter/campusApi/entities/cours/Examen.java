package net.ktccenter.campusApi.entities.cours;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseAuditingEntity;
import net.ktccenter.campusApi.entities.scolarite.Inscription;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "examens_langue")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class Examen extends BaseAuditingEntity {

  @Column(nullable = false)
  private String code;

  private Date dateExamen;

  private Double pourcentage = 0.0;

  @DecimalMin(value = "0")
  private BigDecimal totalFraisPension;

  @DecimalMin(value = "0")
  private BigDecimal totalFraisRattrapage;

  @OneToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Inscription inscription;

  @Transient
  private Float moyenne = 0F;
}
