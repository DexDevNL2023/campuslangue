package net.ktccenter.campusApi.dto.reponse.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteDiplomeDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteInscriptionDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;
import net.ktccenter.campusApi.enums.Sexe;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class EtudiantDTO extends AbstractDTO {
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
    private LiteBrancheDTO branche;
  private String fullName;
  private Set<LiteInscriptionDTO> inscriptions = new HashSet<>();
  private BigDecimal soldeTotal;
  private Integer nbrePaiement;
  private BigDecimal resteApayer;

  public String getFullName() {
    return !this.prenom.isEmpty() ? this.nom + " " + this.prenom : this.nom;
  }
}
