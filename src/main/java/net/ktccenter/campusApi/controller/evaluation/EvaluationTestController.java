package net.ktccenter.campusApi.controller.evaluation;

import net.ktccenter.campusApi.dto.importation.cours.ImportEvaluationTestRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteEvaluationTestDTO;
import net.ktccenter.campusApi.dto.reponse.cours.EvaluationTestDTO;
import net.ktccenter.campusApi.dto.request.cours.EvaluationTestRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface EvaluationTestController {
  EvaluationTestDTO save(@RequestBody EvaluationTestRequestDTO dto);

  List<LiteEvaluationTestDTO> saveAll(@RequestBody List<ImportEvaluationTestRequestDTO> dtos);

  EvaluationTestDTO findById(@PathVariable("id") Long id);

  void delete(@PathVariable("id") Long id);

  List<LiteEvaluationTestDTO> list();

  Page<LiteEvaluationTestDTO> pageQuery(Pageable pageable);

  void update(@RequestBody EvaluationTestRequestDTO dto, @PathVariable("id") Long id);
}
