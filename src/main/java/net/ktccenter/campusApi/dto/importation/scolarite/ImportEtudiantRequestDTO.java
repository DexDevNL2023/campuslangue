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
public class ImportEtudiantRequestDTO {
  @NotBlank(message = "Le nom de l'apprenant est obligatoire")
  private String nom;
  private String prenom;
  @Size(max = 256, message = "La taille de l'image doit être inférieur ou égale à 256")
  private String imageUrl;
  @NotBlank(message = "Nationalité est obligatoire")
  private String nationalite;
  @EnumValidator(enumClass = Sexe.class)
  private Sexe sexe;
  private String adresse;
  private String telephone;
  private String email;
  @NotBlank(message = "Tuteur est obligatoire")
  private String tuteur;
  @NotBlank(message = "Contact du tuteur est obligatoire")
  private String contactTuteur;
  private String dernierDiplomeCode;
  private String brancheCode;
}
