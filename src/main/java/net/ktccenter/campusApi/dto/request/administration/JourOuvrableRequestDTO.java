package net.ktccenter.campusApi.dto.request.administration;

import lombok.*;
import net.ktccenter.campusApi.enums.Jour;
import net.ktccenter.campusApi.validators.EnumValidator;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class JourOuvrableRequestDTO {
    private Long id;
    @EnumValidator(enumClass = Jour.class)
    private Jour jour;
    private Float[] intervalle;
}
