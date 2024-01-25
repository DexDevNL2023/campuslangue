package net.ktccenter.campusApi.entities.cours;

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
@Table(name = "epreuves_langue")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class Epreuve extends BaseAuditingEntity {

  private Float noteObtenue = 0.0F;
  private Boolean estValidee = false;

  private Float noteRattrapage = 0.0F;
  private Boolean estRattrapee = false;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Unite unite;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Examen examen;

  public void setNoteObtenue(Float noteObtenue) {
    this.noteObtenue = noteObtenue;
    if (this.estRattrapee) {
      noteObtenue = this.noteRattrapage;
    }
    if (noteObtenue >= this.unite.getNoteAdmission()) {
      this.estValidee = true;
    } else {
      this.estValidee = false;
    }
  }
}
