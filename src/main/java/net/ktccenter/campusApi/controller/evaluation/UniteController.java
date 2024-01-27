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
  UniteDTO save(@RequestBody UniteRequestDTO dto);

  List<LiteUniteDTO> saveAll(@RequestBody List<ImportUniteRequestDTO> dtos);

  UniteDTO findById(@PathVariable("id") Long id);

  void delete(@PathVariable("id") Long id);

  List<LiteUniteDTO> list();

  Page<LiteUniteDTO> pageQuery(Pageable pageable);

  void update(@RequestBody UniteRequestDTO dto, @PathVariable("id") Long id);
}
