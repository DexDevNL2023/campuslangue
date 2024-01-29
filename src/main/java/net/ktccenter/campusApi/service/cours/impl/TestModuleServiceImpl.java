package net.ktccenter.campusApi.service.cours.impl;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.dao.cours.EvaluationTestRepository;
import net.ktccenter.campusApi.dao.cours.TestModuleRepository;
import net.ktccenter.campusApi.dao.scolarite.ModuleFormationRepository;
import net.ktccenter.campusApi.dao.scolarite.NiveauRepository;
import net.ktccenter.campusApi.dto.importation.cours.ImportTestModuleRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteEvaluationTestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteTestModuleDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteEtudiantForNoteDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteInscriptionForNoteDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteModuleFormationDTO;
import net.ktccenter.campusApi.dto.reponse.branch.TestModuleBranchDTO;
import net.ktccenter.campusApi.dto.reponse.cours.TestModuleDTO;
import net.ktccenter.campusApi.dto.reponse.cours.TestModuleForNoteReponseDTO;
import net.ktccenter.campusApi.dto.request.cours.TestModuleForNoteDTO;
import net.ktccenter.campusApi.dto.request.cours.TestModuleRequestDTO;
import net.ktccenter.campusApi.entities.cours.EvaluationTest;
import net.ktccenter.campusApi.entities.cours.TestModule;
import net.ktccenter.campusApi.entities.scolarite.ModuleFormation;
import net.ktccenter.campusApi.entities.scolarite.Niveau;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.cours.TestModuleMapper;
import net.ktccenter.campusApi.service.cours.TestModuleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class TestModuleServiceImpl implements TestModuleService {
  private final TestModuleRepository repository;
  private final TestModuleMapper mapper;
  private final EvaluationTestRepository evaluationTestRepository;
  private final NiveauRepository niveauRepository;
  private final ModuleFormationRepository moduleFormationRepository;

  public TestModuleServiceImpl(TestModuleRepository repository, TestModuleMapper mapper, EvaluationTestRepository evaluationTestRepository, NiveauRepository niveauRepository, ModuleFormationRepository moduleFormationRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.evaluationTestRepository = evaluationTestRepository;
    this.niveauRepository = niveauRepository;
    this.moduleFormationRepository = moduleFormationRepository;
  }

  @Override
  public TestModuleDTO save(TestModuleRequestDTO dto) {
    return buildTestModuleDto(repository.save(mapper.asEntity(dto)));
  }

  private TestModuleDTO buildTestModuleDto(TestModule testModule) {
    TestModuleDTO dto = mapper.asDTO(testModule);
    Set<LiteEvaluationTestDTO> evaluations = getAllEvaluationsForTestModule(testModule);
    if (evaluations.isEmpty()) {
      evaluations = createAllEvaluationsForTestModule(testModule);
    }
    dto.setEvaluations(evaluations);
    dto.setMoyenne(calculMoyenne(evaluations));
    return dto;
  }

  private Set<LiteEvaluationTestDTO> createAllEvaluationsForTestModule(TestModule testModule) {
    Set<LiteEvaluationTestDTO> list = new HashSet<>();
    Niveau niveau = niveauRepository.findById(testModule.getInscription().getSession().getNiveau().getId()).orElseThrow(
            () ->  new ResourceNotFoundException("Le niveau avec l'id "+testModule.getInscription().getSession().getNiveau().getId()+" n'existe pas")
    );
    log.info("Find niveau "+niveau.toString());
    List<ModuleFormation> moduleFormations = moduleFormationRepository.findAllByNiveau(niveau);
    if (moduleFormations.isEmpty()) throw new ResourceNotFoundException("Avant d'effectuer une inscription veillez ajouter au moins un module de formation pour le niveau "+niveau.getLibelle());
    log.info("Get all module by niveau " + moduleFormations);
    log.info("add test to test module");
    for (ModuleFormation module : moduleFormations) {
      EvaluationTest test = new EvaluationTest();
      test.setModuleFormation(module);
      test.setTestModule(testModule);
      test = evaluationTestRepository.save(test);
      log.info("new test " + test);
      list.add(buildEvaluationLiteDto(test));
    }
    return list;
  }

  @Override
  public List<LiteTestModuleDTO> save(List<ImportTestModuleRequestDTO> dtos) {
    return  ((List<TestModule>) repository.saveAll(mapper.asEntityList(dtos)))
            .stream()
            .map(this::buildTestModuleLiteDto)
            .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    TestModule testModule = findById(id);
    if (!getAllEvaluationsForTestModule(testModule).isEmpty())
        throw new ResourceNotFoundException("Le test module avec l'id " + id + " ne peut pas etre supprimée car elle est utilisée par d'autre ressource");
    repository.deleteById(id);
  }

  private Set<LiteEvaluationTestDTO> getAllEvaluationsForTestModule(TestModule testModule) {
    return evaluationTestRepository.findAllByTestModule(testModule).stream().map(this::buildEvaluationLiteDto).collect(Collectors.toSet());
  }

  private LiteEvaluationTestDTO buildEvaluationLiteDto(EvaluationTest evaluation) {
    LiteEvaluationTestDTO lite = new LiteEvaluationTestDTO(evaluation);
    lite.setModuleFormation(new LiteModuleFormationDTO(evaluation.getModuleFormation()));
    return lite;
  }

  @Override
  public TestModuleDTO getOne(Long id) {
    return buildTestModuleDto(findById(id));
  }

  @Override
  public TestModule findById(Long id) {
    return repository.findById(id).orElseThrow(
            () ->  new ResourceNotFoundException("Le test module avec l'id " + id + " n'existe pas")
    );
  }

  @Override
  public List<TestModuleBranchDTO> findAll() {
      //return ((List<TestModule>) repository.findAll()).stream().map(this::buildTestModuleLiteDto).collect(Collectors.toList());
      return null;
  }

  private LiteTestModuleDTO buildTestModuleLiteDto(TestModule testModule) {
    LiteTestModuleDTO dto = mapper.asLite(testModule);
    Set<LiteEvaluationTestDTO> evaluations = getAllEvaluationsForTestModule(testModule);
    dto.setEvaluations(evaluations);
    dto.setMoyenne(calculMoyenne(evaluations));
    return dto;
  }

  @Override
  public Page<LiteTestModuleDTO> findAll(Pageable pageable) {
    Page<TestModule> entityPage = repository.findAll(pageable);
    List<TestModule> entities = entityPage.getContent();
    return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
  }

  @Override
  public void update(TestModuleRequestDTO dto, Long id) {
    TestModule exist = findById(id);
    dto.setId(exist.getId());
    buildTestModuleDto(repository.save(mapper.asEntity(dto)));
  }

  @Override
  public boolean existsByCode(String code) {
    return repository.findByCode(code).isPresent();
  }

  @Override
  public boolean equalsByDto(TestModuleRequestDTO dto, Long id) {
    TestModule ressource = repository.findByCode(dto.getCode()).orElse(null);
    if (ressource == null) return false;
    return !ressource.getId().equals(id);
  }

  @Override
  public TestModule findByCode(String code) {
    return repository.findByCode(code).orElseThrow(
            () ->  new ResourceNotFoundException("Le test module avec le code " + code + " n'existe pas")
    );
  }

  @Override
  public List<TestModuleForNoteReponseDTO> getAllTestBySession(Long sessionId, Long moduleId) {
    return buildTestModuleForNoteDto(sessionId, moduleId);
  }

  private List<TestModuleForNoteReponseDTO> buildTestModuleForNoteDto(Long sessionId, Long moduleId) {
    List<TestModuleForNoteReponseDTO> listTestModuleDto = new ArrayList<>();
    List<TestModule> listTestModule = repository.findAllBySessionId(sessionId);
    for (TestModule testModule : listTestModule) {
      List<EvaluationTest> evaluations = evaluationTestRepository.findAllByTestModuleIdAndModuleId(testModule.getId(), moduleId);
      if (evaluations.isEmpty()) {
        continue;
      }
      for (EvaluationTest evaluation : evaluations) {
        listTestModuleDto.add(buildTestModuleForNoteDto(testModule, evaluation));
      }
    }
    return listTestModuleDto;
  }

  @Override
  public List<TestModuleForNoteReponseDTO> saisieNotesTest(List<TestModuleForNoteDTO> dtos) {
    List<TestModuleForNoteReponseDTO> listDto = new ArrayList<>();
    for (TestModuleForNoteDTO dto : dtos) {
      TestModule testModule = repository.findById(dto.getTestModuleId()).orElse(null);
      if (testModule != null) {
        EvaluationTest test = evaluationTestRepository.findById(dto.getEvaluation().getEvaluationTestId()).orElse(null);
        if (test != null) {
          test.setDateEvaluation(dto.getEvaluation().getDateEvaluation());
          test.setNote(dto.getEvaluation().getNote());
          test = evaluationTestRepository.save(test);
        }
        listDto.add(buildTestModuleForNoteDto(testModule, test));
      }
    }
    return listDto;
  }

  private TestModuleForNoteReponseDTO buildTestModuleForNoteDto(TestModule testModule, EvaluationTest evaluation) {
    TestModuleForNoteReponseDTO dto = new TestModuleForNoteReponseDTO(testModule);
    Set<LiteEvaluationTestDTO> evaluations = getAllEvaluationsForTestModule(testModule);
    LiteEvaluationTestDTO evaluationLite = new LiteEvaluationTestDTO(evaluation);
    evaluationLite.setModuleFormation(new LiteModuleFormationDTO(evaluation.getModuleFormation()));
    dto.setEvaluation(evaluationLite);
    LiteInscriptionForNoteDTO inscriptionLite = new LiteInscriptionForNoteDTO(testModule.getInscription());
    inscriptionLite.setEtudiant(new LiteEtudiantForNoteDTO(testModule.getInscription().getEtudiant()));
    dto.setInscription(inscriptionLite);
    dto.setMoyenne(calculMoyenne(evaluations));
    return dto;
  }

  private Float calculMoyenne(Set<LiteEvaluationTestDTO> evaluations) {
    Float moyenne = 0F;
    for (LiteEvaluationTestDTO test : evaluations) {
      moyenne += test.getNote();
    }
    return !evaluations.isEmpty() ? moyenne/evaluations.size() : 0;
  }
}
