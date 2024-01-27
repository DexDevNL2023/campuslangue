package net.ktccenter.campusApi.service.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportDiplomeRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteDiplomeDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.DiplomeDTO;
import net.ktccenter.campusApi.dto.request.scolarite.DiplomeRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.Diplome;
import net.ktccenter.campusApi.service.GenericService;

import java.util.List;

public interface DiplomeService extends GenericService<Diplome, DiplomeRequestDTO, DiplomeDTO, LiteDiplomeDTO, ImportDiplomeRequestDTO> {

    boolean equalsByDto(DiplomeRequestDTO dto, Long id);

    Diplome findByCode(String code);

    List<LiteDiplomeDTO> findAll();

    boolean existByCode(String code);

    Diplome findByCodeAndLibelle(String code, String libelle);
}
