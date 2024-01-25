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
public class FormateurRequestDTO {
  private Long id;
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
  private Long diplomeId;
}
