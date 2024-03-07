package net.ktccenter.campusApi.dto.reponse.administration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.enums.Jour;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RequestJourOuvrableDTO {
    private Long id;
    private Jour jour;
    private Float[] intervalle = {7F, 22F};

    public RequestJourOuvrableDTO(Jour jour) {
        this.jour = jour;
    }
}
