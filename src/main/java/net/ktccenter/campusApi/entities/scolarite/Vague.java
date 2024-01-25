package net.ktccenter.campusApi.entities.scolarite;

import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "vagues_formation")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class Vague extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String code;
}
