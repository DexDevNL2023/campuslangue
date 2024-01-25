package net.ktccenter.campusApi.dto.lite.scolarite;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteExamenDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteTestModuleDTO;
import net.ktccenter.campusApi.entities.scolarite.Inscription;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LiteInscriptionDTO {
    private Long id;
    private Date dateInscription;
    private Date dateDelivranceAttestation;
    private Boolean estRedoublant;
    private LiteEtudiantDTO etudiant;
    private LiteSessionDTO session;
    private LiteCampusDTO campus;
    private LiteCompteDTO compte;
    private LiteExamenDTO examen;
    private LiteTestModuleDTO testModule;

    public LiteInscriptionDTO(Inscription inscription) {
        this.id = inscription.getId();
        this.dateInscription = inscription.getDateInscription();
        this.dateDelivranceAttestation = inscription.getDateDelivranceAttestation();
        this.estRedoublant = inscription.getEstRedoublant();
    }
}
