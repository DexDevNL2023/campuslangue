package net.ktccenter.campusApi.dto.reponse;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;

@Data
@NoArgsConstructor
public class RessourceResponse {
    private LiteBrancheDTO branche;
    private Object objectValue;

    public RessourceResponse(LiteBrancheDTO branche, Object objectValue) {
        this.branche = branche;
        this.objectValue = objectValue;
    }
}