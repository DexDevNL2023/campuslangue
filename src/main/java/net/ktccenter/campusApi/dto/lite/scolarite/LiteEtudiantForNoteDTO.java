package net.ktccenter.campusApi.dto.lite.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.scolarite.Etudiant;
import net.ktccenter.campusApi.enums.Sexe;

@Setter
@Getter
@NoArgsConstructor
public class LiteEtudiantForNoteDTO {
  private Long id;
  private String matricule;
  private String nom;
  private String prenom;
  private String nationalite;
  private Sexe sexe;
  private String adresse;
  private String telephone;
  private String email;
  private String fullName;

  public String getFullName() {
    return !this.prenom.isEmpty() ? this.nom + " " + this.prenom : this.nom;
  }

  public LiteEtudiantForNoteDTO(Etudiant etudiant) {
    this.id = etudiant.getId();
    this.matricule = etudiant.getMatricule();
    this.nom = etudiant.getNom();
    this.prenom = etudiant.getPrenom();
    this.nationalite = etudiant.getNationalite();
    this.sexe = etudiant.getSexe();
    this.adresse = etudiant.getAdresse();
    this.telephone = etudiant.getTelephone();
    this.email = etudiant.getEmail();
  }
}
