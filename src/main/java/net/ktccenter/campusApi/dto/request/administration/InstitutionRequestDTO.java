package net.ktccenter.campusApi.dto.request.administration;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class InstitutionRequestDTO {
    private Long id;
    @NotBlank(message = "Le code de l'institution est obligatoire")
    @Size(min = 2, message = "Le code doit être d'au moins 2 caractères")
    private String code;
    @NotBlank(message = "le nom de l'institution est obligatoire")
    private String name;
    @NotBlank(message = "le sigle de l'institution est obligatoire")
    private String sigle;
    private Boolean estFonctionnel;
    private Integer anneeOuverture;
    private Boolean estAgree;
    private Boolean estLocataire;
    private Boolean disposeConventionEtat;
    private Integer nombreSitesOccupes;
    private Boolean estTerrainTitre;
    private Double superficie;
    @NotBlank(message = "l'email de l'institution est obligatoire")
    @Email(message = "le format de l'email est incorrecte", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;
    @NotBlank(message = "le telephone de l'institution est obligatoire")
    private String telephone;
    private String site;
    private String telephone2;
    @NotBlank(message = "l'adresse de l'institution est obligatoire")
    private String adresse;
    @NotBlank(message = "la ville de l'institution est obligatoire")
    private String ville;
    private String bp;
    private Float longitude;
    private Float latitude;
    private String logo;
    private String enteteGauche;
    private String enteteDroite;
    private String piedPage;
}
