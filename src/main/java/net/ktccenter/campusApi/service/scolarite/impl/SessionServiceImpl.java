package net.ktccenter.campusApi.service.scolarite.impl;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.dao.administration.OccupationSalleRepository;
import net.ktccenter.campusApi.dao.cours.UniteRepository;
import net.ktccenter.campusApi.dao.scolarite.InscriptionRepository;
import net.ktccenter.campusApi.dao.scolarite.ModuleFormationRepository;
import net.ktccenter.campusApi.dao.scolarite.SessionRepository;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportSessionRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteUniteDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteInscriptionDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteModuleFormationDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteNiveauDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteSessionDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.SessionDTO;
import net.ktccenter.campusApi.dto.request.scolarite.SessionRequestDTO;
import net.ktccenter.campusApi.entities.administration.OccupationSalle;
import net.ktccenter.campusApi.entities.scolarite.Inscription;
import net.ktccenter.campusApi.entities.scolarite.Session;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.scolarite.SessionMapper;
import net.ktccenter.campusApi.reports.ReportService;
import net.ktccenter.campusApi.service.scolarite.SessionService;
import net.ktccenter.campusApi.utils.MyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class SessionServiceImpl implements SessionService {
  private final SessionRepository repository;
  private final SessionMapper mapper;
  private final OccupationSalleRepository occupationSalleRepository;
  private final InscriptionRepository inscriptionRepository;
  private final ModuleFormationRepository moduleFormationRepository;
  private final UniteRepository uniteRepository;
  private final ReportService reportService;

  public SessionServiceImpl(SessionRepository repository, SessionMapper mapper, OccupationSalleRepository occupationSalleRepository, InscriptionRepository inscriptionRepository, ModuleFormationRepository moduleFormationRepository, UniteRepository uniteRepository, ReportService reportService) {
    this.repository = repository;
    this.mapper = mapper;
    this.occupationSalleRepository = occupationSalleRepository;
    this.inscriptionRepository = inscriptionRepository;
    this.moduleFormationRepository = moduleFormationRepository;
    this.uniteRepository = uniteRepository;
    this.reportService = reportService;
  }

  @Override
  public SessionDTO save(SessionRequestDTO dto) {
    Set<OccupationSalle> occupations = new HashSet<>();
    Session session = mapper.asEntity(dto);
    Set<Long> occupationIds = dto.getOccupationIds();
    for (Long occupationId : occupationIds) {
      OccupationSalle occupation = occupationSalleRepository.findById(occupationId).orElseThrow(
              () ->  new ResourceNotFoundException("L'occupation avec l'id " + occupationId + " n'existe pas")
      );
      if (occupation.getEstOccupee()) throw new ResourceNotFoundException("L'occupation avec l'id " + occupation.getId() + " n'est plus disponible");
      occupation.setEstOccupee(true);
      occupation = occupationSalleRepository.save(occupation);
      occupations.add(occupation);
    }
    log.info(session.getDateDebut().toString());
    log.info(session.getDateDebut().toString());
    //Year year = Year.of(session.getDateDebut().getYear());
    //Month mounthD = Month.of(session.getDateDebut().getMonth());
    //Month mounthF = Month.of(session.getDateFin().getMonth());
    //SimpleDateFormat formatNowMonth = new SimpleDateFormat("MM");
    SimpleDateFormat formatNowYear = new SimpleDateFormat("YYYY");

    String monthDebut = Month.of(session.getDateDebut().getMonth()).toString(); // formatNowMonth.format(session.getDateDebut()).toUpperCase();
    String monthFin = Month.of(session.getDateFin().getMonth()).toString(); // formatNowMonth.format(session.getDateFin()).toUpperCase();
    String currentYear = formatNowYear.format(session.getDateDebut()).toUpperCase();
    log.info(monthDebut);
    log.info(monthFin);
    log.info(currentYear);
    String code = MyUtils.GenerateCode(session.getVague().getCode()+"-"+session.getNiveau().getCode()+"-"+monthDebut+"-"+monthFin+"-"+currentYear);

    //String code = MyUtils.GenerateCode(session.getVague().getCode()+"-"+session.getNiveau().getCode()+"-"+mounthD.name()+"-"+mounthF.name()+"-"+year);
    //String code = MyUtils.GenerateCode(session.getVague().getCode()+"-"+session.getNiveau().getCode()+"-"+mounthD.toString()+"-"+mounthF.toString()+"-"+year.toString());
    //String code = MyUtils.GenerateCode(session.getVague().getCode()+"-"+session.getNiveau().getCode());
    log.info(code);
    session.setCode(code);
    session.setOccupations(occupations);
    session.setEstTerminee(false);
    return buildSessionDto(repository.save(session));
  }

  private SessionDTO buildSessionDto(Session session) {
    SessionDTO dto = mapper.asDTO(session);
    dto.setInscriptions(getAllInscriptionsForSession(session));
    return dto;
  }

  private Set<LiteInscriptionDTO> getAllInscriptionsForSession(Session session) {
    return inscriptionRepository.findAllBySession(session).stream().map(this::buildInscriptionLiteDto).collect(Collectors.toSet());
  }

  private LiteInscriptionDTO buildInscriptionLiteDto(Inscription entity) {
    LiteInscriptionDTO lite = new LiteInscriptionDTO(entity);
    lite.setCampus(new LiteCampusDTO(entity.getCampus()));
    return lite;
  }

  @Override
  public List<LiteSessionDTO> save(List<ImportSessionRequestDTO> dtos) {
    List<Session> list = new ArrayList<>();
    for (ImportSessionRequestDTO dto : dtos) {
      Set<OccupationSalle> occupations = new HashSet<>();
      Session session = mapper.asEntity(dto);
      Set<String> occupationCodes = dto.getOccupationCodes();
      for (String occupationCode : occupationCodes) {
        OccupationSalle occupation = occupationSalleRepository.findByCode(occupationCode).orElseThrow(
                () ->  new ResourceNotFoundException("L'occupation avec le code " + occupationCode + " n'existe pas")
        );
        if (occupation.getEstOccupee()) throw new ResourceNotFoundException("L'occupation avec le code " + occupation.getCode() + " n'est plus disponible");
        occupation.setEstOccupee(true);
        occupation = occupationSalleRepository.save(occupation);
        occupations.add(occupation);
      }
      log.info(session.getDateDebut().toString());
      log.info(session.getDateDebut().toString());
      //Year year = Year.of(session.getDateDebut().getYear());
      //Month mounthD = Month.of(session.getDateDebut().getMonth());
      //Month mounthF = Month.of(session.getDateFin().getMonth());
      //SimpleDateFormat formatNowMonth = new SimpleDateFormat("MM");
      SimpleDateFormat formatNowYear = new SimpleDateFormat("YYYY");

      String monthDebut = Month.of(session.getDateDebut().getMonth()).toString(); // formatNowMonth.format(session.getDateDebut()).toUpperCase();
      String monthFin = Month.of(session.getDateFin().getMonth()).toString(); // formatNowMonth.format(session.getDateFin()).toUpperCase();
      String currentYear = formatNowYear.format(session.getDateDebut()).toUpperCase();
      log.info(monthDebut);
      log.info(monthFin);
      log.info(currentYear);
      String code = MyUtils.GenerateCode(session.getVague().getCode()+"-"+session.getNiveau().getCode()+"-"+monthDebut+"-"+monthFin+"-"+currentYear);

      //String code = MyUtils.GenerateCode(session.getVague().getCode()+"-"+session.getNiveau().getCode()+"-"+mounthD.name()+"-"+mounthF.name()+"-"+year);
      //String code = MyUtils.GenerateCode(session.getVague().getCode()+"-"+session.getNiveau().getCode()+"-"+mounthD.toString()+"-"+mounthF.toString()+"-"+year.toString());
      //String code = MyUtils.GenerateCode(session.getVague().getCode()+"-"+session.getNiveau().getCode());
      log.info(code);
      session.setCode(code);
      session.setOccupations(occupations);
      list.add(session);
    }
    return ((List<Session>) repository.saveAll(list)).stream()
            .map(this::buildSessionLiteDto)
            .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    Session session = findById(id);
    if (!getAllInscriptionsForSession(session).isEmpty())
      throw new ResourceNotFoundException("La session avec l'id " + id + " ne peut pas etre supprimé car elle est utilisé par d'autre ressource");
    repository.delete(session);

  }

  @Override
  public Session findById(Long id) {
    return repository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("La session avec l'id " + id + " n'existe pas")
    );
  }

  @Override
  public SessionDTO getOne(Long id) {
    return buildSessionDto(findById(id));
  }

  @Override
  public List<LiteSessionDTO> findAll() {
    return ((List<Session>) repository.findAll()).stream().map(this::buildSessionLiteDto).collect(Collectors.toList());
  }

  private LiteSessionDTO buildSessionLiteDto(Session session) {
    LiteSessionDTO lite = mapper.asLite(session);
    lite.getNiveau().setUnites(getAllUnitesForNiveau(lite.getNiveau()));
    lite.getNiveau().setModules(getAllModulesForNiveau(lite.getNiveau()));
    return lite;
  }

  private Set<LiteModuleFormationDTO> getAllModulesForNiveau(LiteNiveauDTO niveau) {
    return moduleFormationRepository.findAllByNiveauId(niveau.getId()).stream().map(LiteModuleFormationDTO::new).collect(Collectors.toSet());
  }

  private Set<LiteUniteDTO> getAllUnitesForNiveau(LiteNiveauDTO niveau) {
    return uniteRepository.findAllByNiveauId(niveau.getId()).stream().map(LiteUniteDTO::new).collect(Collectors.toSet());
  }

  @Override
  public Page<LiteSessionDTO> findAll(Pageable pageable) {
    Page<Session> entityPage = repository.findAll(pageable);
    List<Session> entities = entityPage.getContent();
    return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
  }

  @Override
  public SessionDTO close(Long id) {
    Session exist = findById(id);
    exist.setEstTerminee(true);
    return buildSessionDto(repository.save(exist));
  }

  @Override
  public SessionDTO update(SessionRequestDTO dto, Long id) {
    Session exist = findById(id);
    Set<OccupationSalle> occupations = new HashSet<>();
    Session session = mapper.asEntity(dto);
    session.setId(exist.getId());
    Set<Long> occupationIds = dto.getOccupationIds();
    for (Long occupationId : occupationIds) {
      OccupationSalle occupation = occupationSalleRepository.findById(occupationId).orElseThrow(
              () ->  new ResourceNotFoundException("L'occupation avec l'id " + occupationId + " n'existe pas")
      );
      if (occupation.getEstOccupee()) throw new ResourceNotFoundException("L'occupation avec l'id " + occupation.getId() + " n'est plus disponible");
      occupation.setEstOccupee(true);
      occupation = occupationSalleRepository.save(occupation);
      occupations.add(occupation);
    }
    log.info(session.getDateDebut().toString());
    log.info(session.getDateDebut().toString());
    //Year year = Year.of(session.getDateDebut().getYear());
    //Month mounthD = Month.of(session.getDateDebut().getMonth());
    //Month mounthF = Month.of(session.getDateFin().getMonth());
    //SimpleDateFormat formatNowMonth = new SimpleDateFormat("MM");
    SimpleDateFormat formatNowYear = new SimpleDateFormat("YYYY");

    String monthDebut = Month.of(session.getDateDebut().getMonth()).toString(); // formatNowMonth.format(session.getDateDebut()).toUpperCase();
    String monthFin = Month.of(session.getDateFin().getMonth()).toString(); // formatNowMonth.format(session.getDateFin()).toUpperCase();
    String currentYear = formatNowYear.format(session.getDateDebut()).toUpperCase();
    log.info(monthDebut);
    log.info(monthFin);
    log.info(currentYear);
    String code = MyUtils.GenerateCode(session.getVague().getCode()+"-"+session.getNiveau().getCode()+"-"+monthDebut+"-"+monthFin+"-"+currentYear);

    //String code = MyUtils.GenerateCode(session.getVague().getCode()+"-"+session.getNiveau().getCode()+"-"+mounthD.name()+"-"+mounthF.name()+"-"+year);
    //String code = MyUtils.GenerateCode(session.getVague().getCode()+"-"+session.getNiveau().getCode()+"-"+mounthD.toString()+"-"+mounthF.toString()+"-"+year.toString());
    //String code = MyUtils.GenerateCode(session.getVague().getCode()+"-"+session.getNiveau().getCode());
    log.info(code);
    session.setCode(code);
    session.setOccupations(occupations);
    session = repository.save(session);
    return buildSessionDto(session);
  }

  @Override
  public boolean existByCode(String code) {
    return repository.findByCode(code).isPresent();
  }

  @Override
  public boolean equalsByDto(SessionRequestDTO dto, Long id) {
    Session ressource = repository.findById(id).orElse(null);
    if (ressource == null) return false;
    return !ressource.equals(dto);
  }

  @Override
  public Session findByCode(String code) {
    return repository.findByCode(code).orElseThrow(
            () -> new ResourceNotFoundException("La session avec le code " + code + " n'existe pas")
    );
  }
}