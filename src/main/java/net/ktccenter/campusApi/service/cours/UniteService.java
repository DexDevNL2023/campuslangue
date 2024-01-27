package net.ktccenter.campusApi.service.cours;

import net.ktccenter.campusApi.dto.importation.cours.ImportUniteRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteUniteDTO;
import net.ktccenter.campusApi.dto.reponse.cours.UniteDTO;
import net.ktccenter.campusApi.dto.request.cours.UniteRequestDTO;
import net.ktccenter.campusApi.entities.cours.Unite;
import net.ktccenter.campusApi.service.GenericService;

import java.util.List;

public interface UniteService extends GenericService<Unite, UniteRequestDTO, UniteDTO, LiteUniteDTO, ImportUniteRequestDTO> {
    List<LiteUniteDTO> findAll();

    boolean existsByCode(String code);

  boolean equalsByDto(UniteRequestDTO dto, Long id);

  Unite findByCode(String code);
}
