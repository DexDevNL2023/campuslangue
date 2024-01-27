package net.ktccenter.campusApi.service.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportModuleRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteModuleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.ModuleDTO;
import net.ktccenter.campusApi.dto.request.administration.ModuleRequestDTO;
import net.ktccenter.campusApi.entities.administration.Module;
import net.ktccenter.campusApi.service.GenericService;

import java.util.List;

public interface ModuleService extends GenericService<Module, ModuleRequestDTO, ModuleDTO, LiteModuleDTO, ImportModuleRequestDTO> {
    List<LiteModuleDTO> findAll();

    boolean existsByName(String name);

    boolean equalsByDto(ModuleRequestDTO dto, Long id);

    Module findByName(String name);
}
