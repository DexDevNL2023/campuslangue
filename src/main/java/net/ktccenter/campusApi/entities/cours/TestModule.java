package net.ktccenter.campusApi.entities.cours;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseAuditingEntity;
import net.ktccenter.campusApi.entities.scolarite.Inscription;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "tests_module")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class TestModule extends BaseAuditingEntity {

  @Column(nullable = false, unique = true)
  private String code;

  private Double pourcentage = 0.0;

  @OneToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Inscription inscription;

  @Transient
  private Float moyenne = 0F;
}
