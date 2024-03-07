package net.ktccenter.campusApi.dto.lite.administration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.administration.JourOuvrable;
import net.ktccenter.campusApi.enums.Jour;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LiteJourOuvrableDTO {
    private Long id;
    private Jour jour;
    private Float[] intervalle;

    public LiteJourOuvrableDTO(JourOuvrable jourOuvrable) {
        this.id = jourOuvrable.getId();
        this.jour = jourOuvrable.getJour();
        this.intervalle = jourOuvrable.getIntervalle();
    }
}
