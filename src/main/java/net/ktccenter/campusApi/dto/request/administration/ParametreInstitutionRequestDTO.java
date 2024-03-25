package net.ktccenter.campusApi.dto.request.administration;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class ParametreInstitutionRequestDTO {
    private int bareme;
    private String devise;
    private double dureeCours;
    private int pourcentageTestModule;
}
