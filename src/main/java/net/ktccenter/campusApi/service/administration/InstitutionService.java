package net.ktccenter.campusApi.service.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportInstitutionRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteInstitutionDTO;
import net.ktccenter.campusApi.dto.reponse.administration.InstitutionDTO;
import net.ktccenter.campusApi.dto.request.administration.InstitutionRequestDTO;
import net.ktccenter.campusApi.entities.administration.Institution;
import net.ktccenter.campusApi.service.GenericService;

public interface InstitutionService extends GenericService<Institution, InstitutionRequestDTO, InstitutionDTO, LiteInstitutionDTO, ImportInstitutionRequestDTO> {
  InstitutionDTO getCurrentInstitution();
  boolean existsByName(String name);
  boolean equalsByDto(InstitutionRequestDTO dto, Long id);
}
