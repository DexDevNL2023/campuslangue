package net.ktccenter.campusApi.dto.lite.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LiteModuleDTO {
    private Long id;
    private String name;
    private String description;
}
