package net.ktccenter.campusApi.service.scolarite.impl;

import net.ktccenter.campusApi.dao.scolarite.SessionRepository;
import net.ktccenter.campusApi.dao.scolarite.VagueRepository;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportVagueRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteSessionDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteVagueDTO;
import net.ktccenter.campusApi.dto.reponse.branch.VagueBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.VagueDTO;
import net.ktccenter.campusApi.dto.request.scolarite.VagueRequestDTO;
import net.ktccenter.campusApi.entities.administration.Branche;
import net.ktccenter.campusApi.entities.scolarite.Session;
import net.ktccenter.campusApi.entities.scolarite.Vague;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.scolarite.VagueMapper;
import net.ktccenter.campusApi.service.MainService;
import net.ktccenter.campusApi.service.scolarite.VagueService;
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
public class VagueServiceImpl extends MainService implements VagueService {
  private final VagueRepository repository;
  private final VagueMapper mapper;
  private final SessionRepository sessionRepository;

  public VagueServiceImpl(VagueRepository repository, VagueMapper mapper, SessionRepository sessionRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.sessionRepository = sessionRepository;
  }

  @Override
  public VagueDTO save(VagueRequestDTO dto) {
    return buildVagueDto(repository.save(mapper.asEntity(dto)));
  }

  private VagueDTO buildVagueDto(Vague vague) {
    VagueDTO dto = mapper.asDTO(vague);
    dto.setSessions(getAllSessionsForVague(vague));
    return dto;
  }

  @Override
  public List<LiteVagueDTO> save(List<ImportVagueRequestDTO> dtos) {
    return  ((List<Vague>) repository.saveAll(mapper.asEntityList(dtos)))
            .stream()
            .map(mapper::asLite)
            .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    Vague vague = findById(id);
    if (!getAllSessionsForVague(vague).isEmpty())
      throw new ResourceNotFoundException("La vague avec l'id " + id + " ne peut pas etre supprimé car elle est utilisé par d'autre ressource");
    repository.delete(vague);
  }

  private Set<LiteSessionDTO> getAllSessionsForVague(Vague vague) {
    return sessionRepository.findAllByVague(vague).stream().map(this::buildSessionLiteDto).collect(Collectors.toSet());
  }

  private LiteSessionDTO buildSessionLiteDto(Session session) {
    LiteSessionDTO lite = new LiteSessionDTO(session);
    lite.setBranche(new LiteBrancheDTO(session.getBranche()));
    //lite.setNiveau(new LiteNiveauDTO(session.getNiveau()));
    //lite.setFormateur(new LiteFormateurDTO(session.getFormateur()));
    return lite;
  }

  @Override
  public Vague findById(Long id) {
    return repository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("La vague avec l'id " + id + " n'existe pas")
    );
  }

  @Override
  public VagueDTO getOne(Long id) {
    return buildVagueDto(findById(id));
  }

  @Override
  public List<VagueBranchDTO> findAll() {
    List<Vague> vagues = (List<Vague>) repository.findAll();
    List<VagueBranchDTO> result = new ArrayList<>();
    if (hasGrantAuthorized()) {
      for (Branche b : getAllBranches()) {
        result.add(buildData(b, vagues));
      }
    } else {
      result.add(buildData(getCurrentUserBranch(), vagues));
    }
    return result;
  }

  private VagueBranchDTO buildData(Branche branche, List<Vague> vagues) {
    VagueBranchDTO dto = new VagueBranchDTO();
    dto.setBranche(brancheMapper.asLite(branche));
    dto.setData(vagues.stream()
            .filter(e -> belongsToTheCurrentBranch(branche, e))
            .map(mapper::asLite)
            .collect(Collectors.toList()));
    return dto;
  }

  private boolean belongsToTheCurrentBranch(Branche branche, Vague e) {
    return e.getBranche().getId().equals(branche.getId());
  }

  @Override
  public Page<LiteVagueDTO> findAll(Pageable pageable) {
    Page<Vague> entityPage = repository.findAll(pageable);
    List<Vague> entities = entityPage.getContent();
    return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
  }

  @Override
  public VagueDTO update(VagueRequestDTO dto, Long id) {
    Vague exist = findById(id);
    dto.setId(exist.getId());
    return buildVagueDto(repository.save(mapper.asEntity(dto)));
  }

  @Override
  public boolean existByCode(String code) {
    return repository.findByCode(code).isPresent();
  }

  @Override
  public boolean equalsByDto(VagueRequestDTO dto, Long id) {
    Vague ressource = repository.findByCode(dto.getCode()).orElse(null);
    if (ressource == null) return false;
    return !ressource.getId().equals(id);
  }

  @Override
  public Vague findByCode(String code) {
    return repository.findByCode(code).orElseThrow(
            () ->  new ResourceNotFoundException("La vague avec le code " + code + " n'existe pas")
    );
  }
}