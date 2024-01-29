package net.ktccenter.campusApi.service.cours;

import net.ktccenter.campusApi.dto.importation.cours.ImportEvaluationTestRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteEvaluationTestDTO;
import net.ktccenter.campusApi.dto.reponse.branch.EvaluationTestBranchDTO;
import net.ktccenter.campusApi.dto.reponse.cours.EvaluationTestDTO;
import net.ktccenter.campusApi.dto.request.cours.EvaluationTestRequestDTO;
import net.ktccenter.campusApi.entities.cours.EvaluationTest;
import net.ktccenter.campusApi.service.GenericService;

import java.util.List;

public interface EvaluationTestService extends GenericService<EvaluationTest, EvaluationTestRequestDTO, EvaluationTestDTO, LiteEvaluationTestDTO, ImportEvaluationTestRequestDTO> {
    List<EvaluationTestBranchDTO> findAll();

    boolean equalsByDto(EvaluationTestRequestDTO dto, Long id);
}
