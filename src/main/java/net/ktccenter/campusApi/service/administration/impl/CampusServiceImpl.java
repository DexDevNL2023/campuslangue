package net.ktccenter.campusApi.service.administration.impl;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.dao.administration.BrancheRepository;
import net.ktccenter.campusApi.dao.administration.CampusRepository;
import net.ktccenter.campusApi.dao.administration.OccupationSalleRepository;
import net.ktccenter.campusApi.dao.administration.SalleRepository;
import net.ktccenter.campusApi.dto.importation.administration.ImportCampusRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteOccupationSalleDTO;
import net.ktccenter.campusApi.dto.lite.cours.LitePlageHoraireDTO;
import net.ktccenter.campusApi.dto.reponse.administration.CampusByBranchDTO;
import net.ktccenter.campusApi.dto.reponse.administration.CampusDTO;
import net.ktccenter.campusApi.dto.reponse.administration.SalleByBranchDTO;
import net.ktccenter.campusApi.dto.reponse.branch.CampusBranchDTO;
import net.ktccenter.campusApi.dto.request.administration.CampusRequestDTO;
import net.ktccenter.campusApi.entities.administration.Branche;
import net.ktccenter.campusApi.entities.administration.Campus;
import net.ktccenter.campusApi.entities.administration.OccupationSalle;
import net.ktccenter.campusApi.entities.administration.Salle;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CampusServiceImpl extends MainService implements CampusService {
  private final CampusRepository repository;
  private final CampusMapper mapper;
    public final BrancheRepository brancheRepository;
    private final SalleRepository salleRepository;
    private final OccupationSalleRepository occupationSalleRepository;


    public CampusServiceImpl(CampusRepository repository, CampusMapper mapper, BrancheRepository brancheRepository, SalleRepository salleRepository, OccupationSalleRepository occupationSalleRepository) {
    this.repository = repository;
    this.mapper = mapper;
        this.brancheRepository = brancheRepository;
        this.salleRepository = salleRepository;
        this.occupationSalleRepository = occupationSalleRepository;
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

    @Override
    public List<CampusByBranchDTO> findAllByBranch(Long branchId) {
        Branche branche = brancheRepository.findById(branchId).orElseThrow(
                () -> new ResourceNotFoundException("La branche avec l'id " + branchId + " n'existe pas")
        );
        List<CampusByBranchDTO> result = new ArrayList<>();
        List<Campus> campusList = repository.findAllByBranche(branche);
        for (Campus campus : campusList) {
            CampusByBranchDTO campusByBranchDTO = new CampusByBranchDTO(campus);
            campusByBranchDTO.setSalles(salleRepository.findAllByCampus(campus).stream().map(this::buildSalleDto).collect(Collectors.toSet()));
            result.add(campusByBranchDTO);
        }
        return result;
    }

    private SalleByBranchDTO buildSalleDto(Salle salle) {
        SalleByBranchDTO dto = new SalleByBranchDTO(salle);
        dto.setOccupations(getOccupationsForSalle(salle));
        return dto;
    }

    private Set<LiteOccupationSalleDTO> getOccupationsForSalle(Salle salle) {
        return occupationSalleRepository.findAllBySalleAndEstOccupee(salle, false).stream().map(this::buildOccupationLiteDto).collect(Collectors.toSet());
    }

    private LiteOccupationSalleDTO buildOccupationLiteDto(OccupationSalle occupation) {
        LiteOccupationSalleDTO lite = new LiteOccupationSalleDTO(occupation);
        lite.setPlageHoraire(new LitePlageHoraireDTO(occupation.getPlageHoraire()));
        return lite;
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
