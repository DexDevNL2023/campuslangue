package net.ktccenter.campusApi.dto.request.scolarite;

import lombok.*;
import net.ktccenter.campusApi.enums.Sexe;
import net.ktccenter.campusApi.validators.EnumValidator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class EtudiantRequestDTO {
  private Long id;
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
  private Long dernierDiplomeId;
}
