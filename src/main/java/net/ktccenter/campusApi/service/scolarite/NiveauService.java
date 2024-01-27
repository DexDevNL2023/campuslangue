package net.ktccenter.campusApi.service.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportNiveauRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteNiveauDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.NiveauDTO;
import net.ktccenter.campusApi.dto.request.scolarite.NiveauRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.Niveau;
import net.ktccenter.campusApi.service.GenericService;

import java.util.List;

public interface NiveauService extends GenericService<Niveau, NiveauRequestDTO, NiveauDTO, LiteNiveauDTO, ImportNiveauRequestDTO> {

    boolean equalsByDto(NiveauRequestDTO dto, Long id);
    Niveau findByCode(String code);

    List<LiteNiveauDTO> findAll();

    boolean existByCode(String code);
    Niveau findByCodeAndLibelle(String code, String libelle);
}
