package net.ktccenter.campusApi.dto.reponse.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteJourOuvrableDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteParametreInstitutionDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class InstitutionDTO extends AbstractDTO {
    private String code;
    private String name;
    private String sigle;
    private Boolean estFonctionnel;
    private Integer anneeOuverture;
    private Boolean estAgree;
    private Boolean estLocataire;
    private Boolean disposeConventionEtat;
    private Integer nombreSitesOccupes;
    private Boolean estTerrainTitre;
    private Double superficie;
    private String email;
    private String telephone;
    private String site;
    private String telephone2;
    private String adresse;
    private String ville;
    private String bp;
    private Float latitude;
    private Float longitude;
    private String logo;
    private String enteteGauche;
    private String enteteDroite;
    private String piedPage;

    private List<LiteJourOuvrableDTO> jourOuvrables;
    private LiteParametreInstitutionDTO parametres;
}
