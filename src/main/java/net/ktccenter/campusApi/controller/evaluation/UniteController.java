package net.ktccenter.campusApi.controller.evaluation;

import net.ktccenter.campusApi.dto.importation.cours.ImportUniteRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteUniteDTO;
import net.ktccenter.campusApi.dto.reponse.cours.UniteDTO;
import net.ktccenter.campusApi.dto.request.cours.UniteRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UniteController {
  public UniteDTO save(@RequestBody UniteRequestDTO dto);

  public List<LiteUniteDTO> saveAll(@RequestBody List<ImportUniteRequestDTO> dtos);

  public UniteDTO findById(@PathVariable("id") Long id);

  public void delete(@PathVariable("id") Long id);

  public List<LiteUniteDTO> list();

  public Page<LiteUniteDTO> pageQuery(Pageable pageable);

  public UniteDTO update(@RequestBody UniteRequestDTO dto, @PathVariable("id") Long id);
}
