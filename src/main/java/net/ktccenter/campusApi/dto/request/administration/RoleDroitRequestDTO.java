package net.ktccenter.campusApi.dto.request.administration;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class RoleDroitRequestDTO {
    private Long id;
    private Long roleId;
    private Long droitId;
    private Boolean hasDroit;
}
