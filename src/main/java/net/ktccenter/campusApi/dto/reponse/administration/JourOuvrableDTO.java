package net.ktccenter.campusApi.dto.reponse.administration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;
import net.ktccenter.campusApi.enums.Jour;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class JourOuvrableDTO extends AbstractDTO {
    private Jour jour;
    private Float heureDebut;
    private Float heureFin;
}
