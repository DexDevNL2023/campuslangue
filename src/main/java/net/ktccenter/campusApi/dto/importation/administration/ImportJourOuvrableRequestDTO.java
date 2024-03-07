package net.ktccenter.campusApi.dto.importation.administration;

import lombok.*;
import net.ktccenter.campusApi.enums.Jour;
import net.ktccenter.campusApi.validators.EnumValidator;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class ImportJourOuvrableRequestDTO {
    @EnumValidator(enumClass = Jour.class)
    private Jour jour;
    private Float[] intervalle;
}
