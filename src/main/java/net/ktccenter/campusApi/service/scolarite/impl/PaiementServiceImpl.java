package net.ktccenter.campusApi.service.scolarite.impl;

import net.ktccenter.campusApi.dao.administration.CampusRepository;
import net.ktccenter.campusApi.dao.scolarite.PaiementRepository;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportPaiementRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.*;
import net.ktccenter.campusApi.dto.reponse.branch.PaiementBranchAndCampusDTO;
import net.ktccenter.campusApi.dto.reponse.branch.PaiementBranchDTO;
import net.ktccenter.campusApi.dto.reponse.branch.PaiementCampusDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.PaiementDTO;
import net.ktccenter.campusApi.dto.request.scolarite.PaiementRequestDTO;
import net.ktccenter.campusApi.entities.administration.Branche;
import net.ktccenter.campusApi.entities.administration.Campus;
import net.ktccenter.campusApi.entities.scolarite.Paiement;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.scolarite.PaiementMapper;
import net.ktccenter.campusApi.service.MainService;
import net.ktccenter.campusApi.service.scolarite.PaiementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaiementServiceImpl extends MainService implements PaiementService {
  private final PaiementRepository repository;
  private final PaiementMapper mapper;
  private final CampusRepository campusRepository;

  public PaiementServiceImpl(PaiementRepository repository, PaiementMapper mapper, CampusRepository campusRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.campusRepository = campusRepository;
  }

  @Override
  public PaiementDTO save(PaiementRequestDTO dto) {
      Paiement paiement = mapper.asEntity(dto);
      BigDecimal netApayer = paiement.getCompte().getInscription().getSession().getNiveau().getFraisPension().add(paiement.getCompte().getInscription().getSession().getNiveau().getFraisInscription());
      BigDecimal netPayee = paiement.getCompte().getSolde();
      BigDecimal resteAPayer = netApayer.subtract(netPayee);
      if (paiement.getMontant().compareTo(resteAPayer) > 0)
          throw new ResourceNotFoundException("Le montant paiement " + paiement.getMontant() + " n'peut pas supèrieur au reste à payer " + resteAPayer);
      return buildPaiementDto(repository.save(paiement));
  }

  private PaiementDTO buildPaiementDto(Paiement paiement) {
    PaiementDTO dto = mapper.asDTO(paiement);
    dto.setCampus(new LiteCampusDTO(getCampus(paiement.getCampusId())));
    return dto;
  }

  private Campus getCampus(Long campusId) {
    return campusRepository.findById(campusId).orElse(null);
  }

  @Override
  public List<LitePaiementDTO> save(List<ImportPaiementRequestDTO> dtos) {
      List<LitePaiementDTO> listDtos = new ArrayList<>();
      List<Paiement> list = mapper.asEntityList(dtos);
      for (Paiement paiement : list) {
          BigDecimal netApayer = paiement.getCompte().getInscription().getSession().getNiveau().getFraisPension().add(paiement.getCompte().getInscription().getSession().getNiveau().getFraisInscription());
          BigDecimal netPayee = paiement.getCompte().getSolde();
          BigDecimal resteAPayer = netApayer.subtract(netPayee);
          if (paiement.getMontant().compareTo(resteAPayer) > 0)
              throw new ResourceNotFoundException("Le montant paiement " + paiement.getMontant() + " n'peut pas supèrieur au reste à payer " + resteAPayer);
        listDtos.add(mapper.asLite(repository.save(paiement)));
      }
      return listDtos;
  }

  @Override
  public void deleteById(Long id) {
    Paiement Paiement = findById(id);
    repository.delete(Paiement);
  }

  @Override
  public Paiement findById(Long id) {
    return repository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Le paiement avec l'id " + id + " n'existe pas")
    );
  }

  @Override
  public PaiementDTO getOne(Long id) {
    return buildPaiementDto(findById(id));
  }

  @Override
  public List<PaiementBranchDTO> findAll() {
    List<Paiement> paiements = (List<Paiement>) repository.findAll();
    List<PaiementBranchDTO> result = new ArrayList<>();
    if (hasGrantAuthorized()) {
      for (Branche b : getAllBranches()) {
        result.add(buildData(b, paiements));
      }
    } else {
      result.add(buildData(getCurrentUserBranch(), paiements));
    }
    return result;
  }

  private PaiementBranchDTO buildData(Branche branche, List<Paiement> paiements) {
    PaiementBranchDTO dto = new PaiementBranchDTO();
    dto.setBranche(brancheMapper.asLite(branche));
    dto.setData(paiements.stream()
            .filter(e -> belongsToTheCurrentBranch(branche, e))
            .map(mapper::asLite)
            .collect(Collectors.toList()));
    return dto;
  }

  private boolean belongsToTheCurrentBranch(Branche branche, Paiement e) {
    return e.getCompte().getInscription().getSession().getBranche().getId().equals(branche.getId());
  }

  @Override
  public List<PaiementBranchAndCampusDTO> findAllAndGroupByBranchAndCampus() {
    List<PaiementBranchAndCampusDTO> result = new ArrayList<>();
    if (hasGrantAuthorized()) {
      for (Branche b : getAllBranches()) {
        result.add(buildPaiementByBranchAndCampus(b));
      }
    } else {
      result.add(buildPaiementByBranchAndCampus(getCurrentUserBranch()));
    }
    return result;
  }

  private PaiementBranchAndCampusDTO buildPaiementByBranchAndCampus(Branche branche) {
    List<PaiementCampusDTO> data = new ArrayList<>();
    PaiementBranchAndCampusDTO dto = new PaiementBranchAndCampusDTO();
    dto.setBranche(brancheMapper.asLite(branche));
    List<Campus> campusList = campusRepository.findAllByBranche(branche);
    for (Campus campus : campusList) {
      PaiementCampusDTO paiementCampusDTO = new PaiementCampusDTO();
      paiementCampusDTO.setCampus(new LiteCampusDTO(campus));
      List<Paiement> paiements = (List<Paiement>) repository.findAll();
      paiementCampusDTO.setData(paiements.stream()
              .filter(e -> belongsToTheCurrentBranch(branche, e))
              .filter(p -> findByCampus(p, campus.getId()))
              .map(this::buildLitePaiementDto)
              .collect(Collectors.toList()));
      data.add(paiementCampusDTO);
    }
    dto.setData(data);
    return dto;
  }

  @Override
  public Page<LitePaiementDTO> findAll(Pageable pageable) {
    Page<Paiement> entityPage = repository.findAll(pageable);
    List<Paiement> entities = entityPage.getContent();
    return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
  }


  @Override
  public PaiementDTO update(PaiementRequestDTO dto, Long id) {
    Paiement exist = findById(id);
      Paiement paiement = mapper.asEntity(dto);
      paiement.setId(exist.getId());
      BigDecimal netApayer = paiement.getCompte().getInscription().getSession().getNiveau().getFraisPension().add(paiement.getCompte().getInscription().getSession().getNiveau().getFraisInscription());
      BigDecimal netPayee = paiement.getCompte().getSolde();
      BigDecimal resteAPayer = netApayer.subtract(netPayee);
      if (paiement.getMontant().compareTo(resteAPayer) > 0)
          throw new ResourceNotFoundException("Le montant paiement " + paiement.getMontant() + " n'peut pas supèrieur au reste à payer " + resteAPayer);
    return buildPaiementDto(repository.save(paiement));
  }


  @Override
  public boolean existByRefPaiement(String ref) {
    return repository.findByRefPaiement(ref).isPresent();
  }

  @Override
  public boolean equalsByDto(PaiementRequestDTO dto, Long id) {
    Paiement ressource = repository.findByRefPaiement(dto.getRefPaiement()).orElse(null);
    if (ressource == null) return false;
    return !ressource.getId().equals(id);
  }

  @Override
  public Paiement findByRefPaiement(String ref) {
    return repository.findByRefPaiement(ref).orElseThrow(
            () ->  new ResourceNotFoundException("Le paiement avec la reference " + ref + " n'existe pas")
    );
  }

  @Override
  public List<LitePaiementForCampusDTO> findAllByCampus(Long campusId) {
    return ((List<Paiement>) repository.findAll())
            .stream()
            .filter(p -> findByCampus(p, campusId))
            .map(this::buildLitePaiementDto)
            .collect(Collectors.toList());
  }

  private LitePaiementForCampusDTO buildLitePaiementDto(Paiement paiement) {
    LitePaiementForCampusDTO dto = new LitePaiementForCampusDTO(paiement);
    dto.setModePaiement(new LiteModePaiementDTO(paiement.getModePaiement()));
    dto.setRubrique(new LiteRubriqueDTO(paiement.getRubrique()));
    LiteCompteForPaiementDTO liteCompte = new LiteCompteForPaiementDTO(paiement.getCompte());
    LiteInscriptionForPaiementDTO liteInscription = new LiteInscriptionForPaiementDTO(paiement.getCompte().getInscription());
    liteInscription.setEtudiant(new LiteEtudiantForNoteDTO(paiement.getCompte().getInscription().getEtudiant()));
    LiteSessionForCompteDTO liteSession = new LiteSessionForCompteDTO(paiement.getCompte().getInscription().getSession());
    liteSession.setNiveau(new LiteNiveauForSessionDTO(paiement.getCompte().getInscription().getSession().getNiveau()));
    liteInscription.setSession(liteSession);
    liteCompte.setInscription(liteInscription);
    dto.setCompte(liteCompte);
    return dto;
  }

  private boolean findByCampus(Paiement paiement, Long campusId) {
    return paiement.getCampusId().equals(campusId);
  }
}