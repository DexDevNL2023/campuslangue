package net.ktccenter.campusApi.dto.reponse.scolarite;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteCompteForInscriptionDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteEtudiantForNoteDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteSessionForInscriptionDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class InscriptionDTO extends AbstractDTO {
    private Date dateInscription;
    private Date dateDelivranceAttestation;
    private Boolean estRedoublant;
    private LiteSessionForInscriptionDTO session;
    private LiteEtudiantForNoteDTO etudiant;
    private LiteCampusDTO campus;
    private LiteCompteForInscriptionDTO compte;
}
