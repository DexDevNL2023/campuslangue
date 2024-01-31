package net.ktccenter.campusApi.service.scolarite.impl;

import net.ktccenter.campusApi.dao.administration.CampusRepository;
import net.ktccenter.campusApi.dao.scolarite.CompteRepository;
import net.ktccenter.campusApi.dao.scolarite.PaiementRepository;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportCompteRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteCompteDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteModePaiementDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LitePaiementDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteRubriqueDTO;
import net.ktccenter.campusApi.dto.reponse.branch.CompteBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.CompteDTO;
import net.ktccenter.campusApi.dto.request.scolarite.CompteRequestDTO;
import net.ktccenter.campusApi.entities.administration.Branche;
import net.ktccenter.campusApi.entities.administration.Campus;
import net.ktccenter.campusApi.entities.scolarite.CalculTotals;
import net.ktccenter.campusApi.entities.scolarite.Compte;
import net.ktccenter.campusApi.entities.scolarite.Paiement;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.scolarite.CompteMapper;
import net.ktccenter.campusApi.service.MainService;
import net.ktccenter.campusApi.service.scolarite.CompteService;
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
public class CompteServiceImpl extends MainService implements CompteService {
    private final CompteRepository repository;
    private final CompteMapper mapper;
    private final PaiementRepository paiementRepository;
    private final CampusRepository campusRepository;

    public CompteServiceImpl(CompteRepository repository, CompteMapper mapper, PaiementRepository paiementRepository, CampusRepository campusRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.paiementRepository = paiementRepository;
        this.campusRepository = campusRepository;
    }

    @Override
    public CompteDTO save(CompteRequestDTO dto) {
        return buildCompteDto(repository.save(mapper.asEntity(dto)));
    }

    private CompteDTO buildCompteDto(Compte compte) {
        CompteDTO dto = mapper.asDTO(compte);
        dto.getInscription().setCampus(new LiteCampusDTO(getCampus(compte.getInscription().getCampusId())));
        CalculTotals calcul = calculSolde(getAllPaiementsForCompte(compte));
        dto.setSolde(calcul.getSolde());
        dto.setResteApayer(calcul.getResteApayer());
        dto.setPaiements(getAllPaiementsForCompteDto(compte));
        return dto;
    }

    private Campus getCampus(Long campusId) {
        return campusRepository.findById(campusId).orElse(null);
    }

    private LiteCompteDTO buildLiteCompteDto(Compte compte) {
        LiteCompteDTO dto = mapper.asLite(compte);
        dto.getInscription().setCampus(new LiteCampusDTO(getCampus(compte.getInscription().getCampusId())));
        CalculTotals calcul = calculSolde(getAllPaiementsForCompte(compte));
        dto.setSolde(calcul.getSolde());
        dto.setResteApayer(calcul.getResteApayer());
        dto.setPaiements(getAllPaiementsForCompteDto(compte));
        return dto;
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

    @Override
    public List<LiteCompteDTO> save(List<ImportCompteRequestDTO> dtos) {
        return  ((List<Compte>) repository.saveAll(mapper.asEntityList(dtos)))
                .stream()
                .map(this::buildLiteCompteDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        Compte compte = findById(id);
        if (!getAllPaiementsForCompte(compte).isEmpty())
            throw new ResourceNotFoundException("Le compte avec l'id " + id + " ne peut pas etre supprimé car elle est utilisé par d'autre ressource");
        repository.delete(compte);
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

    @Override
    public Compte findById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Le compte avec l'id " + id + " n'existe pas")
        );
    }

    @Override
    public CompteDTO getOne(Long id) {
        return buildCompteDto(findById(id));
    }

    @Override
    public List<CompteBranchDTO> findAll() {
        //return ((List<Compte>) repository.findAll()).stream().map(this::buildLiteCompteDto).collect(Collectors.toList());
        List<Compte> comptes = (List<Compte>) repository.findAll();
        List<CompteBranchDTO> result = new ArrayList<>();
        if (hasGrantAuthorized()) {
            for (Branche b : getAllBranches()) {
                result.add(buildData(b, comptes));
            }
        } else {
            result.add(buildData(getCurrentUserBranch(), comptes));
        }
        return result;
    }

    private CompteBranchDTO buildData(Branche branche, List<Compte> comptes) {
        CompteBranchDTO dto = new CompteBranchDTO();
        dto.setBranche(brancheMapper.asLite(branche));
        dto.setData(comptes.stream()
                .filter(e -> belongsToTheCurrentBranch(branche, e))
                .map(mapper::asLite)
                .collect(Collectors.toList()));
        return dto;
    }

    private boolean belongsToTheCurrentBranch(Branche branche, Compte e) {
        return e.getInscription().getSession().getBranche().getId().equals(branche.getId());
    }

    @Override
    public Page<LiteCompteDTO> findAll(Pageable pageable) {
        Page<Compte> entityPage = repository.findAll(pageable);
        List<Compte> entities = entityPage.getContent();
        return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
    }


    @Override
    public CompteDTO update(CompteRequestDTO dto, Long id) {
        Compte exist = findById(id);
        dto.setId(exist.getId());
        return buildCompteDto(repository.save(mapper.asEntity(dto)));
    }

    @Override
    public boolean existByCode(String code) {
        return repository.findByCode(code).isPresent();
    }

    @Override
    public boolean equalsByDto(CompteRequestDTO dto, Long id) {
        Compte ressource = repository.findByCode(dto.getCode()).orElse(null);
        if (ressource == null) return false;
        return !ressource.getId().equals(id);
    }

    @Override
    public Compte findByCode(String code) {
        return repository.findByCode(code).orElseThrow(
                () ->  new ResourceNotFoundException("Le compte avec le code " + code + " n'existe pas")
        );
    }
}