package net.ktccenter.campusApi.service.scolarite.impl;

import net.ktccenter.campusApi.dao.scolarite.DiplomeRepository;
import net.ktccenter.campusApi.dao.scolarite.FormateurRepository;
import net.ktccenter.campusApi.dao.scolarite.SessionRepository;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportFormateurRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteFormateurDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteSessionDTO;
import net.ktccenter.campusApi.dto.reponse.branch.FormateurBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.FormateurDTO;
import net.ktccenter.campusApi.dto.request.scolarite.FormateurRequestDTO;
import net.ktccenter.campusApi.entities.administration.Branche;
import net.ktccenter.campusApi.entities.administration.User;
import net.ktccenter.campusApi.entities.scolarite.Diplome;
import net.ktccenter.campusApi.entities.scolarite.Formateur;
import net.ktccenter.campusApi.entities.scolarite.Session;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.scolarite.FormateurMapper;
import net.ktccenter.campusApi.service.MainService;
import net.ktccenter.campusApi.service.administration.UserService;
import net.ktccenter.campusApi.service.scolarite.FormateurService;
import net.ktccenter.campusApi.utils.MyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class FormateurServiceImpl extends MainService implements FormateurService {
  private final FormateurRepository repository;
  private final FormateurMapper mapper;
  private final UserService userService;
  private final SessionRepository sessionRepository;
  private final DiplomeRepository diplomeRepository;

  public FormateurServiceImpl(FormateurRepository repository, FormateurMapper mapper, UserService userService, SessionRepository sessionRepository, DiplomeRepository diplomeRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.userService = userService;
    this.sessionRepository = sessionRepository;
    this.diplomeRepository = diplomeRepository;
  }

  @Override
  public FormateurDTO save(FormateurRequestDTO dto) {
    return buildFormateurDto(repository.save(construitFormateur(mapper.asEntity(dto), dto.getBrancheId(), dto.getDiplomeId())));
  }

  private FormateurDTO buildFormateurDto(Formateur formateur) {
    FormateurDTO dto = mapper.asDTO(formateur);
    dto.setSessions(getAllSessionsForFormateur(formateur));
    return dto;
  }

  private Formateur construitFormateur(Formateur formateur, Long brancheId, Long diplomeId) {
    Branche branche = brancheRepository.findById(brancheId).orElse(null);
    if (branche == null)
      throw new ResourceNotFoundException("Aucune branche avec l'id " + brancheId);
    formateur.setBranche(branche);

    Diplome diplome = diplomeRepository.findById(diplomeId).orElse(null);
    if (diplome == null)
      throw new ResourceNotFoundException("Aucune diplome avec l'id " + diplomeId);
    formateur.setDiplome(diplome);

    // On vérifie que l'Formateur à une adresse mail, si oui on creer son compte utilisateur
    User user = userService.createUser(formateur.getNom(), formateur.getPrenom(), formateur.getEmail().toLowerCase(), "ROLE_FORMATEUR", formateur.getImageUrl(), null, null, false, branche);
    formateur.setUser(user);

    if (formateur.getMatricule().isEmpty()) formateur.setMatricule(MyUtils.GenerateMatricule(branche.getCode()));
    return formateur;
  }

  @Override
  public List<LiteFormateurDTO> save(List<ImportFormateurRequestDTO> dtos) {
    List<Formateur> list = new ArrayList<>();
    for (ImportFormateurRequestDTO dto : dtos) {
      Formateur formateur = mapper.asEntity(dto);
      formateur = importConstruitFormateur(formateur, dto.getBrancheCode(), dto.getDiplomeCode());
      list.add(formateur);
    }
    return  ((List<Formateur>) repository.saveAll(list))
            .stream()
            .map(mapper::asLite)
            .collect(Collectors.toList());
  }

  private Formateur importConstruitFormateur(Formateur formateur, String brancheCode, String diplomeCode) {
    Branche branche = brancheRepository.findByCode(brancheCode).orElse(null);
    if (branche == null)
      throw new ResourceNotFoundException("Aucune branche avec le code " + brancheCode);
    formateur.setBranche(branche);

    Diplome diplome = diplomeRepository.findByCode(diplomeCode).orElse(null);
    if (diplome == null)
      throw new ResourceNotFoundException("Aucune diplome avec le code " + diplomeCode);
    formateur.setDiplome(diplome);

    // On vérifie que l'Formateur à une adresse mail, si oui on creer son compte utilisateur
    User user = userService.createUser(formateur.getNom(), formateur.getPrenom(), formateur.getEmail().toLowerCase(), "ROLE_FORMATEUR", formateur.getImageUrl(), null, null, false, branche);
    formateur.setUser(user);

    if (formateur.getMatricule().isEmpty()) formateur.setMatricule(MyUtils.GenerateMatricule(branche.getCode()));
    return formateur;
  }

  @Override
  public void deleteById(Long id) {
    Formateur formateur = findById(id);
    if (!getAllSessionsForFormateur(formateur).isEmpty())
      throw new ResourceNotFoundException("Le formateur avec l'id " + id + " ne peut pas etre supprimé car elle est utilisé par d'autre ressource");
    repository.delete(formateur);
  }

  private Set<LiteSessionDTO> getAllSessionsForFormateur(Formateur formateur) {
    return sessionRepository.findAllByFormateur(formateur).stream().map(this::buildSessionLiteDto).collect(Collectors.toSet());
  }

  private LiteSessionDTO buildSessionLiteDto(Session entity) {
    LiteSessionDTO lite = new LiteSessionDTO(entity);
    lite.setBranche(new LiteBrancheDTO(entity.getBranche()));
    //lite.setNiveau(new LiteNiveauDTO(entity.getNiveau()));
    //lite.setVague(new LiteVagueDTO(entity.getVague()));
    return lite;
  }

  @Override
  public Formateur findById(Long id) {
    return repository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Le formateur avec l'id " + id + " n'existe pas")
    );
  }

  @Override
  public FormateurDTO getOne(Long id) {
    return buildFormateurDto(findById(id));
  }

  @Override
  public List<FormateurBranchDTO> findAll() {
    List<Formateur> formateurs = (List<Formateur>) repository.findAll();
    List<FormateurBranchDTO> result = new ArrayList<>();
    if (hasGrantAuthorized()) {
      for (Branche b : getAllBranches()) {
        result.add(buildData(b, formateurs));
      }
    } else {
      result.add(buildData(getCurrentUserBranch(), formateurs));
    }
    return result;
  }

  private FormateurBranchDTO buildData(Branche branche, List<Formateur> formateurs) {
    FormateurBranchDTO dto = new FormateurBranchDTO();
    dto.setBranche(brancheMapper.asLite(branche));
    dto.setData(formateurs.stream()
            //.filter(e -> belongsToTheCurrentBranch(branche, e))
            .filter(e -> e.getBranche().getId().equals(branche.getId()))
            .map(mapper::asLite)
            .collect(Collectors.toList()));
    return dto;
  }

  private boolean belongsToTheCurrentBranch(Branche branche, Formateur e) {
    Set<LiteSessionDTO> sessions = getAllSessionsForFormateur(e);
    for (LiteSessionDTO session : sessions) {
      if (Objects.equals(session.getBranche().getId(), branche.getId())) return true;
    }
    return false;
  }

  @Override
  public Page<LiteFormateurDTO> findAll(Pageable pageable) {
    Page<Formateur> entityPage = repository.findAll(pageable);
    List<Formateur> entities = entityPage.getContent();
    return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
  }


  @Override
  public FormateurDTO update(FormateurRequestDTO dto, Long id) {
    Formateur exist = findById(id);
    dto.setId(exist.getId());
    return buildFormateurDto(repository.save(construitFormateur(mapper.asEntity(dto), dto.getBrancheId(), dto.getDiplomeId())));
  }

  @Override
  public boolean existByTelephone(String telephone) {
    return repository.findByTelephone(telephone).isPresent();
  }

  @Override
  public boolean existByEmail(String email) {
    return repository.findByEmail(email).isPresent();
  }

  @Override
  public boolean equalsByDto(FormateurRequestDTO dto, Long id) {
    Formateur ressource = repository.findByTelephoneOrEmail(dto.getTelephone(), dto.getEmail()).orElse(null);
    if (ressource == null) return false;
    return !ressource.getId().equals(id);
  }

  @Override
  public boolean findByTelephoneAndEmail(String telephone, String email) {
    return repository.findByTelephoneOrEmail(telephone, email).isPresent();
  }

  @Override
  public Formateur findByMatricule(String matricule) {
    return repository.findByMatricule(matricule).orElseThrow(
            () -> new ResourceNotFoundException("L'étudiant avec le matricule " + matricule + " n'existe pas")
    );
  }

  @Override
  public List<LiteFormateurDTO> findAllByBranch(Long branchId) {
    Branche branche = brancheRepository.findById(branchId).orElseThrow(
            () -> new ResourceNotFoundException("La branche avec l'id " + branchId + " n'existe pas")
    );
    List<LiteFormateurDTO> result = new ArrayList<>();
    List<Formateur> formateurs = repository.findAllByBranche(branche);
    for (Formateur formateur : formateurs) {
      result.add(new LiteFormateurDTO(formateur));
    }
    return result;
  }
}