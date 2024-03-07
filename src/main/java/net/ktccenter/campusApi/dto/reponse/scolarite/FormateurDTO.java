package net.ktccenter.campusApi.dto.reponse.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteDiplomeDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteSessionDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;
import net.ktccenter.campusApi.enums.Sexe;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class FormateurDTO extends AbstractDTO {
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
  Set<LiteSessionDTO> sessions = new HashSet<>();

  public String getFullName() {
    return !this.prenom.isEmpty() ? this.nom + " " + this.prenom : this.nom;
  }
}
