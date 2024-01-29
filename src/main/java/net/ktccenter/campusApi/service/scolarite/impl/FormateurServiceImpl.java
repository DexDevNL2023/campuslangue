package net.ktccenter.campusApi.service.scolarite.impl;

import net.ktccenter.campusApi.dao.scolarite.FormateurRepository;
import net.ktccenter.campusApi.dao.scolarite.SessionRepository;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportFormateurRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteFormateurDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteNiveauDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteSessionDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteVagueDTO;
import net.ktccenter.campusApi.dto.reponse.branch.FormateurBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.FormateurDTO;
import net.ktccenter.campusApi.dto.request.scolarite.FormateurRequestDTO;
import net.ktccenter.campusApi.entities.administration.User;
import net.ktccenter.campusApi.entities.scolarite.Formateur;
import net.ktccenter.campusApi.entities.scolarite.Session;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.scolarite.FormateurMapper;
import net.ktccenter.campusApi.service.administration.UserService;
import net.ktccenter.campusApi.service.scolarite.FormateurService;
import net.ktccenter.campusApi.utils.MyUtils;
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
public class FormateurServiceImpl implements FormateurService {
  private final FormateurRepository repository;
  private final FormateurMapper mapper;
  private final UserService userService;
  private final SessionRepository sessionRepository;

  public FormateurServiceImpl(FormateurRepository repository, FormateurMapper mapper, UserService userService, SessionRepository sessionRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.userService = userService;
    this.sessionRepository = sessionRepository;
  }

  @Override
  public FormateurDTO save(FormateurRequestDTO dto) {
    return buildFormateurDto(repository.save(construitFormateur(mapper.asEntity(dto))));
  }

  private FormateurDTO buildFormateurDto(Formateur formateur) {
    FormateurDTO dto = mapper.asDTO(formateur);
    dto.setSessions(getAllSessionsForFormateur(formateur));
    return dto;
  }

  private Formateur construitFormateur(Formateur Formateur) {
    // On vérifie que l'Formateur à une adresse mail, si oui on creer son compte utilisateur
    User user = userService.createUser(Formateur.getNom(), Formateur.getPrenom(), Formateur.getEmail().toLowerCase(), "ROLE_FORMATEUR", Formateur.getImageUrl(), null, null);
    Formateur.setUser(user);
    if (Formateur.getMatricule().isEmpty()) Formateur.setMatricule(MyUtils.GenerateMatricule("DEFAULT-TRAINER"));
    return Formateur;
  }

  @Override
  public List<LiteFormateurDTO> save(List<ImportFormateurRequestDTO> dtos) {
    List<Formateur> list = mapper.asEntityList(dtos);
    list.forEach(this::construitFormateur);
    return  ((List<Formateur>) repository.saveAll(list))
            .stream()
            .map(mapper::asLite)
            .collect(Collectors.toList());
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
    lite.setNiveau(new LiteNiveauDTO(entity.getNiveau()));
    lite.setVague(new LiteVagueDTO(entity.getVague()));
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
      //return ((List<Formateur>) repository.findAll()).stream().map(mapper::asLite).collect(Collectors.toList());
      return null;
  }

  @Override
  public Page<LiteFormateurDTO> findAll(Pageable pageable) {
    Page<Formateur> entityPage = repository.findAll(pageable);
    List<Formateur> entities = entityPage.getContent();
    return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
  }


  @Override
  public void update(FormateurRequestDTO dto, Long id) {
    Formateur exist = findById(id);
    dto.setId(exist.getId());
    buildFormateurDto(repository.save(construitFormateur(mapper.asEntity(dto))));
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
}