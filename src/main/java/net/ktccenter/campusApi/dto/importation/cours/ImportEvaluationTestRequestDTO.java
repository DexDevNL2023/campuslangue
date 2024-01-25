package net.ktccenter.campusApi.dto.importation.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class ImportEvaluationTestRequestDTO {
    @NotNull(message = "la date du test d'evaluation est obligatoire")
    private Date dateEvaluation;
    private Float note;
    private String testModuleCode;
    private String moduleFormationCode;
}
