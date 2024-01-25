package net.ktccenter.campusApi.dto.lite.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LiteInstitutionDTO {
    private Long id;
    private String name;
    private String sigle;
    private String email;
    private String telephone;
    private String site;
    private String telephone2;
    private String adresse;
    private String ville;
    private String bp;
    private String logo;
    private String enteteGauche;
    private String enteteDroite;
    private String piedPage;
}
