package net.ktccenter.campusApi.dto.importation.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class ImportRubriqueRequestDTO {
	@NotBlank(message = "Le code de la rubrique est obligatoire")
	@Size(min = 2, message = "Le code doit être d'au moins 2 caractères")
	private String code;
	@NotBlank(message = "le libelle de la rubrique est obligatoire")
	private String libelle;
	private BigDecimal frais;
	private Double reduction;
}
