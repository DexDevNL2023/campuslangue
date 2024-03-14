package net.ktccenter.campusApi.dto.importation.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
public class ImportModuleRequestDTO {
    @NotBlank(message = "le nom du module est obligatoire")
    private String name;
    private String description;
    private Boolean hasDroit = true;
}
