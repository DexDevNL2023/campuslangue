package net.ktccenter.campusApi.dto.importation.cours;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ImportEpreuveRequestDTO {
    private Float noteObtenue;
    private Boolean estValidee;
    private Float noteRattrapage;
    private Boolean estRattrapee;
    private String uniteCode;
    private String examenCode;
}
