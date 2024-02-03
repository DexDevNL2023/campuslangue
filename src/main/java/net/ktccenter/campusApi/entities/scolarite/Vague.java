package net.ktccenter.campusApi.entities.scolarite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseEntity;
import net.ktccenter.campusApi.entities.administration.Branche;

import javax.persistence.*;

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

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Branche branche;
}
