package net.ktccenter.campusApi.dto.lite.administration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class LitePermissionAndModuleDTO {
    private List<String> modules = new ArrayList<>();
    private List<String> permissions = new ArrayList<>();
}
