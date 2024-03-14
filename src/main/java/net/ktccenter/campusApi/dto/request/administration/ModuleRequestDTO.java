package net.ktccenter.campusApi.dto.request.administration;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class ModuleRequestDTO  {
    private Long id;
    @NotBlank(message = "le nom du module est obligatoire")
    private String name;
    private String description;
    private Boolean hasDroit = true;
}
