package net.ktccenter.campusApi.dto.lite.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.entities.scolarite.Formateur;
import net.ktccenter.campusApi.enums.Sexe;

@Setter
@Getter
@NoArgsConstructor
public class LiteFormateurDTO {
  private Long id;
  private String matricule;
  private String nom;
  private String prenom;
  private String imageUrl;
  private Sexe sexe;
  private String adresse;
  private String telephone;
  private String email;
  private Integer experience;
  private LiteDiplomeDTO diplome;
  private LiteBrancheDTO branche;
  private String fullName;

  public LiteFormateurDTO(Formateur formateur) {
    this.id = formateur.getId();
    this.matricule = formateur.getMatricule();
    this.nom = formateur.getNom();
    this.prenom = formateur.getPrenom();
    this.imageUrl = formateur.getImageUrl();
    this.sexe = formateur.getSexe();
    this.adresse = formateur.getAdresse();
    this.telephone = formateur.getTelephone();
    this.email = formateur.getEmail();
    this.experience = formateur.getExperience();
  }

  public String getFullName() {
    return !this.prenom.isEmpty() ? this.nom + " " + this.prenom : this.nom;
  }
}
