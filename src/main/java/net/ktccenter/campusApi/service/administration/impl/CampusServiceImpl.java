package net.ktccenter.campusApi.service.administration.impl;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.dao.administration.CampusRepository;
import net.ktccenter.campusApi.dto.importation.administration.ImportCampusRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusDTO;
import net.ktccenter.campusApi.dto.reponse.administration.CampusDTO;
import net.ktccenter.campusApi.dto.reponse.branch.CampusBranchDTO;
import net.ktccenter.campusApi.dto.request.administration.CampusRequestDTO;
import net.ktccenter.campusApi.entities.administration.Branche;
import net.ktccenter.campusApi.entities.administration.Campus;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.administration.CampusMapper;
import net.ktccenter.campusApi.service.MainService;
import net.ktccenter.campusApi.service.administration.CampusService;
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
public class CampusServiceImpl extends MainService implements CampusService {
  private final CampusRepository repository;
  private final CampusMapper mapper;


  public CampusServiceImpl(CampusRepository repository, CampusMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public CampusDTO save(CampusRequestDTO dto) {
    return mapper.asDTO(repository.save(mapper.asEntity(dto)));
  }


  @Override
  public List<LiteCampusDTO> save(List<ImportCampusRequestDTO> dtos) {
    return  ((List<Campus>) repository.saveAll(mapper.asEntityList(dtos)))
            .stream()
            .map(mapper::asLite)
            .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    Campus campus = findById(id);
    if (!campus.getSalles().isEmpty()) {
      throw new ResourceNotFoundException("Ce campus " + id + " ne peut pas être supprimé car des salles y sont enrégistrées");
    }
    repository.deleteById(id);
  }

  @Override
  public CampusDTO getOne(Long id) {
    return mapper.asDTO(findById(id));
  }

  @Override
  public Campus findById(Long id) {
    return repository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Le campus avec l'id " + id + " n'existe pas")
    );
  }

  @Override
  public Page<LiteCampusDTO> findAll(Pageable pageable) {
    Page<Campus> entityPage = repository.findAll(pageable);
    List<Campus> entities = entityPage.getContent();
    return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
  }


  @Override
  public CampusDTO update(CampusRequestDTO dto, Long id) {
    Campus exist =  findById(id);
    dto.setId(exist.getId());
    return mapper.asDTO(repository.save(mapper.asEntity(dto)));
  }

  @Override
  public boolean existsByCodeAndLibelle(String code, String libelle) {
      return repository.findByCodeAndLibelle(code, libelle).isPresent();
  }

  @Override
  public boolean equalsByDto(CampusRequestDTO dto, Long id) {
      Campus ressource = repository.findByCodeAndLibelle(dto.getCode(), dto.getLibelle()).orElse(null);
      if (ressource == null) return false;
      return !ressource.getId().equals(id);
  }

  @Override
  public Campus findByCode(String code) {
    return repository.findByCode(code).orElseThrow(
            () ->  new ResourceNotFoundException("Le campus avec le code " + code + " n'existe pas")
    );
  }

  @Override
  public List<CampusBranchDTO> findAll() {
    log.info("1");
    List<CampusBranchDTO> result = new ArrayList<>();
    if (hasGrantAuthorized()) {
      log.info("2");
      for (Branche b : getAllBranches()) {
        result.add(buildData(b));
        log.info("3");
      }
    } else {
      result.add(this.buildData(getCurrentUserBranch()));
      log.info(getCurrentUserBranch().getCode());
    }
    return result;
  }

  private CampusBranchDTO buildData(Branche branche) {
    CampusBranchDTO dto = new CampusBranchDTO();
    log.info("5");
    dto.setBranche(brancheMapper.asLite(branche));
    log.info("6");
    dto.setData(mapper.asDTOList(repository.findAllByBranche(branche)));
    log.info("7");
    return dto;
  }
}
