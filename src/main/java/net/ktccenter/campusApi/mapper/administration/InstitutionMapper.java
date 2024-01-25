package net.ktccenter.campusApi.mapper.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportInstitutionRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteInstitutionDTO;
import net.ktccenter.campusApi.mapper.GenericMapper;
import net.ktccenter.campusApi.dto.reponse.administration.InstitutionDTO;
import net.ktccenter.campusApi.dto.request.administration.InstitutionRequestDTO;
import net.ktccenter.campusApi.entities.administration.Institution;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InstitutionMapper extends GenericMapper<Institution, InstitutionRequestDTO, InstitutionDTO, LiteInstitutionDTO, ImportInstitutionRequestDTO> {
}
