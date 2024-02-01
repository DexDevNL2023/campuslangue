package net.ktccenter.campusApi.dto.lite.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.enums.Sexe;

@Setter
@Getter
@NoArgsConstructor
public class LiteEtudiantDTO {
  private Long id;
  private String matricule;
  private String nom;
  private String prenom;
  private String imageUrl;
  private String nationalite;
  private Sexe sexe;
  private String adresse;
  private String telephone;
  private String email;
  private String tuteur;
  private String contactTuteur;
  private LiteDiplomeDTO dernierDiplome;
  private String fullName;

  public String getFullName() {
    return !this.prenom.isEmpty() ? this.nom + " " + this.prenom : this.nom;
  }
}
