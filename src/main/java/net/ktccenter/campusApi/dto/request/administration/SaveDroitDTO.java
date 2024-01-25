package net.ktccenter.campusApi.dto.request.administration;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class SaveDroitDTO {
    private String module;
    private String droit;
    private String Key;
    private String verbe;
    private Boolean isDefault;
}


