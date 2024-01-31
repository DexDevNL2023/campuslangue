package net.ktccenter.campusApi.service.scolarite.impl;

import net.ktccenter.campusApi.dao.cours.EvaluationTestRepository;
import net.ktccenter.campusApi.dao.scolarite.ModuleFormationRepository;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportModuleFormationRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteModuleFormationDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.ModuleFormationDTO;
import net.ktccenter.campusApi.dto.request.scolarite.ModuleFormationRequestDTO;
import net.ktccenter.campusApi.entities.cours.EvaluationTest;
import net.ktccenter.campusApi.entities.scolarite.ModuleFormation;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.scolarite.ModuleFormationMapper;
import net.ktccenter.campusApi.service.scolarite.ModuleFormationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ModuleFormationServiceImpl implements ModuleFormationService {
  private final ModuleFormationRepository repository;
  private final ModuleFormationMapper mapper;
  private final EvaluationTestRepository evaluationTestRepository;

  public ModuleFormationServiceImpl(ModuleFormationRepository repository, ModuleFormationMapper mapper, EvaluationTestRepository evaluationTestRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.evaluationTestRepository = evaluationTestRepository;
  }

  @Override
  public ModuleFormationDTO save(ModuleFormationRequestDTO dto) {
    return mapper.asDTO(repository.save(mapper.asEntity(dto)));
  }

  @Override
  public List<LiteModuleFormationDTO> save(List<ImportModuleFormationRequestDTO> dtos) {
    return  ((List<ModuleFormation>) repository.saveAll(mapper.asEntityList(dtos)))
            .stream()
            .map(mapper::asLite)
            .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    ModuleFormation moduleFormation = findById(id);
    if (!getAllTestsForModuleFormation(moduleFormation).isEmpty())
      throw new ResourceNotFoundException("Le module formation avec l'id " + id + " ne peut pas etre supprimé car elle est utilisé par d'autre ressource");
    repository.delete(moduleFormation);
  }

  private Set<EvaluationTest> getAllTestsForModuleFormation(ModuleFormation moduleFormation) {
    return new HashSet<>(evaluationTestRepository.findAllByModuleFormation(moduleFormation));
  }

  @Override
  public ModuleFormation findById(Long id) {
    return repository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Le module formation avec l'id " + id + " n'existe pas")
    );
  }

  @Override
  public ModuleFormationDTO getOne(Long id) {
    return mapper.asDTO(findById(id));
  }

  @Override
  public List<LiteModuleFormationDTO> findAll() {
    return ((List<ModuleFormation>) repository.findAll()).stream().map(mapper::asLite).collect(Collectors.toList());
  }

  @Override
  public Page<LiteModuleFormationDTO> findAll(Pageable pageable) {
    Page<ModuleFormation> entityPage = repository.findAll(pageable);
    List<ModuleFormation> entities = entityPage.getContent();
    return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
  }


  @Override
  public ModuleFormationDTO update(ModuleFormationRequestDTO dto, Long id) {
    ModuleFormation exist = findById(id);
    dto.setId(exist.getId());
    return mapper.asDTO(repository.save(mapper.asEntity(dto)));
  }


  @Override
  public boolean existByCode(String code) {
    return repository.findByCode(code).isPresent();
  }

  @Override
  public boolean equalsByDto(ModuleFormationRequestDTO dto, Long id) {
    ModuleFormation ressource = repository.findByCode(dto.getCode()).orElse(null);
    if (ressource == null) return false;
    return !ressource.getId().equals(id);
  }

  @Override
  public ModuleFormation findByCode(String code) {
    return repository.findByCode(code).orElseThrow(
            () ->  new ResourceNotFoundException("Le module formation avec le code " + code + " n'existe pas")
    );
  }

  @Override
  public ModuleFormation findByCodeAndLibelle(String code, String libelle) {
    return repository.findByCodeAndLibelle(code, libelle).orElseThrow(
            () ->  new ResourceNotFoundException("Le module formation avec le code " + code + " et le libelle "+ libelle +" n'existe pas")
    );
  }
}
