package net.ktccenter.campusApi.dto.request.scolarite;

import lombok.*;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class NiveauRequestDTO {
    private Long id;
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
    @NotNull(message = "Le diplôme réquis est obligatoire")
    private Long diplomeRequisId;
    @NotNull(message = "Le diplôme de fin fr formation est obligatoire")
    private Long diplomeFinFormationId;
}
