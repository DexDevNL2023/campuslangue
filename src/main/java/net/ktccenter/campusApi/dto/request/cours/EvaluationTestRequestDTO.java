package net.ktccenter.campusApi.dto.request.cours;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class EvaluationTestRequestDTO {
    private Long id;
    @NotNull(message = "la date du test d'evaluation est obligatoire")
    private Date dateEvaluation;
    private Float note = 0.0F;
    private Long testModuleId;
    private Long moduleFormationId;
}
