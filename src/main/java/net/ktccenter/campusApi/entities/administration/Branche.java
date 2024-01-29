package net.ktccenter.campusApi.entities.administration;

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
@Table(name="Branches")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class Branche extends BaseEntity {

  @Column(nullable = false, unique=true)
  private String code;

  @Column(nullable = false)
  private String ville;

  @Column(nullable = false)
  private String telephone;

  private String email;

  private Boolean parDefaut = false;
}
