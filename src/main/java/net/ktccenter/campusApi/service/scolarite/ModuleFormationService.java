package net.ktccenter.campusApi.service.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportModuleFormationRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteModuleFormationDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.ModuleFormationDTO;
import net.ktccenter.campusApi.dto.request.scolarite.ModuleFormationRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.ModuleFormation;
import net.ktccenter.campusApi.service.GenericService;

import java.util.List;

public interface ModuleFormationService extends GenericService<ModuleFormation, ModuleFormationRequestDTO, ModuleFormationDTO, LiteModuleFormationDTO, ImportModuleFormationRequestDTO> {

  boolean equalsByDto(ModuleFormationRequestDTO dto, Long id);

  ModuleFormation findByCode(String code);

  List<LiteModuleFormationDTO> findAll();

  List<LiteModuleFormationDTO> findAllByNiveau(Long niveauId);

  boolean existByCode(String code);

  ModuleFormation findByCodeAndLibelle(String code, String libelle);
}
