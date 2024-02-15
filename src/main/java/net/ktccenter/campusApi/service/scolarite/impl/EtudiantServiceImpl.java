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
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusForInscriptionDTO;
import net.ktccenter.campusApi.dto.lite.cours.*;
import net.ktccenter.campusApi.dto.lite.scolarite.*;
import net.ktccenter.campusApi.dto.reponse.branch.EtudiantBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.EtudiantDTO;
import net.ktccenter.campusApi.dto.request.scolarite.EtudiantRequestDTO;
import net.ktccenter.campusApi.entities.administration.Branche;
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
import net.ktccenter.campusApi.service.MainService;
import net.ktccenter.campusApi.service.administration.UserService;
import net.ktccenter.campusApi.service.scolarite.EtudiantService;
import net.ktccenter.campusApi.utils.MyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EtudiantServiceImpl extends MainService implements EtudiantService {
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
        return buildEtudiantDto(repository.save(construitEtudiant(mapper.asEntity(dto), dto.getBrancheId())));
    }

    private Etudiant construitEtudiant(Etudiant etudiant, Long brancheId) {
        Branche branche = brancheRepository.findById(brancheId).orElse(null);
        if (branche == null)
            throw new ResourceNotFoundException("Aucune branche avec l'id " + brancheId);
        // On vérifie que l'etudiant à une adresse mail, si oui on creer son compte utilisateur
        User user = userService.createUser(etudiant.getNom(), etudiant.getPrenom(), etudiant.getEmail().toLowerCase(), "ROLE_ETUDIANT", etudiant.getImageUrl(), null, null, false, branche);
        etudiant.setUser(user);
        etudiant.setBranche(branche);
        // On génére le matricule de l"étudiant
        etudiant.setMatricule(MyUtils.GenerateMatricule("DEFAULT-STUDENT"));
        return etudiant;
    }

    private LiteEtudiantDTO buildLiteEtudiantDto(Etudiant etudiant) {
        //dto.setInscriptions(getAllInscriptionsForEtudiantDto(etudiant));
        return mapper.asLite(etudiant);
    }

    @Override
    public List<LiteEtudiantDTO> save(List<ImportEtudiantRequestDTO> dtos) {
        for (ImportEtudiantRequestDTO dto : dtos) {
            if (dto.getTelephone().equals(dto.getContactTuteur()))
                throw new ResourceNotFoundException("L'apprenant ne saurais avoir le même numéro de téléphone que son tuteur");
        }
        List<Etudiant> list = new ArrayList<>();
        for (ImportEtudiantRequestDTO dto : dtos) {
            Etudiant etudiant = mapper.asEntity(dto);
            etudiant = importConstruitEtudiant(etudiant, dto.getBrancheCode());
            list.add(etudiant);
        }
        return  ((List<Etudiant>) repository.saveAll(list))
                .stream()
                .map(this::buildLiteEtudiantDto)
                .collect(Collectors.toList());
    }

    private Etudiant importConstruitEtudiant(Etudiant etudiant, String brancheCode) {
        Branche branche = brancheRepository.findByCode(brancheCode).orElse(null);
        if (branche == null)
            throw new ResourceNotFoundException("Aucune branche avec le code " + brancheCode);
        // On vérifie que l'etudiant à une adresse mail, si oui on creer son compte utilisateur
        User user = userService.createUser(etudiant.getNom(), etudiant.getPrenom(), etudiant.getEmail().toLowerCase(), "ROLE_ETUDIANT", etudiant.getImageUrl(), null, null, false, branche);
        etudiant.setUser(user);

        // On génére le matricule de l"étudiant
        etudiant.setMatricule(MyUtils.GenerateMatricule("DEFAULT-STUDENT"));
        return etudiant;
    }

    @Override
    public void deleteById(Long id) {
        Etudiant etudiant = findById(id);
        if (!getAllInscriptionsForEtudiant(etudiant).isEmpty())
            throw new ResourceNotFoundException("L'apprenant avec l'id " + id + " ne peut pas etre supprimé car elle est utilisé par d'autre ressource");
        repository.delete(etudiant);
    }

    private Set<LiteInscriptionForEtudiantDTO> getAllInscriptionsForEtudiantDto(Etudiant etudiant) {
        return inscriptionRepository.findAllByEtudiant(etudiant).stream().map(this::buildInscriptionLiteDto).collect(Collectors.toSet());
    }

    private LiteInscriptionForEtudiantDTO buildInscriptionLiteDto(Inscription entity) {
        LiteInscriptionForEtudiantDTO lite = new LiteInscriptionForEtudiantDTO(entity);
        lite.setSession(new LiteSessionForInscriptionDTO(entity.getSession()));
        lite.setCampus(new LiteCampusForInscriptionDTO(getCampus(entity.getCampusId())));
        lite.setCompte(getCompteForInscription(entity.getId()));
        lite.setExamen(getExamenForInscription(entity));
        lite.setTestModule(getTestModuleForInscription(entity));
        return lite;
    }

    private LiteTestModuleDTO getTestModuleForInscription(Inscription inscription) {
        return buildTestModuleLiteDto(testModuleRepository.findByInscription(inscription));
    }

    private LiteTestModuleDTO buildTestModuleLiteDto(TestModule entity) {
        if (entity == null) return new LiteTestModuleDTO();
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
        if (entity == null) return new LiteExamenDTO();
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
        lite.setUnite(new LiteUniteDTO(entity.getUnite()));
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

    private LiteCompteForInscriptionDTO getCompteForInscription(Long inscriptionId) {
        return buildCompteLiteDto(compteRepository.findByInscriptionId(inscriptionId));
    }

    private LiteCompteForInscriptionDTO buildCompteLiteDto(Compte entity) {
        if (entity == null) return new LiteCompteForInscriptionDTO();
        LiteCompteForInscriptionDTO lite = new LiteCompteForInscriptionDTO(entity);
        List<Paiement> paiements = paiementRepository.findAllByCompte(entity);
        CalculTotals calcul = calculSolde(paiements);
        lite.setSolde(calcul.getSolde());
        lite.setResteApayer(calcul.getResteApayer());
        lite.setPaiements(getAllPaiementsForCompteDto(paiements));
        return lite;
    }

    private Set<LitePaiementForInscriptionDTO> getAllPaiementsForCompteDto(List<Paiement> paiements) {
        return paiements.stream().map(LitePaiementForInscriptionDTO::new).collect(Collectors.toSet());
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

    private EtudiantDTO buildEtudiantDto(Etudiant etudiant) {
        EtudiantDTO dto = mapper.asDTO(etudiant);
        dto.setInscriptions(getAllInscriptionsForEtudiantDto(etudiant));
        return buildStateSurCompte(dto);
    }

    private EtudiantDTO buildStateSurCompte(EtudiantDTO dto) {
        BigDecimal soldeTotal = BigDecimal.valueOf(0.0);
        BigDecimal resteTotal = BigDecimal.valueOf(0.0);
        Integer nbrePaiement = 0;
        List<Inscription> inscriptions = getAllInscriptionsForEtudiantId(dto.getId());
        for (Inscription inscription : inscriptions) {
            Compte compte = compteRepository.findByInscription(inscription);
            List<Paiement> paiements = paiementRepository.findAllByCompte(compte);
            CalculTotals calcul = calculSolde(paiements);
            soldeTotal = soldeTotal.add(calcul.getSolde());
            resteTotal = resteTotal.add(calcul.getResteApayer());
            nbrePaiement = nbrePaiement + paiements.size();
        }
        dto.setSoldeTotal(soldeTotal);
        dto.setResteApayer(resteTotal);
        dto.setNbrePaiement(nbrePaiement);
        return dto;
    }

    @Override
    public List<EtudiantBranchDTO> findAll() {
        List<Etudiant> etudiants = (List<Etudiant>) repository.findAll();
        List<EtudiantBranchDTO> result = new ArrayList<>();
        if (hasGrantAuthorized()) {
            for (Branche b : getAllBranches()) {
                result.add(buildData(b, etudiants));
            }
        } else {
            result.add(buildData(getCurrentUserBranch(), etudiants));
        }
        return result;
    }

    private EtudiantBranchDTO buildData(Branche branche, List<Etudiant> etudiants) {
        List<LiteEtudiantDTO> data = etudiants.stream()
                .filter(e -> belongsToTheCurrentBranch(branche, e))
                .map(mapper::asLite)
                .collect(Collectors.toList());
        EtudiantBranchDTO dto = new EtudiantBranchDTO();
        dto.setBranche(brancheMapper.asLite(branche));
        dto.setData(data);
        return dto;
    }

    private boolean belongsToTheCurrentBranch(Branche branche, Etudiant e) {
        return e.getBranche().getId().equals(branche.getId());
    }

    @Override
    public Page<LiteEtudiantDTO> findAll(Pageable pageable) {
        Page<Etudiant> entityPage = repository.findAll(pageable);
        List<Etudiant> entities = entityPage.getContent();
        return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
    }

    @Override
    public EtudiantDTO update(EtudiantRequestDTO dto, Long id) {
        if (dto.getTelephone().equals(dto.getContactTuteur()))
            throw new ResourceNotFoundException("L'apprenant ne saurais avoir le même numéro de téléphone que son tuteur");
        Etudiant exist = findById(id);
        dto.setId(exist.getId());
        return buildEtudiantDto(repository.save(construitEtudiant(mapper.asEntity(dto), dto.getBrancheId())));
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
        if (sessionId > 0) {
            return findAll()
                    .stream()
                    .filter(e -> findBySession(e, sessionId))
                    .collect(Collectors.toList());
        } else if (salleId == null || salleId <= 0 && niveauId > 0) {
            return findAll()
                    .stream()
                    .filter(e -> findByNiveau(e, niveauId))
                    .collect(Collectors.toList());
        } else if (niveauId == null || niveauId <= 0 && salleId > 0) {
            return findAll()
                    .stream()
                    .filter(e -> findBySalle(e, salleId))
                    .collect(Collectors.toList());
        } else {
            return findAll()
                    .stream()
                    .filter(e -> findBySalleAndNiveau(e, salleId, niveauId))
                    .collect(Collectors.toList());
        }
    }

    private boolean findBySession(EtudiantBranchDTO etudiant, Long sessionId) {
        List<LiteEtudiantDTO> etudiants = etudiant.getData();
        for (LiteEtudiantDTO e : etudiants) {
            List<Inscription> inscriptions = getAllInscriptionsForEtudiantId(e.getId());
            for (Inscription inscription : inscriptions) {
                if (inscription.getSession().getId().equals(sessionId)) return true;
            }
        }
        return false;
    }

    private boolean findByNiveau(EtudiantBranchDTO etudiant, Long niveauId) {
        List<LiteEtudiantDTO> etudiants = etudiant.getData();
        for (LiteEtudiantDTO e : etudiants) {
            List<Inscription> inscriptions = getAllInscriptionsForEtudiantId(e.getId());
            for (Inscription inscription : inscriptions) {
                if (inscription.getSession().getNiveau().getId().equals(niveauId)) return true;
            }
        }
        return false;
    }

    private boolean findBySalle(EtudiantBranchDTO etudiant, Long salleId) {
        List<LiteEtudiantDTO> etudiants = etudiant.getData();
        for (LiteEtudiantDTO e : etudiants) {
            List<Inscription> inscriptions = getAllInscriptionsForEtudiantId(e.getId());
            for (Inscription inscription : inscriptions) {
                if (findBySalleInOccupations(inscription.getSession(), salleId)) return true;
            }
        }
        return false;
    }

    private boolean findBySalleAndNiveau(EtudiantBranchDTO etudiant, Long salleId, Long niveauId) {
        List<LiteEtudiantDTO> etudiants = etudiant.getData();
        for (LiteEtudiantDTO e : etudiants) {
            List<Inscription> inscriptions = getAllInscriptionsForEtudiantId(e.getId());
            for (Inscription inscription : inscriptions) {
                if (inscription.getSession().getNiveau().getId().equals(niveauId) && findBySalleInOccupations(inscription.getSession(), salleId))
                    return true;
            }
        }
        return false;
    }

    private boolean findBySalleInOccupations(Session session, Long salleId) {
        for (OccupationSalle Occupation : session.getOccupations()) {
            if (Occupation.getSalle().getId().equals(salleId)) return true;
        }
        return false;
    }

    @Override
    public List<EtudiantBranchDTO> getAllWithUnpaid() {
        return findAll()
                .stream()
                .filter(this::unpaid)
                .collect(Collectors.toList());
    }

    private boolean unpaid(EtudiantBranchDTO etudiant) {
        List<LiteEtudiantDTO> etudiants = etudiant.getData();
        for (LiteEtudiantDTO e : etudiants) {
            List<Inscription> inscriptions = getAllInscriptionsForEtudiantId(e.getId());
            for (Inscription inscription : inscriptions) {
                BigDecimal netApayer = inscription.getSession().getNiveau().getFraisPension().add(inscription.getSession().getNiveau().getFraisInscription());
                Compte compte = compteRepository.findByInscription(inscription);
                List<Paiement> paiements = paiementRepository.findAllByCompte(compte);
                CalculTotals calcul = calculSolde(paiements);
                if (calcul.getSolde().compareTo(netApayer) != 0) return true;
            }
        }
        return false;
    }

    private List<Inscription> getAllInscriptionsForEtudiant(Etudiant etudiant) {
        return inscriptionRepository.findAllByEtudiant(etudiant);
    }

    private List<Inscription> getAllInscriptionsForEtudiantId(Long etudiantId) {
        Etudiant e = findById(etudiantId);
        return inscriptionRepository.findAllByEtudiant(e);
    }
}
