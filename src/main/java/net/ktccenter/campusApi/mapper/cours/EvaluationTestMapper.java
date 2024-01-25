package net.ktccenter.campusApi.mapper.cours;

import net.ktccenter.campusApi.dto.importation.cours.ImportEvaluationTestRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteEvaluationTestDTO;
import net.ktccenter.campusApi.dto.reponse.cours.EvaluationTestDTO;
import net.ktccenter.campusApi.dto.request.cours.EvaluationTestRequestDTO;
import net.ktccenter.campusApi.entities.cours.EvaluationTest;
import net.ktccenter.campusApi.mapper.GenericMapper;
import net.ktccenter.campusApi.service.cours.TestModuleService;
import net.ktccenter.campusApi.service.scolarite.ModuleFormationService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TestModuleService.class, ModuleFormationService.class})
public interface EvaluationTestMapper extends GenericMapper<EvaluationTest, EvaluationTestRequestDTO, EvaluationTestDTO, LiteEvaluationTestDTO, ImportEvaluationTestRequestDTO> {
    @Override
    @Mapping(source = "testModuleId", target = "testModule")
    @Mapping(source = "moduleFormationId", target = "moduleFormation")
    EvaluationTest asEntity(EvaluationTestRequestDTO dto);

    @Override
    @Mapping(source = "testModuleCode", target = "testModule")
    @Mapping(source = "moduleFormationCode", target = "moduleFormation")
    List<EvaluationTest> asEntityList(List<ImportEvaluationTestRequestDTO> dtoList);
}
