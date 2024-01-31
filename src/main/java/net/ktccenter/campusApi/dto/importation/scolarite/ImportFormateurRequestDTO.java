package net.ktccenter.campusApi.dto.importation.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.enums.Sexe;
import net.ktccenter.campusApi.validators.EnumValidator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
public class ImportFormateurRequestDTO {
  private String matricule;
  @NotBlank(message = "Le nom du formateur est obligatoire")
  private String nom;
  private String prenom;
  @Size(max = 256, message = "La taille de l'image doit être inférieur ou égale à 256")
  private String imageUrl;
  @EnumValidator(enumClass = Sexe.class)
  private Sexe sexe;
  private String adresse;
  private String telephone;
  private String email;
  private Integer experience;
  private String diplomeCode;
    private String brancheCode;
}
