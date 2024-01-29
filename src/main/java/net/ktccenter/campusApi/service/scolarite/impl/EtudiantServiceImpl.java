package net.ktccenter.campusApi.service.scolarite.impl;

import net.ktccenter.campusApi.dao.administration.CampusRepository;
import net.ktccenter.campusApi.dao.cours.EpreuveRepository;
import net.ktccenter.campusApi.dao.cours.EvaluationTestRepository;
import net.ktccenter.campusApi.dao.cours.ExamenRepository;
import net.ktccenter.campusApi.dao.cours.TestModuleRepository;
import net.ktccenter.campusApi.dao.scolarite.CompteRepository;
import net.ktccenter.campusApi.dao.scolarite.EtudiantRepository;
import net.ktccenter.campusApi.dao.scolarite.InscriptionRepository;
import net.ktccenter.campusApi.dao.scolarite.PaiementRepository;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportEtudiantRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusDTO;
import net.ktccenter.campusApi.dto.lite.cours.*;
import net.ktccenter.campusApi.dto.lite.scolarite.*;
import net.ktccenter.campusApi.dto.reponse.branch.EtudiantBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.EtudiantDTO;
import net.ktccenter.campusApi.dto.request.scolarite.EtudiantRequestDTO;
import net.ktccenter.campusApi.entities.administration.Campus;
import net.ktccenter.campusApi.entities.administration.OccupationSalle;
import net.ktccenter.campusApi.entities.administration.User;
import net.ktccenter.campusApi.entities.cours.Epreuve;
import net.ktccenter.campusApi.entities.cours.EvaluationTest;
import net.ktccenter.campusApi.entities.cours.Examen;
import net.ktccenter.campusApi.entities.cours.TestModule;
import net.ktccenter.campusApi.entities.scolarite.*;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.scolarite.EtudiantMapper;
import net.ktccenter.campusApi.service.administration.UserService;
import net.ktccenter.campusApi.service.scolarite.EtudiantService;
import net.ktccenter.campusApi.utils.MyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EtudiantServiceImpl implements EtudiantService {
    private final EtudiantRepository repository;
    private final EtudiantMapper mapper;
    private final UserService userService;
    private final InscriptionRepository inscriptionRepository;
    private final CampusRepository campusRepository;
    private final PaiementRepository paiementRepository;
    private final CompteRepository compteRepository;
    private final TestModuleRepository testModuleRepository;
    private final ExamenRepository examenRepository;
    private final EvaluationTestRepository evaluationTestRepository;
    private final EpreuveRepository epreuveRepository;

    public EtudiantServiceImpl(EtudiantRepository repository, EtudiantMapper mapper, UserService userService, InscriptionRepository inscriptionRepository, CampusRepository campusRepository, PaiementRepository paiementRepository, CompteRepository compteRepository, TestModuleRepository testModuleRepository, ExamenRepository examenRepository, EvaluationTestRepository evaluationTestRepository, EpreuveRepository epreuveRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.userService = userService;
        this.inscriptionRepository = inscriptionRepository;
        this.campusRepository = campusRepository;
        this.paiementRepository = paiementRepository;
        this.compteRepository = compteRepository;
        this.testModuleRepository = testModuleRepository;
        this.examenRepository = examenRepository;
        this.evaluationTestRepository = evaluationTestRepository;
        this.epreuveRepository = epreuveRepository;
    }

    @Override
    public EtudiantDTO save(EtudiantRequestDTO dto) {
        if (dto.getTelephone().equals(dto.getContactTuteur()))
            throw new ResourceNotFoundException("L'apprenant ne saurais avoir le même numéro de téléphone que son tuteur");
        return buildEtudiantDto(repository.save(construitEtudiant(mapper.asEntity(dto))));
    }

    private EtudiantDTO buildEtudiantDto(Etudiant etudiant) {
        EtudiantDTO dto = mapper.asDTO(etudiant);
        dto.setInscriptions(getAllInscriptionsForEtudiantDto(etudiant));
        return buildStateSurCompte(dto);
    }

    private EtudiantDTO buildStateSurCompte(EtudiantDTO dto) {
        BigDecimal soldeTotal = BigDecimal.valueOf(0.0);
        BigDecimal resteTotal = BigDecimal.valueOf(0.0);
        Integer nbrePaiement = 0;
        Set<LiteInscriptionDTO> inscriptions = dto.getInscriptions();
        for (LiteInscriptionDTO inscription : inscriptions) {
            soldeTotal = soldeTotal.add(inscription.getCompte().getSolde());
            resteTotal = resteTotal.add(inscription.getCompte().getResteApayer());
            nbrePaiement = nbrePaiement + inscription.getCompte().getPaiements().size();
        }
        dto.setSoldeTotal(soldeTotal);
        dto.setResteApayer(resteTotal);
        dto.setNbrePaiement(nbrePaiement);
        return dto;
    }

    private LiteEtudiantDTO buildLiteEtudiantDto(Etudiant etudiant) {
        LiteEtudiantDTO dto = mapper.asLite(etudiant);
        dto.setInscriptions(getAllInscriptionsForEtudiantDto(etudiant));
        return dto;
    }

    private Etudiant construitEtudiant(Etudiant etudiant) {
        // On vérifie que l'etudiant à une adresse mail, si oui on creer son compte utilisateur
        User user = userService.createUser(etudiant.getNom(), etudiant.getPrenom(), etudiant.getEmail().toLowerCase(), "ROLE_ETUDIANT", etudiant.getImageUrl(), null, null);
        etudiant.setUser(user);

        // On génére le matricule de l"étudiant
        etudiant.setMatricule(MyUtils.GenerateMatricule("DEFAULT-STUDENT"));
        return etudiant;
    }

    @Override
    public List<LiteEtudiantDTO> save(List<ImportEtudiantRequestDTO> dtos) {
        for (ImportEtudiantRequestDTO dto : dtos) {
            if (dto.getTelephone().equals(dto.getContactTuteur()))
                throw new ResourceNotFoundException("L'apprenant ne saurais avoir le même numéro de téléphone que son tuteur");
        }
        List<Etudiant> list = mapper.asEntityList(dtos);
        list.forEach(this::construitEtudiant);
        return  ((List<Etudiant>) repository.saveAll(list))
                .stream()
                .map(this::buildLiteEtudiantDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        Etudiant etudiant = findById(id);
        if (!getAllInscriptionsForEtudiant(etudiant).isEmpty())
            throw new ResourceNotFoundException("L'apprenant avec l'id " + id + " ne peut pas etre supprimé car elle est utilisé par d'autre ressource");
        repository.delete(etudiant);
    }

    private Set<LiteInscriptionDTO> getAllInscriptionsForEtudiantDto(Etudiant etudiant) {
        return inscriptionRepository.findAllByEtudiant(etudiant).stream().map(this::buildInscriptionLiteDto).collect(Collectors.toSet());
    }

    private LiteInscriptionDTO buildInscriptionLiteDto(Inscription entity) {
        LiteInscriptionDTO lite = new LiteInscriptionDTO(entity);
        lite.setSession(new LiteSessionDTO(entity.getSession()));
        lite.setCampus(new LiteCampusDTO(getCampus(entity.getCampusId())));
        lite.setCompte(getCompteForInscription(entity));
        lite.setExamen(getExamenForInscription(entity));
        lite.setTestModule(getTestModuleForInscription(entity));
        return lite;
    }

    private LiteTestModuleDTO getTestModuleForInscription(Inscription inscription) {
        return buildTestModuleLiteDto(testModuleRepository.findByInscription(inscription));
    }

    private LiteTestModuleDTO buildTestModuleLiteDto(TestModule entity) {
        if (entity == null) return null;
        LiteTestModuleDTO lite = new LiteTestModuleDTO(entity);
        Set<LiteEvaluationTestDTO> evaluations = getAllEvaluationsForTestModule(entity);
        lite.setEvaluations(evaluations);
        lite.setMoyenne(calculMoyenneTestModule(evaluations));
        return lite;
    }

    private Set<LiteEvaluationTestDTO> getAllEvaluationsForTestModule(TestModule testModule) {
        return evaluationTestRepository.findAllByTestModule(testModule).stream().map(this::buildEpreuveLiteDto).collect(Collectors.toSet());
    }

    private LiteEvaluationTestDTO buildEpreuveLiteDto(EvaluationTest evaluation) {
        LiteEvaluationTestDTO lite = new LiteEvaluationTestDTO(evaluation);
        lite.setModuleFormation(new LiteModuleFormationDTO(evaluation.getModuleFormation()));
        return lite;
    }

    private Float calculMoyenneTestModule(Set<LiteEvaluationTestDTO> evaluations) {
        Float moyenne = 0F;
        for (LiteEvaluationTestDTO test : evaluations) {
            moyenne += test.getNote();
        }
        return !evaluations.isEmpty() ? moyenne/evaluations.size() : 0;
    }

    private LiteExamenDTO getExamenForInscription(Inscription inscription) {
        return buildExamenLiteDto(examenRepository.findByInscription(inscription));
    }

    private LiteExamenDTO buildExamenLiteDto(Examen entity) {
        if (entity == null) return null;
        LiteExamenDTO lite = new LiteExamenDTO(entity);
        Set<LiteEpreuveDTO> epreuves = getAllEpreuvessForExamen(entity);
        lite.setEpreuves(epreuves);
        lite.setMoyenne(calculMoyenneExamen(epreuves));
        lite.setAppreciation(buildAppreciation(lite.getMoyenne()));
        lite.setTotalFraisPension(calculTotalPension(epreuves));
        lite.setTotalFraisRattrapage(calculTotalRatrappage(epreuves));
        return lite;
    }

    // 20/20, Excellent ; 16/20 à 19/20, Très bien ; 14/20 à 16/20, Bien ; 12/20 à 13/20, Assez bien ;
    // 10/20 à 11/20, Passable ; 5/20 à 8/20, Insuffisant ; 0/20 à 4/20, Médiocre.
    private String buildAppreciation(Float moyenne) {
        int note = moyenne.intValue();
        if (note >= 17) {
            return "Sehr Gut Bestanden";
        } else if (note >= 13) {
            return "Gut Bestanden";
        } else if (note >= 10) {
            return "Bestanden";
        } else {
            return "Nicht Bestanden";
        }
    }

    private Set<LiteEpreuveDTO> getAllEpreuvessForExamen(Examen examen) {
        return epreuveRepository.findAllByExamen(examen).stream().map(this::buildEpreuveLiteDto).collect(Collectors.toSet());
    }

    private LiteEpreuveDTO buildEpreuveLiteDto(Epreuve entity) {
        LiteEpreuveDTO lite = new LiteEpreuveDTO(entity);
        LiteUniteDTO liteUniteDTO = new LiteUniteDTO(entity.getUnite());
        liteUniteDTO.setNiveau(new LiteNiveauDTO(entity.getUnite().getNiveau()));
        lite.setUnite(liteUniteDTO);
        return lite;
    }

    private Float calculMoyenneExamen(Set<LiteEpreuveDTO> epreuves) {
        Float moyenne = 0F;
        for (LiteEpreuveDTO epreuve : epreuves) {
            if (!epreuve.getEstValidee()) {
                return 0F;
            }
            moyenne += epreuve.getNoteObtenue();
        }
        return !epreuves.isEmpty() ? moyenne/epreuves.size() : 0;
    }

    private BigDecimal calculTotalRatrappage(Set<LiteEpreuveDTO> epreuves) {
        BigDecimal totalFraisPension = BigDecimal.valueOf(0.0);
        for (LiteEpreuveDTO epreuve : epreuves) {
            totalFraisPension = totalFraisPension.add(epreuve.getUnite().getNiveau().getFraisRattrapage());
        }
        return totalFraisPension;
    }

    private BigDecimal calculTotalPension(Set<LiteEpreuveDTO> epreuves) {
        BigDecimal totalFraisRattrapage = BigDecimal.valueOf(0.0);
        for (LiteEpreuveDTO epreuve : epreuves) {
            if (!epreuve.getEstValidee()) totalFraisRattrapage = totalFraisRattrapage.add(epreuve.getUnite().getNiveau().getFraisRattrapage());
        }
        return totalFraisRattrapage;
    }

    private LiteCompteDTO getCompteForInscription(Inscription inscription) {
        return buildCompteLiteDto(compteRepository.findByInscription(inscription));
    }

    private LiteCompteDTO buildCompteLiteDto(Compte entity) {
        if (entity == null) return null;
        LiteCompteDTO lite = new LiteCompteDTO(entity);
        CalculTotals calcul = calculSolde(getAllPaiementsForCompte(entity));
        lite.setSolde(calcul.getSolde());
        lite.setResteApayer(calcul.getResteApayer());
        lite.setPaiements(getAllPaiementsForCompteDto(entity));
        return lite;
    }

    private List<Paiement> getAllPaiementsForCompte(Compte compte) {
        return paiementRepository.findAllByCompte(compte);
    }

    private Set<LitePaiementDTO> getAllPaiementsForCompteDto(Compte compte) {
        return paiementRepository.findAllByCompte(compte).stream().map(this::buildPaiementLiteDto).collect(Collectors.toSet());
    }

    private LitePaiementDTO buildPaiementLiteDto(Paiement entity) {
        LitePaiementDTO lite = new LitePaiementDTO();
        lite.setId(entity.getId());
        lite.setRefPaiement(entity.getRefPaiement());
        lite.setMontant(entity.getMontant());
        lite.setDatePaiement(entity.getDatePaiement());
        lite.setModePaiement(new LiteModePaiementDTO(entity.getModePaiement()));
        lite.setRubrique(new LiteRubriqueDTO(entity.getRubrique()));
        return lite;
    }

    private CalculTotals calculSolde(List<Paiement> paiements) {
        CalculTotals calcul = new CalculTotals();
        BigDecimal solde = BigDecimal.valueOf(0.0);
        BigDecimal reste = BigDecimal.valueOf(0.0);
        for (Paiement paiement : paiements) {
            BigDecimal netApayer = paiement.getCompte().getInscription().getSession().getNiveau().getFraisPension().add(paiement.getCompte().getInscription().getSession().getNiveau().getFraisInscription());
            solde = solde.add(paiement.getMontant());
            reste = netApayer.subtract(solde);
        }
        calcul.setSolde(solde);
        calcul.setResteApayer(reste);
        return calcul;
    }

    private Campus getCampus(Long campusId) {
        return campusRepository.findById(campusId).orElse(null);
    }

    @Override
    public Etudiant findById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("L'apprenant avec l'id " + id + " n'existe pas")
        );
    }

    @Override
    public EtudiantDTO getOne(Long id) {
        return buildEtudiantDto(findById(id));
    }

    @Override
    public List<EtudiantBranchDTO> findAll() {
        //return ((List<Etudiant>) repository.findAll()).stream().map(this::buildLiteEtudiantDto).collect(Collectors.toList());
        return null;
    }

    @Override
    public Page<LiteEtudiantDTO> findAll(Pageable pageable) {
        Page<Etudiant> entityPage = repository.findAll(pageable);
        List<Etudiant> entities = entityPage.getContent();
        return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
    }

    @Override
    public void update(EtudiantRequestDTO dto, Long id) {
        if (dto.getTelephone().equals(dto.getContactTuteur()))
            throw new ResourceNotFoundException("L'apprenant ne saurais avoir le même numéro de téléphone que son tuteur");
        Etudiant exist = findById(id);
        dto.setId(exist.getId());
        buildEtudiantDto(repository.save(construitEtudiant(mapper.asEntity(dto))));
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
    public boolean equalsByDto(EtudiantRequestDTO dto, Long id) {
        Etudiant ressource = repository.findByTelephoneOrEmail(dto.getTelephone(), dto.getEmail()).orElse(null);
        if (ressource == null) return false;
        return !ressource.getId().equals(id);
    }

    @Override
    public boolean findByTelephoneAndEmail(String telephone, String email) {
        return repository.findByTelephoneOrEmail(telephone, email).isPresent();
    }

    @Override
    public Etudiant findByMatricule(String matricule) {
        return repository.findByMatricule(matricule).orElseThrow(
                () -> new ResourceNotFoundException("L'apprenant avec le matricule " + matricule + " n'existe pas")
        );
    }

    @Override
    public List<EtudiantBranchDTO> getAllBySession(Long sessionId, Long salleId, Long niveauId) {
        /*List<LiteEtudiantDTO> list = new ArrayList<>();
        if (sessionId == null || sessionId <= 0) {
            return ((List<Etudiant>) repository.findAll())
                    .stream()
                    .filter(e -> findBySession(e, sessionId))
                    .map(this::buildLiteEtudiantDto)
                    .collect(Collectors.toList());
        } else if (salleId == null || salleId <= 0 && niveauId > 0) {
            return ((List<Etudiant>) repository.findAll())
                    .stream()
                    .filter(e -> findByNiveau(e, niveauId))
                    .map(this::buildLiteEtudiantDto)
                    .collect(Collectors.toList());
        } else if (niveauId == null || niveauId <= 0 && salleId > 0) {
            return ((List<Etudiant>) repository.findAll())
                    .stream()
                    .filter(e -> findBySalle(e, salleId))
                    .map(this::buildLiteEtudiantDto)
                    .collect(Collectors.toList());
        } else {
            return ((List<Etudiant>) repository.findAll())
                    .stream()
                    .filter(e -> findBySalleAndNiveau(e, salleId, niveauId))
                    .map(this::buildLiteEtudiantDto)
                    .collect(Collectors.toList());
        }*/
        return null;
    }

    private boolean findBySession(Etudiant etudiant, Long sessionId) {
        List<Inscription> inscriptions = getAllInscriptionsForEtudiant(etudiant);
        for (Inscription inscription : inscriptions) {
            if (Objects.equals(inscription.getSession().getId(), sessionId)) return true;
        }
        return false;
    }

    private boolean findByNiveau(Etudiant etudiant, Long niveauId) {
        List<Inscription> inscriptions = getAllInscriptionsForEtudiant(etudiant);
        for (Inscription inscription : inscriptions) {
            if (Objects.equals(inscription.getSession().getNiveau().getId(), niveauId))
                return true;
        }
        return false;
    }

    private boolean findBySalle(Etudiant etudiant, Long salleId) {
        List<Inscription> inscriptions = getAllInscriptionsForEtudiant(etudiant);
        for (Inscription inscription : inscriptions) {
            if (findBySalleInOccupations(inscription.getSession().getOccupations(), salleId))
                return true;
        }
        return false;
    }

    private boolean findBySalleAndNiveau(Etudiant etudiant, Long salleId, Long niveauId) {
        List<Inscription> inscriptions = getAllInscriptionsForEtudiant(etudiant);
        for (Inscription inscription : inscriptions) {
            if (Objects.equals(inscription.getSession().getNiveau().getId(), niveauId) && findBySalleInOccupations(inscription.getSession().getOccupations(), salleId))
                return true;
        }
        return false;
    }

    private boolean findBySalleInOccupations(Set<OccupationSalle> occupations, Long salleId) {
        for (OccupationSalle Occupation : occupations) {
            if (Objects.equals(Occupation.getSalle().getId(), salleId)) return true;
        }
        return false;
    }

    @Override
    public List<EtudiantBranchDTO> getAllWithUnpaid() {
        /*return ((List<Etudiant>) repository.findAll())
                .stream()
                .filter(this::unpaid)
                .map(this::buildLiteEtudiantDto)
                .collect(Collectors.toList());*/
        return null;
    }

    private boolean unpaid(Etudiant etudiant) {
        List<Inscription> inscriptions = getAllInscriptionsForEtudiant(etudiant);
        for (Inscription inscription : inscriptions) {
            BigDecimal netApayer = inscription.getSession().getNiveau().getFraisPension().add(inscription.getSession().getNiveau().getFraisInscription());
            LiteCompteDTO liteCompte = getCompteForInscription(inscription);
            if (liteCompte.getSolde().compareTo(netApayer) != 0) return true;
        }
        return false;
    }

    private List<Inscription> getAllInscriptionsForEtudiant(Etudiant etudiant) {
        return inscriptionRepository.findAllByEtudiant(etudiant);
    }
}
