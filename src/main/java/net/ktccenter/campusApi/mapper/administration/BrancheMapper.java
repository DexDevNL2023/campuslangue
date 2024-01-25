package net.ktccenter.campusApi.mapper.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportBrancheRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.dto.reponse.administration.BrancheDTO;
import net.ktccenter.campusApi.dto.request.administration.BrancheRequestDTO;
import net.ktccenter.campusApi.entities.administration.Branche;
import net.ktccenter.campusApi.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring" )
public interface BrancheMapper extends GenericMapper<Branche, BrancheRequestDTO, BrancheDTO, LiteBrancheDTO, ImportBrancheRequestDTO> {
}
