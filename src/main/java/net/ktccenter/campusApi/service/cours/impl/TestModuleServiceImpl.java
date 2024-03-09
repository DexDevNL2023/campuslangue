package net.ktccenter.campusApi.service.cours.impl;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.dao.cours.EvaluationTestRepository;
import net.ktccenter.campusApi.dao.cours.TestModuleRepository;
import net.ktccenter.campusApi.dao.scolarite.ModuleFormationRepository;
import net.ktccenter.campusApi.dao.scolarite.NiveauRepository;
import net.ktccenter.campusApi.dto.importation.cours.ImportTestModuleRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteEvaluationTestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteTestModuleDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteModuleFormationDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteNiveauForSessionDTO;
import net.ktccenter.campusApi.dto.reponse.branch.TestModuleBranchDTO;
import net.ktccenter.campusApi.dto.reponse.cours.TestModuleDTO;
import net.ktccenter.campusApi.dto.reponse.cours.TestModuleForNoteReponseDTO;
import net.ktccenter.campusApi.dto.reponse.cours.TestModuleForResultatReponseDTO;
import net.ktccenter.campusApi.dto.request.cours.*;
import net.ktccenter.campusApi.entities.administration.Branche;
import net.ktccenter.campusApi.entities.cours.EvaluationTest;
import net.ktccenter.campusApi.entities.cours.TestModule;
import net.ktccenter.campusApi.entities.scolarite.Etudiant;
import net.ktccenter.campusApi.entities.scolarite.ModuleFormation;
import net.ktccenter.campusApi.entities.scolarite.Niveau;
import net.ktccenter.campusApi.enums.ResultatFilter;
import net.ktccenter.campusApi.enums.ResultatState;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.cours.TestModuleMapper;
import net.ktccenter.campusApi.service.MainService;
import net.ktccenter.campusApi.service.cours.TestModuleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class TestModuleServiceImpl extends MainService implements TestModuleService {
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
        test.setNote(0F);
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

  private LiteEvaluationTestDTO buildEvaluationLiteDto(EvaluationTest entity) {
    LiteEvaluationTestDTO lite = new LiteEvaluationTestDTO(entity);
    LiteModuleFormationDTO liteModuleFormationDTO = new LiteModuleFormationDTO(entity.getModuleFormation());
    liteModuleFormationDTO.setNiveau(new LiteNiveauForSessionDTO(entity.getModuleFormation().getNiveau()));
    lite.setModuleFormation(liteModuleFormationDTO);
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
    List<TestModule> tests = (List<TestModule>) repository.findAll();
    List<TestModuleBranchDTO> result = new ArrayList<>();
    if (hasGrantAuthorized()) {
      for (Branche b : getAllBranches()) {
        result.add(buildData(b, tests));
      }
    } else {
      result.add(buildData(getCurrentUserBranch(), tests));
    }
    return result;
  }

  private TestModuleBranchDTO buildData(Branche branche, List<TestModule> tests) {
    TestModuleBranchDTO dto = new TestModuleBranchDTO();
    dto.setBranche(brancheMapper.asLite(branche));
    dto.setData(tests.stream()
            .filter(e -> belongsToTheCurrentBranch(branche, e))
            .map(mapper::asLite)
            .collect(Collectors.toList()));
    return dto;
  }

  private boolean belongsToTheCurrentBranch(Branche branche, TestModule e) {
    return e.getInscription().getSession().getBranche().getId().equals(branche.getId());
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
  public TestModuleDTO update(TestModuleRequestDTO dto, Long id) {
    TestModule exist = findById(id);
    dto.setId(exist.getId());
    return buildTestModuleDto(repository.save(mapper.asEntity(dto)));
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
  public FullTestModuleForNoteDTO getAllTestBySession(Long sessionId, Long moduleId) {
    return buildTestModuleForNoteDto(sessionId, moduleId);
  }

  private FullTestModuleForNoteDTO buildTestModuleForNoteDto(Long sessionId, Long moduleId) {
    Date dateEvaluation = new Date();
    List<TestModuleForNoteDTO> listTestModuleDto = new ArrayList<>();
    List<TestModule> listTestModule = repository.findAllBySessionId(sessionId);
    for (TestModule testModule : listTestModule) {
      List<EvaluationTest> evaluations = evaluationTestRepository.findAllByTestModuleIdAndModuleId(testModule.getId(), moduleId);
      if (evaluations.isEmpty()) {
        continue;
      }
      for (EvaluationTest evaluation : evaluations) {
        dateEvaluation = evaluation.getDateEvaluation();
        listTestModuleDto.add(buildTestModuleForNoteDto(testModule, evaluation));
      }
    }
    return new FullTestModuleForNoteDTO(dateEvaluation, listTestModuleDto);
  }

  @Override
  public List<TestModuleForNoteReponseDTO> saisieNotesTest(FullTestModuleForNoteDTO dto) {
    List<TestModuleForNoteReponseDTO> listDto = new ArrayList<>();
    List<TestModuleForNoteDTO> listTestModuleDto = dto.getTestModules();
    for (TestModuleForNoteDTO testDto : listTestModuleDto) {
      TestModule testModule = repository.findById(testDto.getTestModuleId()).orElse(null);
      if (testModule != null) {
        EvaluationTest test = evaluationTestRepository.findById(testDto.getEvaluation().getEvaluationTestId()).orElse(null);
        if (test != null) {
          test.setDateEvaluation(dto.getDateEvaluation());
          test.setNote(testDto.getEvaluation().getNote());
          test = evaluationTestRepository.save(test);
        }
        listDto.add(buildTestModuleForNoteReponseDto(testModule, test));
      }
    }
    return listDto;
  }

  @Override
  public void importNotesTest(FullTestModuleForNoteImportDTO dto) {
    List<EvaluationTestForNoteImportDTO> listEvaluationTestDto = dto.getEvaluations();
    for (EvaluationTestForNoteImportDTO evaluationTestDto : listEvaluationTestDto) {
      EvaluationTest test = evaluationTestRepository.findById(evaluationTestDto.getEvaluationTestId()).orElse(null);
      if (test != null) {
        test.setDateEvaluation(dto.getDateEvaluation());
        test.setNote(evaluationTestDto.getNote());
        evaluationTestRepository.save(test);
      }
    }
  }

  @Override
  public List<TestModuleForResultatReponseDTO> getAllResultatTestBySession(Long sessionId, Long moduleId, ResultatState state, ResultatFilter order) {
    if (order == ResultatFilter.ALPHABETIQUE) {
      return repository.findAllBySessionId(sessionId)
              .stream()
              .filter(t -> getByAppreciation(t, state))
              .map(t -> buildTestModuleResultatDto(t, moduleId, state))
              .sorted(Comparator.comparing(TestModuleForResultatReponseDTO::getFullName))
              .collect(Collectors.toList());
    } else {
      return repository.findAllBySessionId(sessionId)
              .stream()
              .filter(t -> getByAppreciation(t, state))
              .map(t -> buildTestModuleResultatDto(t, moduleId, state))
              .sorted(Comparator.comparingInt(t -> t.getMoyenne().intValue()))
              .collect(Collectors.toList());
    }
  }

  private boolean getByAppreciation(TestModule testModule, ResultatState state) {
    List<EvaluationTest> evaluations = evaluationTestRepository.findAllByTestModule(testModule);
    Float moyenne = 0F;
    boolean isFail = false;
    for (EvaluationTest evaluation : evaluations) {
      moyenne += evaluation.getNote();
    }
    moyenne = !evaluations.isEmpty() && !isFail ? moyenne / evaluations.size() : 0;
    int note = moyenne.intValue();
    switch (state) {
      case ALL:
        return true;
      case WIN:
        return (note >= 10);
      case FAIL:
        return (note < 10);
      default:
        return false;
    }
  }

  private TestModuleForResultatReponseDTO buildTestModuleResultatDto(TestModule testModule, Long moduleId, ResultatState state) {
    Set<EvaluationTestForNoteDTO> listEvaluationDto = new HashSet<>();
    TestModuleForResultatReponseDTO dto = new TestModuleForResultatReponseDTO();
    dto.setMatricule(testModule.getInscription().getEtudiant().getMatricule());
    dto.setFullName(getFullName(testModule.getInscription().getEtudiant()));
    List<EvaluationTest> evaluations = evaluationTestRepository.findAllByTestModuleIdAndModuleId(testModule.getId(), moduleId);
    for (EvaluationTest evaluation : evaluations) {
      listEvaluationDto.add(buildEvaluationForResultatDto(evaluation));
    }
    dto.setEvaluations(listEvaluationDto);
    return dto;
  }

  private EvaluationTestForNoteDTO buildEvaluationForResultatDto(EvaluationTest evaluation) {
    EvaluationTestForNoteDTO evaluationLite = new EvaluationTestForNoteDTO(evaluation);
    evaluationLite.setModuleFormationCode(evaluation.getModuleFormation().getCode());
    return evaluationLite;
  }

  public String getFullName(Etudiant etudiant) {
    return !etudiant.getPrenom().isEmpty() ? etudiant.getNom() + " " + etudiant.getPrenom() : etudiant.getNom();
  }

  private TestModuleForNoteDTO buildTestModuleForNoteDto(TestModule testModule, EvaluationTest evaluation) {
    TestModuleForNoteDTO dto = new TestModuleForNoteDTO(testModule);
    EvaluationTestForNoteDTO evaluationLite = new EvaluationTestForNoteDTO(evaluation);
    evaluationLite.setModuleFormationCode(evaluation.getModuleFormation().getCode());
    dto.setEvaluation(evaluationLite);
    dto.setMatricule(testModule.getInscription().getEtudiant().getMatricule());
    dto.setNom(testModule.getInscription().getEtudiant().getNom());
    dto.setPrenom(testModule.getInscription().getEtudiant().getPrenom());
    return dto;
  }

  private TestModuleForNoteReponseDTO buildTestModuleForNoteReponseDto(TestModule testModule, EvaluationTest evaluation) {
    TestModuleForNoteReponseDTO dto = new TestModuleForNoteReponseDTO(testModule);
    LiteEvaluationTestDTO evaluationLite = new LiteEvaluationTestDTO(evaluation);
    evaluationLite.setModuleFormation(new LiteModuleFormationDTO(evaluation.getModuleFormation()));
    dto.setEvaluation(evaluationLite);
    dto.setMatricule(testModule.getInscription().getEtudiant().getMatricule());
    dto.setNom(testModule.getInscription().getEtudiant().getNom());
    dto.setPrenom(testModule.getInscription().getEtudiant().getPrenom());
    dto.setMoyenne(calculMoyenne(getAllEvaluationsForTestModule(testModule)));
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
