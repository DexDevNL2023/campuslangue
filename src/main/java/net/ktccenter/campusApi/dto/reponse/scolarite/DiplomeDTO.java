package net.ktccenter.campusApi.dto.reponse.scolarite;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteEtudiantDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteFormateurDTO;
import net.ktccenter.campusApi.dto.reponse.AbstractDTO;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class DiplomeDTO extends AbstractDTO {
    private String code;
    private String libelle;
    private Set<LiteEtudiantDTO> etudiants = new HashSet<>();
    private Set<LiteFormateurDTO> formateurs = new HashSet<>();
}
