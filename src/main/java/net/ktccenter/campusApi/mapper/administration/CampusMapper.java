package net.ktccenter.campusApi.mapper.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportCampusRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusDTO;
import net.ktccenter.campusApi.dto.reponse.administration.CampusDTO;
import net.ktccenter.campusApi.dto.request.administration.CampusRequestDTO;
import net.ktccenter.campusApi.entities.administration.Campus;
import net.ktccenter.campusApi.mapper.GenericMapper;
import net.ktccenter.campusApi.service.administration.BrancheService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BrancheService.class})
public interface CampusMapper extends GenericMapper<Campus, CampusRequestDTO, CampusDTO, LiteCampusDTO, ImportCampusRequestDTO> {
    @Override
    @Mapping(source = "brancheId", target = "branche")
    Campus asEntity(CampusRequestDTO dto);

    @Override
    @Mapping(source = "brancheCode", target = "branche")
    List<Campus> asEntityList(List<ImportCampusRequestDTO> dtoList);
}
