package net.ktccenter.campusApi.entities.scolarite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseAuditingEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "logs_impressions")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class LogImpression extends BaseAuditingEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Inscription inscription;
}
