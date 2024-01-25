package net.ktccenter.campusApi.dto.request.scolarite;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class RubriqueRequestDTO {
  private Long id;
	@NotBlank(message = "Le code de la rubrique est obligatoire")
	@Size(min = 2, message = "Le code doit être d'au moins 2 caractères")
	private String code;
	@NotBlank(message = "le libelle de la rubrique est obligatoire")
	private String libelle;
	private BigDecimal frais;
	private Double reduction;
}
