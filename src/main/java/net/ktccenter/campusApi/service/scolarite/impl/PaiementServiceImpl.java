package net.ktccenter.campusApi.service.scolarite.impl;

import net.ktccenter.campusApi.dao.administration.CampusRepository;
import net.ktccenter.campusApi.dao.scolarite.PaiementRepository;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportPaiementRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LitePaiementDTO;
import net.ktccenter.campusApi.dto.reponse.branch.PaiementBranchDTO;
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

  private LitePaiementDTO buildLitePaiementDto(Paiement paiement) {
    LitePaiementDTO dto = mapper.asLite(paiement);
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
          listDtos.add(buildLitePaiementDto(repository.save(paiement)));
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
  public Page<LitePaiementDTO> findAll(Pageable pageable) {
    Page<Paiement> entityPage = repository.findAll(pageable);
    List<Paiement> entities = entityPage.getContent();
    return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
  }


  @Override
  public void update(PaiementRequestDTO dto, Long id) {
    Paiement exist = findById(id);
      Paiement paiement = mapper.asEntity(dto);
      paiement.setId(exist.getId());
      BigDecimal netApayer = paiement.getCompte().getInscription().getSession().getNiveau().getFraisPension().add(paiement.getCompte().getInscription().getSession().getNiveau().getFraisInscription());
      BigDecimal netPayee = paiement.getCompte().getSolde();
      BigDecimal resteAPayer = netApayer.subtract(netPayee);
      if (paiement.getMontant().compareTo(resteAPayer) > 0)
          throw new ResourceNotFoundException("Le montant paiement " + paiement.getMontant() + " n'peut pas supèrieur au reste à payer " + resteAPayer);
    buildPaiementDto(repository.save(paiement));
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
  public List<LitePaiementDTO> findAllByCampus(Long campusId) {
    return ((List<Paiement>) repository.findAll())
            .stream()
            .filter(p -> findByCampus(p, campusId))
            .map(this::buildLitePaiementDto)
            .collect(Collectors.toList());
  }

  private boolean findByCampus(Paiement paiement, Long campusId) {
    return paiement.getCampusId().equals(campusId);
  }
}