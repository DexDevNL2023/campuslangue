package net.ktccenter.campusApi.dto.importation.administration;

import lombok.*;
import net.ktccenter.campusApi.enums.Jour;
import net.ktccenter.campusApi.validators.EnumValidator;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class ImportJourOuvrableRequestDTO {
    @EnumValidator(enumClass = Jour.class)
    private Jour jour;
    @Min(value = 6, message = "Vous devez indiquez une heure de debut superieur ou égale à 7h")
    private Float heureDebut;
    @Max(value = 22, message = "Vous devez indiquez une heure de fin inferieur à 22h")
    private Float heureFin;
}
