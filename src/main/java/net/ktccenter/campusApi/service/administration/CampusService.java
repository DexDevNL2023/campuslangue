package net.ktccenter.campusApi.service.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportCampusRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusDTO;
import net.ktccenter.campusApi.dto.reponse.administration.CampusDTO;
import net.ktccenter.campusApi.dto.request.administration.CampusRequestDTO;
import net.ktccenter.campusApi.entities.administration.Campus;
import net.ktccenter.campusApi.service.GenericService;

public interface CampusService extends GenericService<Campus, CampusRequestDTO, CampusDTO, LiteCampusDTO, ImportCampusRequestDTO> {
    boolean existsByCodeAndLibelle(String code, String libelle);

    boolean equalsByDto(CampusRequestDTO dto, Long id);

    Campus findByCode(String code);
}
