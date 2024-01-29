package net.ktccenter.campusApi.controller.evaluation;

import net.ktccenter.campusApi.dto.importation.cours.ImportEpreuveRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteEpreuveDTO;
import net.ktccenter.campusApi.dto.reponse.branch.EpreuveBranchDTO;
import net.ktccenter.campusApi.dto.reponse.cours.EpreuveDTO;
import net.ktccenter.campusApi.dto.request.cours.EpreuveRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface EpreuveController {
  EpreuveDTO save(@RequestBody EpreuveRequestDTO dto);

  List<LiteEpreuveDTO> saveAll(@RequestBody List<ImportEpreuveRequestDTO> dtos);

  EpreuveDTO findById(@PathVariable("id") Long id);

  void delete(@PathVariable("id") Long id);

    List<EpreuveBranchDTO> list();

  Page<LiteEpreuveDTO> pageQuery(Pageable pageable);

  void update(@RequestBody EpreuveRequestDTO dto, @PathVariable("id") Long id);
}
