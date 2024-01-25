package net.ktccenter.campusApi.service.administration.impl;

import net.ktccenter.campusApi.dao.administration.CampusRepository;
import net.ktccenter.campusApi.dao.administration.SalleRepository;
import net.ktccenter.campusApi.dto.importation.administration.ImportCampusRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteSalleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.CampusDTO;
import net.ktccenter.campusApi.dto.request.administration.CampusRequestDTO;
import net.ktccenter.campusApi.entities.administration.Campus;
import net.ktccenter.campusApi.entities.administration.Salle;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.administration.CampusMapper;
import net.ktccenter.campusApi.service.administration.CampusService;
import net.ktccenter.campusApi.service.administration.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CampusServiceImpl implements CampusService {
  private final CampusRepository repository;
  private final CampusMapper mapper;
  private final SalleRepository salleRepository;
  private final UserService userService;

  public CampusServiceImpl(CampusRepository repository, CampusMapper mapper, SalleRepository salleRepository, UserService userService) {
    this.repository = repository;
    this.mapper = mapper;
    this.salleRepository = salleRepository;
    this.userService = userService;
  }

  @Override
  public CampusDTO save(CampusRequestDTO dto) {
    return buildCampusDto(repository.save(mapper.asEntity(dto)));
  }

  private CampusDTO buildCampusDto(Campus campus) {
    CampusDTO dto = mapper.asDTO(campus);
    dto.setSalles(getSallesForCampus(campus));
    return dto;
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
    if (!getSallesForCampus(campus).isEmpty())
        throw new ResourceNotFoundException("La campus avec l'id " + id + " ne peut pas etre supprimée car elle est utilisée par d'autre ressource");
    repository.deleteById(id);
  }

  private Set<LiteSalleDTO> getSallesForCampus(Campus campus) {
    return salleRepository.findAllByCampus(campus).stream().map(this::buildSalleLiteDto).collect(Collectors.toSet());
  }

  private LiteSalleDTO buildSalleLiteDto(Salle salle) {
    LiteSalleDTO lite = new LiteSalleDTO();
    lite.setId(salle.getId());
    lite.setCode(salle.getCode());
    lite.setLibelle(salle.getLibelle());
    lite.setCapacite(salle.getCapacite());
    return lite;
  }

  @Override
  public CampusDTO getOne(Long id) {
    return buildCampusDto(findById(id));
  }

  @Override
  public Campus findById(Long id) {
    Long currentBrancheId = userService.getCurrentBranche().getId();
    if (userService.hasGrantAuthorized()) {
      return repository.findById(id).orElseThrow(
              () -> new ResourceNotFoundException("Le campus avec l'id " + id + " n'existe pas")
      );
    } else {
      return repository.findById(id).filter(c -> c.getBranche().getId().equals(currentBrancheId)).orElseThrow(
              () -> new ResourceNotFoundException("Le campus avec l'id " + id + " n'existe pas")
      );
    }
  }

  @Override
  public List<LiteCampusDTO> findAll() {
    Long currentBrancheId = userService.getCurrentBranche().getId();
    if (userService.hasGrantAuthorized()) {
      return ((List<Campus>) repository.findAll()).stream().map(mapper::asLite).collect(Collectors.toList());
    } else {
      return ((List<Campus>) repository.findAll())
              .stream()
              .filter(c -> c.getBranche().getId().equals(currentBrancheId))
              .map(mapper::asLite)
              .collect(Collectors.toList());
    }
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
    return buildCampusDto(repository.save(mapper.asEntity(dto)));
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
}
