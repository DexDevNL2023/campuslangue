package net.ktccenter.campusApi.controller.evaluation;

import net.ktccenter.campusApi.dto.importation.cours.ImportTestModuleRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteTestModuleDTO;
import net.ktccenter.campusApi.dto.reponse.cours.TestModuleDTO;
import net.ktccenter.campusApi.dto.request.cours.TestModuleRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface TestModuleController {
  public TestModuleDTO save(@RequestBody TestModuleRequestDTO dto);

  public List<LiteTestModuleDTO> saveAll(@RequestBody List<ImportTestModuleRequestDTO> dtos);

  public TestModuleDTO findById(@PathVariable("id") Long id);

  public void delete(@PathVariable("id") Long id);

  public List<LiteTestModuleDTO> list();

  public Page<LiteTestModuleDTO> pageQuery(Pageable pageable);

  public TestModuleDTO update(@RequestBody TestModuleRequestDTO dto, @PathVariable("id") Long id);
}
