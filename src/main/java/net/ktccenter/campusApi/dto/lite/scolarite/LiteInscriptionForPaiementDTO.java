package net.ktccenter.campusApi.dto.lite.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.entities.scolarite.Inscription;

@Setter
@Getter
@NoArgsConstructor
public class LiteInscriptionForPaiementDTO {
    private Long id;
    private LiteEtudiantForNoteDTO etudiant;
    private LiteSessionForCompteDTO session;

    public LiteInscriptionForPaiementDTO(Inscription inscription) {
        this.id = inscription.getId();
    }
}
