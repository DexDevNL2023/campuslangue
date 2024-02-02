package net.ktccenter.campusApi.dto.lite.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusDTO;
import net.ktccenter.campusApi.entities.scolarite.Inscription;

@Setter
@Getter
@NoArgsConstructor
public class LiteInscriptionForCompteDTO {
    private Long id;
    private LiteEtudiantForNoteDTO etudiant;
    private LiteSessionForCompteDTO session;
    private LiteCampusDTO campus;

    public LiteInscriptionForCompteDTO(Inscription inscription) {
        this.id = inscription.getId();
    }
}
