package net.ktccenter.campusApi.mapper.cours;

import net.ktccenter.campusApi.dto.importation.cours.ImportTestModuleRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteTestModuleDTO;
import net.ktccenter.campusApi.dto.reponse.cours.TestModuleDTO;
import net.ktccenter.campusApi.dto.request.cours.TestModuleRequestDTO;
import net.ktccenter.campusApi.entities.cours.TestModule;
import net.ktccenter.campusApi.mapper.GenericMapper;
import net.ktccenter.campusApi.service.scolarite.InscriptionService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {InscriptionService.class})
public interface TestModuleMapper extends GenericMapper<TestModule, TestModuleRequestDTO, TestModuleDTO, LiteTestModuleDTO, ImportTestModuleRequestDTO> {
    @Override
    @Mapping(source = "inscriptionId", target = "inscription")
    TestModule asEntity(TestModuleRequestDTO dto);

    @Override
    @Mapping(source = "inscriptionCode", target = "inscription")
    List<TestModule> asEntityList(List<ImportTestModuleRequestDTO> dtoList);
}
