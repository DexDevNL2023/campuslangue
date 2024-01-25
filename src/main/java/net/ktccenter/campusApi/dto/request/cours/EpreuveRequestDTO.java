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
    private Float noteObtenue;
    private Boolean estValidee;
    private Float noteRattrapage;
    private Boolean estRattrapee;
    private Long uniteId;
    private Long examenId;
}
