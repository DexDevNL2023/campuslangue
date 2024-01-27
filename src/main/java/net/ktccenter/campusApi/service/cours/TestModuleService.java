package net.ktccenter.campusApi.service.cours;

import net.ktccenter.campusApi.dto.importation.cours.ImportTestModuleRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteTestModuleDTO;
import net.ktccenter.campusApi.dto.reponse.cours.TestModuleDTO;
import net.ktccenter.campusApi.dto.reponse.cours.TestModuleForNoteReponseDTO;
import net.ktccenter.campusApi.dto.request.cours.TestModuleForNoteDTO;
import net.ktccenter.campusApi.dto.request.cours.TestModuleRequestDTO;
import net.ktccenter.campusApi.entities.cours.TestModule;
import net.ktccenter.campusApi.service.GenericService;

import java.util.List;

public interface TestModuleService extends GenericService<TestModule, TestModuleRequestDTO, TestModuleDTO, LiteTestModuleDTO, ImportTestModuleRequestDTO> {
    List<LiteTestModuleDTO> findAll();

    boolean existsByCode(String code);

  boolean equalsByDto(TestModuleRequestDTO dto, Long id);

  TestModule findByCode(String code);

  List<TestModuleForNoteReponseDTO> getAllTestBySession(Long sessionId, Long moduleId);

  List<TestModuleForNoteReponseDTO> saisieNotesTest(List<TestModuleForNoteDTO> dtos);
}
