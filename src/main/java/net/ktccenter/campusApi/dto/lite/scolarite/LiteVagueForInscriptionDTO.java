package net.ktccenter.campusApi.dto.lite.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.ktccenter.campusApi.entities.scolarite.Vague;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class LiteVagueForInscriptionDTO {
    private Long id;
    private String code;

    public LiteVagueForInscriptionDTO(Vague vague) {
        this.id = vague.getId();
        this.code = vague.getCode();
    }
}
