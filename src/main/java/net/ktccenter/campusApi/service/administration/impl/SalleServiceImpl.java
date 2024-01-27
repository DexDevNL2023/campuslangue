package net.ktccenter.campusApi.service.administration.impl;

import net.ktccenter.campusApi.dao.administration.OccupationSalleRepository;
import net.ktccenter.campusApi.dao.administration.SalleRepository;
import net.ktccenter.campusApi.dao.cours.PlageHoraireRepository;
import net.ktccenter.campusApi.dto.importation.administration.ImportSalleRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusForSalleDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteOccupationSalleDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteSalleDTO;
import net.ktccenter.campusApi.dto.lite.cours.LitePlageHoraireDTO;
import net.ktccenter.campusApi.dto.reponse.administration.OccupationSalleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.SalleDTO;
import net.ktccenter.campusApi.dto.reponse.branch.SalleBranchDTO;
import net.ktccenter.campusApi.dto.request.administration.OccupationSalleRequestDTO;
import net.ktccenter.campusApi.dto.request.administration.SalleRequestDTO;
import net.ktccenter.campusApi.entities.administration.Branche;
import net.ktccenter.campusApi.entities.administration.OccupationSalle;
import net.ktccenter.campusApi.entities.administration.Salle;
import net.ktccenter.campusApi.entities.cours.PlageHoraire;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.administration.SalleMapper;
import net.ktccenter.campusApi.service.MainService;
import net.ktccenter.campusApi.service.administration.SalleService;
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
public class SalleServiceImpl extends MainService implements SalleService {
  private final SalleRepository repository;
  private final SalleMapper mapper;
  private final PlageHoraireRepository plageHoraireRepository;
  private final OccupationSalleRepository occupationSalleRepository;

  public SalleServiceImpl(SalleRepository repository, SalleMapper mapper, PlageHoraireRepository plageHoraireRepository, OccupationSalleRepository occupationSalleRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.plageHoraireRepository = plageHoraireRepository;
    this.occupationSalleRepository = occupationSalleRepository;
  }

  @Override
  public SalleDTO save(SalleRequestDTO dto) {
    Salle salle = repository.save(mapper.asEntity(dto));
    if (getOccupationsForSalle(salle).isEmpty()) {
      List<PlageHoraire> plages = (List<PlageHoraire>) plageHoraireRepository.findAll();
      ajouteOccupation(salle, plages);
    }
    return buildSalleDto(salle);
  }

  private SalleDTO buildSalleDto(Salle salle) {
    SalleDTO dto = mapper.asDTO(salle);
    dto.setOccupations(getOccupationsForSalle(salle));
    return dto;
  }

  private void ajouteOccupation(Salle salle, List<PlageHoraire> list) {
    for (PlageHoraire plage : list) {
      occupationSalleRepository.save(new OccupationSalle(salle.getCode()+"-"+plage.getCode(),false, plage, salle));
    }
  }

  @Override
  public List<LiteSalleDTO> save(List<ImportSalleRequestDTO> dtos) {
    List<Salle> list = (List<Salle>) repository.saveAll(mapper.asEntityList(dtos));
    for (Salle salle : list) {
      if (getOccupationsForSalle(salle).isEmpty()) {
        List<PlageHoraire> plages = (List<PlageHoraire>) plageHoraireRepository.findAll();
        ajouteOccupation(salle, plages);
      }
    }
    return  list
            .stream()
            .map(mapper::asLite)
            .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    Salle salle = findById(id);
    if (!getOccupationsForSalle(salle).isEmpty())
        throw new ResourceNotFoundException("La salle avec l'id " + id + " ne peut pas etre supprimée car elle est utilisée par d'autre ressource");
    repository.deleteById(id);
  }

  private Set<LiteOccupationSalleDTO> getOccupationsForSalle(Salle salle) {
    return occupationSalleRepository.findAllBySalle(salle).stream().map(this::buildOccupationLiteDto).collect(Collectors.toSet());
  }

  private LiteOccupationSalleDTO buildOccupationLiteDto(OccupationSalle occupation) {
    LiteOccupationSalleDTO lite = new LiteOccupationSalleDTO(occupation);
    lite.setPlageHoraire(new LitePlageHoraireDTO(occupation.getPlageHoraire()));
    return lite;
  }

  @Override
  public SalleDTO getOne(Long id) {
    return buildSalleDto(findById(id));
  }

  @Override
  public Salle findById(Long id) {
    return repository.findById(id).orElseThrow(
      () ->  new ResourceNotFoundException("La salle avec l'id " + id + " n'existe pas")
    );
  }



  @Override
  public Page<LiteSalleDTO> findAll(Pageable pageable) {
    Page<Salle> entityPage = repository.findAll(pageable);
    List<Salle> entities = entityPage.getContent();
    return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
  }

  @Override
  public void update(SalleRequestDTO dto, Long id) {
    Salle exist = findById(id);
    dto.setId(exist.getId());
    exist = repository.save(mapper.asEntity(dto));
    if (getOccupationsForSalle(exist).isEmpty()) {
      List<PlageHoraire> plages = (List<PlageHoraire>) plageHoraireRepository.findAll();
      ajouteOccupation(exist, plages);
    }

  }

  @Override
  public OccupationSalleDTO changeOccupation(OccupationSalleRequestDTO dto) {
    Salle salle = repository.findById(dto.getSalleId()).orElseThrow(
            () ->  new ResourceNotFoundException("La salle avec l'id " + dto.getSalleId() + " n'existe pas")
    );
    PlageHoraire plage = plageHoraireRepository.findById(dto.getPlageHoraireId()).orElseThrow(
            () ->  new ResourceNotFoundException("La plage avec l'id " + dto.getPlageHoraireId() + " n'existe pas")
    );
    OccupationSalle occupation = occupationSalleRepository.findById(dto.getId()).orElseThrow(
            () ->  new ResourceNotFoundException("L'occupation avec l'id " + dto.getId() + " n'existe pas")
    );
    // On met l'objet à jour
    occupation.setSalle(salle);
    occupation.setPlageHoraire(plage);
    occupation.setEstOccupee(dto.getEstOccupee());
    occupation = occupationSalleRepository.save(occupation);
    // On construit le dto reponse
    return construitDtoOccupation(occupation);
  }

  private OccupationSalleDTO construitDtoOccupation(OccupationSalle occupation) {
    OccupationSalleDTO dto = new OccupationSalleDTO(occupation);
    dto.setPlageHoraire(new LitePlageHoraireDTO(occupation.getPlageHoraire()));
    LiteSalleDTO liteSalle = new LiteSalleDTO(occupation.getSalle());
    liteSalle.setCampus(new LiteCampusForSalleDTO(occupation.getSalle().getCampus()));
    dto.setSalle(liteSalle);
    return dto;
  }

  @Override
  public OccupationSalleDTO getOccupationById(Long occupationId) {
    OccupationSalle occupation = occupationSalleRepository.findById(occupationId).orElseThrow(
            () ->  new ResourceNotFoundException("L'occupation avec l'id " + occupationId + " n'existe pas")
    );
    // On construit le dto reponse
    return construitDtoOccupation(occupation);
  }

  @Override
  public OccupationSalleDTO getOccupationByCode(String occupationCode) {
    OccupationSalle occupation = occupationSalleRepository.findByCode(occupationCode).orElseThrow(
            () ->  new ResourceNotFoundException("L'occupation avec le code " + occupationCode + " n'existe pas")
    );
    // On construit le dto reponse
    return construitDtoOccupation(occupation);
  }

  @Override
  public List<LiteOccupationSalleDTO> getPlageDisponible(Long salleId) {
    Salle salle = repository.findById(salleId).orElseThrow(
            () ->  new ResourceNotFoundException("La salle avec l'id " + salleId + " n'existe pas")
    );
    return occupationSalleRepository.findAllBySalleAndEstOccupee(salle, false).stream().map(this::buildOccupationLiteDto).collect(Collectors.toList());
  }

  @Override
  public List<LiteOccupationSalleDTO> getPlannigSalle(Long salleId) {
    Salle salle = repository.findById(salleId).orElseThrow(
            () ->  new ResourceNotFoundException("La salle avec l'id " + salleId + " n'existe pas")
    );
    return occupationSalleRepository.findAllBySalle(salle).stream().map(this::buildOccupationLiteDto).collect(Collectors.toList());
  }

    @Override
    public boolean existsByCodeAndLibelle(String code, String libelle) {
        return repository.findByCodeAndLibelle(code, libelle).isPresent();
    }

    @Override
    public boolean equalsByDto(SalleRequestDTO dto, Long id) {
        Salle ressource = repository.findByCodeAndLibelle(dto.getCode(), dto.getLibelle()).orElse(null);
        if (ressource == null) return false;
        return !ressource.getId().equals(id);
    }

  @Override
  public List<SalleBranchDTO> findAll() {
    List<SalleBranchDTO> result = new ArrayList<>();
    if (hasGrantAuthorized()) {
      for (Branche b : getAllBranches()) {
        result.add(buildData(b));
      }
    } else {
      result.add(this.buildData(getCurrentUserBranch()));
    }
    return result;
  }

  SalleBranchDTO buildData(Branche branche) {
    SalleBranchDTO dto = new SalleBranchDTO();
    dto.setBranche(brancheMapper.asLite(branche));
    dto.setData(mapper.asDTOList(repository.findAllByBranche(branche)));
    return dto;
  }

  @Override
  public Salle findByCode(String code) {
    return repository.findByCode(code).orElseThrow(
            () ->  new ResourceNotFoundException("La salle avec le code " + code + " n'existe pas")
    );
  }
}
