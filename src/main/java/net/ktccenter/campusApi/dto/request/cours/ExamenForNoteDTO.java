package net.ktccenter.campusApi.dto.request.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.cours.Examen;

@Setter
@Getter
@NoArgsConstructor
public class ExamenForNoteDTO {
  private Long examenId;
  private String matricule;
  private String nom;
  private String prenom;
  private String fullName;
    private EpreuveForNoteDTO epreuve;

  public ExamenForNoteDTO(Examen examen) {
    this.examenId = examen.getId();
  }

  public String getFullName() {
    return !this.prenom.isEmpty() ? this.nom + " " + this.prenom : this.nom;
  }
}
