package net.ktccenter.campusApi.mapper.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportModuleFormationRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteModuleFormationDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.ModuleFormationDTO;
import net.ktccenter.campusApi.dto.request.scolarite.ModuleFormationRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.ModuleFormation;
import net.ktccenter.campusApi.mapper.GenericMapper;
import net.ktccenter.campusApi.service.scolarite.NiveauService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {NiveauService.class})
public interface ModuleFormationMapper extends GenericMapper<ModuleFormation, ModuleFormationRequestDTO, ModuleFormationDTO, LiteModuleFormationDTO, ImportModuleFormationRequestDTO> {
    @Override
    @Mapping(source = "niveauId", target = "niveau")
    ModuleFormation asEntity(ModuleFormationRequestDTO dto);

    @Override
    @Mapping(source = "niveauCode", target = "niveau")
    List<ModuleFormation> asEntityList(List<ImportModuleFormationRequestDTO> dtoList);
}
