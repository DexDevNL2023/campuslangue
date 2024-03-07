package net.ktccenter.campusApi.service.cours;

import net.ktccenter.campusApi.dto.importation.cours.ImportPlageHoraireRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LitePlageHoraireDTO;
import net.ktccenter.campusApi.dto.reponse.cours.PlageDTO;
import net.ktccenter.campusApi.dto.reponse.cours.PlageHoraireDTO;
import net.ktccenter.campusApi.dto.request.cours.PlageHoraireRequestDTO;
import net.ktccenter.campusApi.entities.cours.PlageHoraire;
import net.ktccenter.campusApi.service.GenericService;

import java.util.List;

public interface PlageHoraireService extends GenericService<PlageHoraire, PlageHoraireRequestDTO, PlageHoraireDTO, LitePlageHoraireDTO, ImportPlageHoraireRequestDTO> {
    List<LitePlageHoraireDTO> findAll();

    boolean existsByCode(String code);

    boolean equalsByDto(PlageHoraireRequestDTO dto, Long id);

    PlageHoraire findByCode(String code);

    List<PlageDTO> findByJour(String code);
}
