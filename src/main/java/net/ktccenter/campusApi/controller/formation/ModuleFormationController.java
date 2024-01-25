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
  public ModuleFormationDTO save(@RequestBody ModuleFormationRequestDTO dto);

  public List<LiteModuleFormationDTO> saveAll(@RequestBody List<ImportModuleFormationRequestDTO> dtos);

  public ModuleFormationDTO findById(@PathVariable("id") Long id);

  public void delete(@PathVariable("id") Long id);

  public List<LiteModuleFormationDTO> list();

  public Page<LiteModuleFormationDTO> pageQuery(Pageable pageable);

  public ModuleFormationDTO update(@RequestBody ModuleFormationRequestDTO dto, @PathVariable("id") Long id);
}