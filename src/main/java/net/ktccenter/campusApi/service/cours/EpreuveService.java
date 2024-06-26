package net.ktccenter.campusApi.service.cours;

import net.ktccenter.campusApi.dto.importation.cours.ImportEpreuveRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteEpreuveDTO;
import net.ktccenter.campusApi.dto.reponse.branch.EpreuveBranchDTO;
import net.ktccenter.campusApi.dto.reponse.cours.EpreuveDTO;
import net.ktccenter.campusApi.dto.request.cours.EpreuveRequestDTO;
import net.ktccenter.campusApi.entities.cours.Epreuve;
import net.ktccenter.campusApi.service.GenericService;

import java.util.List;

public interface EpreuveService extends GenericService<Epreuve, EpreuveRequestDTO, EpreuveDTO, LiteEpreuveDTO, ImportEpreuveRequestDTO> {
    List<EpreuveBranchDTO> findAll();

  boolean equalsByDto(EpreuveRequestDTO dto, Long id);
}
