package net.ktccenter.campusApi.service.cours.impl;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.dao.cours.EvaluationTestRepository;
import net.ktccenter.campusApi.dto.importation.cours.ImportEvaluationTestRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteEvaluationTestDTO;
import net.ktccenter.campusApi.dto.reponse.branch.EvaluationTestBranchDTO;
import net.ktccenter.campusApi.dto.reponse.cours.EvaluationTestDTO;
import net.ktccenter.campusApi.dto.request.cours.EvaluationTestRequestDTO;
import net.ktccenter.campusApi.entities.administration.Branche;
import net.ktccenter.campusApi.entities.cours.EvaluationTest;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.cours.EvaluationTestMapper;
import net.ktccenter.campusApi.service.MainService;
import net.ktccenter.campusApi.service.cours.EvaluationTestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class EvaluationTestServiceImpl extends MainService implements EvaluationTestService {
  private final EvaluationTestRepository repository;
  private final EvaluationTestMapper mapper;

  public EvaluationTestServiceImpl(EvaluationTestRepository repository, EvaluationTestMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public EvaluationTestDTO save(EvaluationTestRequestDTO dto) {
    return mapper.asDTO(repository.save(mapper.asEntity(dto)));
  }

  @Override
  public List<LiteEvaluationTestDTO> save(List<ImportEvaluationTestRequestDTO> dtos) {
    return  ((List<EvaluationTest>) repository.saveAll(mapper.asEntityList(dtos)))
            .stream()
            .map(mapper::asLite)
            .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    repository.deleteById(id);
  }

  @Override
  public EvaluationTestDTO getOne(Long id) {
    return mapper.asDTO(findById(id));
  }

  @Override
  public EvaluationTest findById(Long id) {
    return repository.findById(id).orElseThrow(
            () ->  new ResourceNotFoundException("L'evaluation test avec l'id " + id + " n'existe pas")
    );
  }

  @Override
  public List<EvaluationTestBranchDTO> findAll() {
    List<EvaluationTest> evaluations = (List<EvaluationTest>) repository.findAll();
    List<EvaluationTestBranchDTO> result = new ArrayList<>();
    if (hasGrantAuthorized()) {
      for (Branche b : getAllBranches()) {
        result.add(buildData(b, evaluations));
      }
    } else {
      result.add(buildData(getCurrentUserBranch(), evaluations));
    }
    return result;
  }

  private EvaluationTestBranchDTO buildData(Branche branche, List<EvaluationTest> evaluations) {
    EvaluationTestBranchDTO dto = new EvaluationTestBranchDTO();
    dto.setBranche(brancheMapper.asLite(branche));
    dto.setData(evaluations.stream()
            .filter(e -> belongsToTheCurrentBranch(branche, e))
            .map(mapper::asLite)
            .collect(Collectors.toList()));
    return dto;
  }

  private boolean belongsToTheCurrentBranch(Branche branche, EvaluationTest e) {
    return e.getTestModule().getInscription().getSession().getBranche().getId().equals(branche.getId());
  }

  @Override
  public Page<LiteEvaluationTestDTO> findAll(Pageable pageable) {
    Page<EvaluationTest> entityPage = repository.findAll(pageable);
    List<EvaluationTest> entities = entityPage.getContent();
    return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
  }

  @Override
  public void update(EvaluationTestRequestDTO dto, Long id) {
    EvaluationTest exist =  findById(id);
    dto.setId(exist.getId());
    mapper.asDTO(repository.save(mapper.asEntity(dto)));
  }

  @Override
  public boolean equalsByDto(EvaluationTestRequestDTO dto, Long id) {
    EvaluationTest ressource = repository.findById(id).orElse(null);
    if (ressource == null) return false;
    return !ressource.equals(dto);
  }
}