package net.ktccenter.campusApi.controller.evaluation;

import net.ktccenter.campusApi.dto.importation.cours.ImportExamenRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteExamenDTO;
import net.ktccenter.campusApi.dto.reponse.cours.ExamenDTO;
import net.ktccenter.campusApi.dto.request.cours.ExamenRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ExamenController {
  public ExamenDTO save(@RequestBody ExamenRequestDTO dto);

  public List<LiteExamenDTO> saveAll(@RequestBody List<ImportExamenRequestDTO> dtos);

  public ExamenDTO findById(@PathVariable("id") Long id);

  public void delete(@PathVariable("id") Long id);

  public List<LiteExamenDTO> list();

  public Page<LiteExamenDTO> pageQuery(Pageable pageable);

  public ExamenDTO update(@RequestBody ExamenRequestDTO dto, @PathVariable("id") Long id);
}
