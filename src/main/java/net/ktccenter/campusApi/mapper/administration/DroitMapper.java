package net.ktccenter.campusApi.mapper.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportDroitRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteDroitDTO;
import net.ktccenter.campusApi.mapper.GenericMapper;
import net.ktccenter.campusApi.dto.reponse.administration.DroitDTO;
import net.ktccenter.campusApi.dto.request.administration.DroitRequestDTO;
import net.ktccenter.campusApi.entities.administration.Droit;
import net.ktccenter.campusApi.service.administration.ModuleService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ModuleService.class})
public interface DroitMapper extends GenericMapper<Droit, DroitRequestDTO, DroitDTO, LiteDroitDTO, ImportDroitRequestDTO> {

    @Override
    @Mapping(source = "moduleId", target = "module")
    Droit asEntity(DroitRequestDTO dto);

    @Override
    @Mapping(source = "moduleName", target = "module")
    List<Droit> asEntityList(List<ImportDroitRequestDTO> dtoList);
}
