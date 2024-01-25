package net.ktccenter.campusApi.entities.cours;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseAuditingEntity;
import net.ktccenter.campusApi.entities.scolarite.ModuleFormation;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "evaluations_test_module")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class EvaluationTest extends BaseAuditingEntity {

  private Date dateEvaluation;

  private Float note = 0.0F;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private TestModule testModule;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private ModuleFormation moduleFormation;
}
