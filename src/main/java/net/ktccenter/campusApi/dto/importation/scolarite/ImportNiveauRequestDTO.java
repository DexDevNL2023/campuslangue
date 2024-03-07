package net.ktccenter.campusApi.dto.importation.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class ImportNiveauRequestDTO {
    @NotBlank(message = "Le code du niveau est obligatoire")
    @Size(min = 2, message = "Le code doit être d'au moins 2 caractères")
    private String code;
    @NotBlank(message = "le libelle du niveau est obligatoire")
    private String libelle;
    @DecimalMin(value = "0", inclusive = false)
    private BigDecimal fraisInscription;
    @DecimalMin(value = "0", inclusive = false)
    private BigDecimal fraisPension;
    private BigDecimal fraisRattrapage;
    @NotBlank(message = "Le diplôme réquis est obligatoire")
    private String diplomeRequisCode;
    @NotBlank(message = "Le diplôme de fin formation est obligatoire")
    private String diplomeFinFormationCode;
    @Min(value = 1, message = "Vous devez indiquez la durée d'une séance de cours pour ce niveau")
    private Float dureeSeance = 2F;
}
