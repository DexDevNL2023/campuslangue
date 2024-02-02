package net.ktccenter.campusApi.dto.request.cours;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class EpreuveRequestDTO {
    private Long id;
    private Float noteObtenue = 0.0F;
    private Boolean estValidee;
    private Float noteRattrapage = 0.0F;
    private Boolean estRattrapee;
    private Long uniteId;
    private Long examenId;
}
