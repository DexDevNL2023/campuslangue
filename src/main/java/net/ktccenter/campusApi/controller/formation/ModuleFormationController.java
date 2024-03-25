package net.ktccenter.campusApi.controller.formation;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportModuleFormationRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteModuleFormationDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.ModuleFormationDTO;
import net.ktccenter.campusApi.dto.request.scolarite.ModuleFormationRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ModuleFormationController {
  ModuleFormationDTO save(@RequestBody ModuleFormationRequestDTO dto);

  List<LiteModuleFormationDTO> saveAll(@RequestBody List<ImportModuleFormationRequestDTO> dtos);

  ModuleFormationDTO findById(@PathVariable("id") Long id);

  void delete(@PathVariable("id") Long id);

  List<LiteModuleFormationDTO> list();

  List<LiteModuleFormationDTO> findAllByNiveau(@PathVariable("niveauId") Long niveauId);

  Page<LiteModuleFormationDTO> pageQuery(Pageable pageable);

  void update(@RequestBody ModuleFormationRequestDTO dto, @PathVariable("id") Long id);
}