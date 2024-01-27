package net.ktccenter.campusApi.service.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportRoleDroitRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteRoleDroitDTO;
import net.ktccenter.campusApi.dto.reponse.administration.RoleDroitDTO;
import net.ktccenter.campusApi.dto.request.administration.RoleDroitRequestDTO;
import net.ktccenter.campusApi.entities.administration.RoleDroit;
import net.ktccenter.campusApi.service.GenericService;

import java.util.List;

public interface RoleDroitService extends GenericService<RoleDroit, RoleDroitRequestDTO, RoleDroitDTO, LiteRoleDroitDTO, ImportRoleDroitRequestDTO> {
    List<LiteRoleDroitDTO> findAll();
}
