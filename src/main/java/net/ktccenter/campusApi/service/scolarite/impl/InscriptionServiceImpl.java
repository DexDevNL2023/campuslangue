package net.ktccenter.campusApi.service.scolarite.impl;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.dao.administration.CampusRepository;
import net.ktccenter.campusApi.dao.administration.InstitutionRepository;
import net.ktccenter.campusApi.dao.cours.*;
import net.ktccenter.campusApi.dao.scolarite.*;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportInscriptionRequestDTO;
import net.ktccenter.campusApi.dto.lite.LiteNewInscriptionDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteCompteForInscriptionDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteInscriptionDTO;
import net.ktccenter.campusApi.dto.reponse.branch.InscriptionBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.InscriptionDTO;
import net.ktccenter.campusApi.dto.request.scolarite.InscriptionRequestDTO;
import net.ktccenter.campusApi.dto.request.scolarite.InscrireExitStudientRequestDTO;
import net.ktccenter.campusApi.dto.request.scolarite.InscrireNewStudientRequestDTO;
import net.ktccenter.campusApi.entities.administration.Branche;
import net.ktccenter.campusApi.entities.administration.Campus;
import net.ktccenter.campusApi.entities.administration.Institution;
import net.ktccenter.campusApi.entities.administration.User;
import net.ktccenter.campusApi.entities.cours.*;
import net.ktccenter.campusApi.entities.scolarite.*;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.scolarite.InscriptionMapper;
import net.ktccenter.campusApi.reports.ReportService;
import net.ktccenter.campusApi.service.MainService;
import net.ktccenter.campusApi.service.administration.UserService;
import net.ktccenter.campusApi.service.scolarite.InscriptionService;
import net.ktccenter.campusApi.utils.MyUtils;
import net.sf.jasperreports.engine.JRException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class InscriptionServiceImpl extends MainService implements InscriptionService {
  private final InscriptionRepository repository;
  private final InscriptionMapper mapper;
  private final EtudiantRepository etudiantRepository;
  private final RubriqueRepository rubriqueRepository;
  private final ModePaiementRepository modePaiementRepository;
  private final SessionRepository sessionRepository;
  private final CampusRepository campusRepository;
  private final PaiementRepository paiementRepository;
  private final CompteRepository compteRepository;
  private final InstitutionRepository institutionRepository;
  private final ModuleFormationRepository moduleFormationRepository;
  private final NiveauRepository niveauRepository;
  private final UniteRepository uniteRepository;
  private final DiplomeRepository diplomeRepository;
  private final ExamenRepository examenRepository;
  private final TestModuleRepository testModuleRepository;
  private final EvaluationTestRepository evaluationTestRepository;
  private final EpreuveRepository epreuveRepository;
  private final UserService userService;
  private final JavaMailSender javaMailSender;
  private final ReportService reportService;

  public InscriptionServiceImpl(InscriptionRepository repository, InscriptionMapper mapper, EtudiantRepository etudiantRepository, RubriqueRepository rubriqueRepository, ModePaiementRepository modePaiementRepository, SessionRepository sessionRepository, CampusRepository campusRepository, PaiementRepository paiementRepository, CompteRepository compteRepository, InstitutionRepository institutionRepository, ModuleFormationRepository moduleFormationRepository, NiveauRepository niveauRepository, UniteRepository uniteRepository, DiplomeRepository diplomeRepository, ExamenRepository examenRepository, TestModuleRepository testModuleRepository, EvaluationTestRepository evaluationTestRepository, EpreuveRepository epreuveRepository, UserService userService, JavaMailSender javaMailSender, ReportService reportService) {
    this.repository = repository;
    this.mapper = mapper;
    this.etudiantRepository = etudiantRepository;
    this.rubriqueRepository = rubriqueRepository;
    this.modePaiementRepository = modePaiementRepository;
    this.sessionRepository = sessionRepository;
    this.campusRepository = campusRepository;
    this.paiementRepository = paiementRepository;
    this.compteRepository = compteRepository;
    this.institutionRepository = institutionRepository;
    this.moduleFormationRepository = moduleFormationRepository;
    this.niveauRepository = niveauRepository;
    this.uniteRepository = uniteRepository;
    this.diplomeRepository = diplomeRepository;
    this.examenRepository = examenRepository;
    this.testModuleRepository = testModuleRepository;
    this.evaluationTestRepository = evaluationTestRepository;
    this.epreuveRepository = epreuveRepository;
    this.userService = userService;
    this.javaMailSender = javaMailSender;
    this.reportService = reportService;
  }

  @Override
  public LiteNewInscriptionDTO inscrireExitStudient(InscrireExitStudientRequestDTO dto) {
    log.info("1");
    //log.info("Make inscription for existing studient " + dto.toString());
    Inscription inscription = inscritExitStudient(dto);
    return buildLiteInscriptionDto(repository.save(inscription));
  }

  private Inscription inscritExitStudient(InscrireExitStudientRequestDTO dto) {
    Etudiant etudiant = etudiantRepository.findById(dto.getEtudiantId()).orElseThrow(
            () -> new ResourceNotFoundException("L'apprenant avec l'id " + dto.getEtudiantId() + " n'existe pas")
    );
    log.info("2");
    //log.info("Find etudiant entity " + etudiant.toString());
    Session session = sessionRepository.findById(dto.getSessionId()).orElseThrow(
            () -> new ResourceNotFoundException("La session avec l'id " + dto.getSessionId() + " n'existe pas")
    );
    log.info("3");
    //log.info("Find session entity " + session);
    if (session.getEstTerminee()) throw new ResourceNotFoundException("La session avec l'id " + dto.getSessionId()
            + " a été cloturée");
    log.info("4");
    Campus campus = campusRepository.findById(dto.getCampusId()).orElseThrow(
            () -> new ResourceNotFoundException("Le campus avec l'id " + dto.getCampusId() + " n'existe pas")
    );
    log.info("5");
    //log.info("Find campus entity " + campus.toString());
    Rubrique rubrique = rubriqueRepository.findById(dto.getRubriqueId()).orElseThrow(
            () -> new ResourceNotFoundException("La rubrique avec l'id " + dto.getRubriqueId() + " n'existe pas")
    );
    log.info("6");
    //log.info("Find rubrique entity " + rubrique.toString());
    ModePaiement modePaiement = modePaiementRepository.findById(dto.getModePaiementId()).orElseThrow(
            () -> new ResourceNotFoundException("Le mode de paiement avec l'id " + dto.getModePaiementId() + " n'existe pas")
    );
    log.info("7");
    //log.info("Find mode paiement entity " + modePaiement.toString());

    return makeInscription(etudiant, session, campus, dto.getRubriqueId(), modePaiement,
            dto.getFraisInscription(), dto.getAvancePension(), dto.getRefPaiement(), dto.getEstRedoublant());
  }

  @Override
  public LiteNewInscriptionDTO inscrireNewStudient(InscrireNewStudientRequestDTO dto) {
    log.info("Make inscription for new studient " + dto.toString());
    Inscription inscription = inscritNewStudient(dto);
    return buildLiteInscriptionDto(repository.save(inscription));
  }

  private Inscription inscritNewStudient(InscrireNewStudientRequestDTO dto) {
    Session session = sessionRepository.findById(dto.getSessionId()).orElseThrow(
            () -> new ResourceNotFoundException("La session avec l'id " + dto.getSessionId() + " n'existe pas")
    );
    log.info("2");
    //log.info("Find session entity " + session);
    if (session.getEstTerminee()) throw new ResourceNotFoundException("La session avec l'id " + dto.getSessionId()
            + " a été cloturée");
    log.info("3");
    Campus campus = campusRepository.findById(dto.getCampusId()).orElseThrow(
            () -> new ResourceNotFoundException("Le campus avec l'id " + dto.getCampusId() + " n'existe pas")
    );
    log.info("4");
    //log.info("Find campus entity " + campus.toString());
    ModePaiement modePaiement = modePaiementRepository.findById(dto.getModePaiementId()).orElseThrow(
            () -> new ResourceNotFoundException("Le mode de paiement avec l'id " + dto.getModePaiementId() + " n'existe pas")
    );
    log.info("5");
    //log.info("Find mode paiement entity " + modePaiement.toString());

    // On crée le profile de l'étudiant
    Etudiant newEtudiant = createEtudiantProfile(dto, session);
    log.info("6");
    //log.info("Add etudiant entity " + newEtudiant);

    return makeInscription(newEtudiant, session, campus, dto.getRubriqueId(), modePaiement,
            dto.getFraisInscription(), dto.getAvancePension(), dto.getRefPaiement(), dto.getEstRedoublant());
  }

  @Override
  public InscriptionDTO save(InscriptionRequestDTO dto) {
    return buildInscriptionDto(repository.save(mapper.asEntity(dto)));
  }

  private InscriptionDTO buildInscriptionDto(Inscription inscription) {
    InscriptionDTO dto = mapper.asDTO(inscription);
    dto.setCampus(new LiteCampusDTO(getCampus(inscription.getCampusId())));
    dto.setCompte(getCompteForInscription(inscription));
    return dto;
  }

  private LiteNewInscriptionDTO buildLiteInscriptionDto(Inscription entity) {
    /*dto.setEtudiant(new LiteEtudiantForNoteDTO(entity.getEtudiant()));
    dto.setSession(new LiteSessionForNoteDTO(entity.getSession()));
    dto.setCampus(new LiteCampusDTO(getCampus(entity.getCampusId())));*/
    return new LiteNewInscriptionDTO(entity);
  }

  private Campus getCampus(Long campusId) {
    return campusRepository.findById(campusId).orElse(null);
  }

  @Override
  public List<LiteInscriptionDTO> save(List<ImportInscriptionRequestDTO> dtos) {
    return  ((List<Inscription>) repository.saveAll(mapper.asEntityList(dtos)))
            .stream()
            .map(mapper::asLite)
            .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    Inscription inscription = findById(id);
    if (getCompteForInscription(inscription) != null)
      throw new ResourceNotFoundException("L'inscription avec l'id " + id + " ne peut pas etre supprimé car elle est utilisé par d'autre ressource");
    repository.delete(inscription);
  }

  private LiteCompteForInscriptionDTO getCompteForInscription(Inscription inscription) {
    return buildCompteLiteDto(compteRepository.findByInscription(inscription));
  }

  private LiteCompteForInscriptionDTO buildCompteLiteDto(Compte entity) {
    if (entity == null) return null;
    BigDecimal solde = BigDecimal.valueOf(0.0);
      BigDecimal reste = BigDecimal.valueOf(0.0);
    Set<Paiement> paiements = new HashSet<>(paiementRepository.findAllByCompte(entity));
    for (Paiement paiement : paiements) {
        BigDecimal netApayer = paiement.getCompte().getInscription().getSession().getNiveau().getFraisPension().add(paiement.getCompte().getInscription().getSession().getNiveau().getFraisInscription());
        solde = solde.add(paiement.getMontant());
        reste = netApayer.subtract(solde);
    }
    LiteCompteForInscriptionDTO lite = new LiteCompteForInscriptionDTO(entity);
    lite.setSolde(solde);
    lite.setResteApayer(reste);
    return lite;
  }

  @Override
  public Inscription findById(Long id) {
    return repository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("L'inscription avec l'id " + id + " n'existe pas")
    );
  }

  @Override
  public InscriptionDTO getOne(Long id) {
    return buildInscriptionDto(findById(id));
  }

  @Override
  public List<InscriptionBranchDTO> findAll() {
    List<Inscription> inscriptions = (List<Inscription>) repository.findAll();
    List<InscriptionBranchDTO> result = new ArrayList<>();
    if (hasGrantAuthorized()) {
      for (Branche b : getAllBranches()) {
        result.add(buildData(b, inscriptions));
      }
    } else {
      result.add(buildData(getCurrentUserBranch(), inscriptions));
    }
    return result;
  }

  private InscriptionBranchDTO buildData(Branche branche, List<Inscription> inscriptions) {
    InscriptionBranchDTO dto = new InscriptionBranchDTO();
    dto.setBranche(brancheMapper.asLite(branche));
    dto.setData(inscriptions.stream()
            .filter(e -> belongsToTheCurrentBranch(branche, e))
            .map(mapper::asLite)
            .collect(Collectors.toList()));
    return dto;
  }

  private boolean belongsToTheCurrentBranch(Branche branche, Inscription e) {
    return e.getSession().getBranche().getId().equals(branche.getId());
  }

  @Override
  public Page<LiteInscriptionDTO> findAll(Pageable pageable) {
    Page<Inscription> entityPage = repository.findAll(pageable);
    List<Inscription> entities = entityPage.getContent();
    return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
  }

  @Override
  public InscriptionDTO update(InscriptionRequestDTO dto, Long id) {
    Session session = sessionRepository.findById(dto.getSessionId()).orElseThrow(
            () -> new ResourceNotFoundException("La session avec l'id " + dto.getSessionId() + " n'existe pas")
    );
    if (session.getEstTerminee()) throw new ResourceNotFoundException("La session avec l'id " + dto.getSessionId()
            + " a été cloturée");
    log.info("Find session entity " + session);
    Inscription exist = findById(id);
    dto.setId(exist.getId());
    return buildInscriptionDto(repository.save(mapper.asEntity(dto)));
  }

  @Override
  public boolean equalsByDto(InscriptionRequestDTO dto, Long id) {
    Inscription ressource = repository.findById(id).orElse(null);
    if (ressource == null) return false;
    return !ressource.equals(dto);
  }

  private Inscription makeInscription(Etudiant etudiant,
                                      Session session,
                                      Campus campus,
                                      Long rubriqueId,
                                      ModePaiement modePaiement,
                                      BigDecimal fraisInscription,
                                      BigDecimal avancePension,
                                      String refPaiement,
                                      Boolean estRedoublant) {
    log.info("21");
    // On verifie que l'etudiant n'est pas inscrit dans cette session
    if (isInscritToSession(etudiant, session)) throw
            new ResourceNotFoundException("L'étudiant " + etudiant.getNom().toLowerCase() + " est déjà inscrit pour cette session.");
    log.info("22");
    // On génére le matricule de l"étudiant
    etudiant.setMatricule(MyUtils.GenerateMatricule(session.getBranche().getCode()+"-"+session.getVague().getCode()+"-"+session.getNiveau().getCode()));
    log.info("23");
    // On met à jour le profil de l'étudiant
    etudiant = etudiantRepository.save(etudiant);
    log.info("24");

    // On crée l'inscription
    Inscription inscription = new Inscription();
    // On construit l'objet inscription et on le persite
    inscription.setDateInscription(MyUtils.currentDate());
    inscription.setEstRedoublant(estRedoublant);
    inscription.setSession(session);
    inscription.setCampusId(campus.getId());
    inscription.setCampus(campus);
    inscription.setEtudiant(etudiant);
    inscription = repository.save(inscription);
    log.info("25");
    //log.info("Create inscription entity " + inscription);
    TestModule testModule = getEtudiantTestModule(inscription);
    log.info("26");
    //log.info("Create test module entity " + testModule);
    Examen examen = getEtudiantExamen(inscription);
    log.info("27");
    //log.info("Create examen entity " + examen);

    // On crée le versement
    if (fraisInscription.compareTo(session.getNiveau().getFraisInscription()) > 0) throw
            new ResourceNotFoundException("Les frais d'inscription saisie sont supérieur au frais d'inscription prévue pour ce niveau.");
    log.info("28");
    if (fraisInscription.compareTo(new BigDecimal("0.0")) <= 0)
      fraisInscription = session.getNiveau().getFraisInscription();
    log.info("29");
    // On construit l'objet versement et on le persite
    Rubrique rubriqueInscription = rubriqueRepository.findByCode("INSCRIPTION").orElseThrow(
            () -> new ResourceNotFoundException("La rubrique avec le code INSCRIPTION n'existe pas")
    );
    log.info("29A");
    //log.info("Find rubrique entity " + rubriqueInscription.toString());
    // On cree le compte de paiement
    Compte compte = getEtudiantComptePaiement(inscription);
    log.info("29B");
    //log.info("create compte entity " + compte);
    Paiement versement = new Paiement();
    versement.setRefPaiement(refPaiement);
    versement.setDatePaiement(MyUtils.dateNow());
    versement.setModePaiement(modePaiement);
    versement.setMontant(fraisInscription);
    versement.setRubrique(rubriqueInscription);
    versement.setCampusId(campus.getId());
    versement.setCampus(campus);
    versement.setCompte(compte);
    versement = paiementRepository.save(versement);
    log.info("29C");
    //log.info("Create paiement entity " + versement);

    // Si l'avance sur la pension est supérieure à zéro, on effectue un versement sur la rubrique pension
    if (avancePension.compareTo(new BigDecimal("0.0")) > 0) {
      Rubrique rubrique = rubriqueRepository.findById(rubriqueId).orElseThrow(
              () -> new ResourceNotFoundException("La rubrique avec l'id " + rubriqueId + " n'existe pas")
      );
      log.info("29D");
      //log.info("Find rubrique entity " + rubrique.toString());
      // On construit l'objet versement et on le persite
      Paiement avance = new Paiement();
      avance.setRefPaiement(refPaiement);
      avance.setDatePaiement(MyUtils.dateNow());
      avance.setModePaiement(modePaiement);
      avance.setMontant(avancePension);
      avance.setRubrique(rubrique);
      avance.setCampusId(campus.getId());
      avance.setCampus(campus);
      avance.setCompte(compte);
      avance = paiementRepository.save(avance);
      log.info("29E");
      //log.info("Avance sur " + rubrique.getLibelle() + " : " + avance);
    }

    // On envoie le mail de confirmation de paiement
    //if (etudiant != null && etudiant.getEmail() != null) sendHtmlMail(etudiant, versement);
    return inscription;
  }

  private boolean isInscritToSession(Etudiant etudiant, Session session) {
    List<Inscription> list = repository.findAllByEtudiantAndSession(etudiant, session);
    return !list.isEmpty();
  }

  private Compte getEtudiantComptePaiement(Inscription inscription) {
    String code = MyUtils.GenerateCode("CMPT"+"-"+inscription.getCampus().getBranche().getCode()+"-"+inscription.getEtudiant().getMatricule());
    log.info("50");
    //log.info(code);
    Compte compte = new Compte();
    compte.setCode(code);
    compte.setInscription(inscription);
    compte = compteRepository.save(compte);
    log.info("51");
    //log.info(compte.toString());
    return compte;
  }

  private Examen getEtudiantExamen(Inscription inscription) {
    log.info("41");
    //log.info("Begin to build examen");
    String code = MyUtils.GenerateCode("EXAMEN-"+inscription.getSession().getCode()+"-"+inscription.getEtudiant().getMatricule());
    log.info("42");
    //log.info("Examen code "+code);
    Examen examen = new Examen();
    examen.setCode(code);
    examen.setInscription(inscription);
    examen = examenRepository.save(examen);
    log.info("43");
    //log.info("Examen "+ examen);
    Niveau niveau = niveauRepository.findById(inscription.getSession().getNiveau().getId()).orElseThrow(
            () ->  new ResourceNotFoundException("Le niveau avec l'id "+inscription.getSession().getNiveau().getId()+" n'existe pas")
    );
    log.info("44");
    //log.info("Find niveau "+niveau.toString());
    List<Unite> unites = uniteRepository.findAllByNiveau(niveau);
    if (unites.isEmpty()) throw
            new ResourceNotFoundException("Avant d'effectuer une inscription veillez ajouter au moins une unité de formation pour le niveau " +
                    niveau.getLibelle());
    log.info("45");
    //log.info("Get all unite by niveau "+ unites);
    log.info("add test to test module");
    for (Unite unite : unites) {
      Epreuve epreuve = new Epreuve();
      epreuve.setUnite(unite);
      epreuve.setExamen(examen);
      epreuve = epreuveRepository.save(epreuve);
      log.info("46");
      //log.info("new epreuve " + epreuve);
    }
    log.info("47");
    //
    return examen;
  }

  private TestModule getEtudiantTestModule(Inscription inscription) {
    log.info("30");
    //log.info("Begin to build test module");
    String code = MyUtils.GenerateCode("TEST-"+inscription.getSession().getCode()+"-"+inscription.getEtudiant().getMatricule());
    log.info("31");
    //log.info("Test module code"+code);
    TestModule testModule = new TestModule();
    testModule.setCode(code);
    testModule.setInscription(inscription);
    testModule = testModuleRepository.save(testModule);
    log.info("32");
    //log.info("Test module "+ testModule);
    Niveau niveau = niveauRepository.findById(inscription.getSession().getNiveau().getId()).orElseThrow(
            () ->  new ResourceNotFoundException("Le niveau avec l'id "+inscription.getSession().getNiveau().getId()+" n'existe pas")
    );
    log.info("33");
    //log.info("Find niveau "+niveau.toString());
    List<ModuleFormation> moduleFormations = moduleFormationRepository.findAllByNiveau(niveau);
    if (moduleFormations.isEmpty()) throw new ResourceNotFoundException("Avant d'effectuer une inscription veillez ajouter au moins un module de formation pour le niveau "+niveau.getLibelle());
    log.info("34");
    //log.info("Get all module by niveau "+ moduleFormations);
    log.info("add test to test module");
    for (ModuleFormation module : moduleFormations) {
      EvaluationTest test = new EvaluationTest();
      test.setModuleFormation(module);
      test.setTestModule(testModule);
      test = evaluationTestRepository.save(test);
      log.info("35");
      //log.info("new test " + test);
    }
    log.info("36");
    //
    return testModule;
  }

  private Etudiant createEtudiantProfile(InscrireNewStudientRequestDTO dto, Session session) {
    if(!MyUtils.isValidEmailAddress(dto.getEmail()))
      throw new ResourceNotFoundException("L'email " + dto.getEmail() + " est invalide.");

    if (etudiantRepository.findByEmail(dto.getEmail()).isPresent())
      throw new ResourceNotFoundException("L'apprenant avec l'email " + dto.getEmail() + " existe déjà");

    if (etudiantRepository.findByTelephone(dto.getTelephone()).isPresent())
      throw new ResourceNotFoundException("L'apprenant avec le téléphone " + dto.getTelephone() + " existe déjà");

    Diplome diplome = diplomeRepository.findById(dto.getDernierDiplomeId()).orElseThrow(
            () -> new ResourceNotFoundException("Le diplome avec l'id " + dto.getDernierDiplomeId() + " n'existe pas")
    );
    log.info("Find diplome entity " + diplome.toString());

    // On recherche l'existance d'un étudiant potenciel
    log.info("Find if etudiant exist");
    Optional<Etudiant> existingEtudiant = etudiantRepository.findByTelephoneOrEmail(dto.getTelephone(), dto.getEmail().toLowerCase());
    if (existingEtudiant.isPresent()) throw new ResourceNotFoundException("Un etudiant existe deja avec les données suivants : " + existingEtudiant);
    log.info("Etudiant not exist");

    // On créer l'objet etudiant
    Etudiant etudiant = new Etudiant();
    log.info("New etudiant");

    // On vérifie que l'etudiant à une adresse mail, si oui on creer son compte utilisateur
      User user = userService.createUser(dto.getNom(), dto.getPrenom(), dto.getEmail().toLowerCase(), "ROLE_ETUDIANT", dto.getImageUrl(), null, null, false, session.getBranche());
    etudiant.setUser(user);

    // On construit le dto du compte user assoicer à étudiant
    log.info("beging to create etudiant");
    etudiant.setNom(dto.getNom());
    etudiant.setPrenom(dto.getPrenom());

    // On génére le matricule de l"étudiant
    etudiant.setMatricule(MyUtils.GenerateMatricule("DEFAULT-STUDENT"));
    //etudiant.setMatricule(MyUtils.GenerateMatricule(session.getBranche().getCode()+"-"+session.getVague().getCode()+"-"+session.getNiveau().getCode()));

    // On construit l'objet etudiant
    etudiant.setImageUrl(dto.getImageUrl());
    etudiant.setNationalite(dto.getNationalite());
    etudiant.setSexe(dto.getSexe());
    etudiant.setAdresse(dto.getAdresse());
    etudiant.setTelephone(dto.getTelephone());
    etudiant.setEmail(dto.getEmail().toLowerCase());
    etudiant.setTuteur(dto.getTuteur());
    etudiant.setContactTuteur(dto.getContactTuteur());
    etudiant.setDernierDiplome(diplome);
    log.info("Finish to build etudiant");

    // On met à jour le profil de l'étudiant
    etudiant = etudiantRepository.save(etudiant);
    log.info("Create etudiant");

    return etudiant;
  }

  public void sendHtmlMail(Etudiant etudiant, Paiement versement) {
    try {
      Institution institution = institutionRepository.findFirstByOrderByName().orElseThrow(
              () ->  new ResourceNotFoundException("L'institution n'existe pas")
      );
      String toAddress = etudiant.getUser().getEmail();
      String fromAddress = institution.getEmail();
      String senderName = institution.getName();
      String subject = "Confirmation de paiement";
      String body = "Cher(Chère) "+etudiant.getPrenom()+" "+etudiant.getNom()+". \n" +
              versement.getRubrique().getLibelle()+" au "+versement.getCompte().getInscription().getSession().getNiveau().getLibelle()+" pour un montant de "+versement.getMontant()+", refèrence de paiement "+versement.getRefPaiement()+" du "+versement.getDatePaiement().toString()+".\n" +
              "Votre paiement a été validé et votre inscription va maintenant être traitée.\n" +
              "Cordialement, Le Service Etudiant.";
      MimeMessage mail = javaMailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mail, true, "UTF-8");
      helper.setFrom(fromAddress, senderName);
      if (toAddress.contains(",")) {
        helper.setTo(toAddress.split(","));
      } else {
        helper.setTo(toAddress);
      }
      helper.setSubject(subject);
      helper.setText(body, true);
      javaMailSender.send(mail);
      System.out.println("Sent mail: " + subject);
    } catch (MessagingException | UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Path downloadAttestation(Long id) throws URISyntaxException, JRException, IOException {
    Inscription inscription = findById(id);
    if (isNotWin(inscription)) throw new ResourceNotFoundException("Vous n'avez pas réussi votre examen");
    return reportService.downloadAttestationFormationAllemandToPdf(inscription);
  }

  private boolean isNotWin(Inscription inscription) {
    Examen examen = examenRepository.findByInscription(inscription);
    int note = examen.getMoyenne().intValue();
    return (note < 12);
  }
}