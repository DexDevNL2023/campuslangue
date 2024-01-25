package net.ktccenter.campusApi.mapper.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportModuleRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteModuleDTO;
import net.ktccenter.campusApi.mapper.GenericMapper;
import net.ktccenter.campusApi.dto.reponse.administration.ModuleDTO;
import net.ktccenter.campusApi.dto.request.administration.ModuleRequestDTO;
import net.ktccenter.campusApi.entities.administration.Module;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring" )
public interface ModuleMapper extends GenericMapper<Module, ModuleRequestDTO, ModuleDTO, LiteModuleDTO, ImportModuleRequestDTO> {
}
