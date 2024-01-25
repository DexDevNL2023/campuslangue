package net.ktccenter.campusApi.service.scolarite.impl;

import net.ktccenter.campusApi.dao.scolarite.ModePaiementRepository;
import net.ktccenter.campusApi.dao.scolarite.PaiementRepository;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportModePaiementRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteModePaiementDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LitePaiementDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteRubriqueDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.ModePaiementDTO;
import net.ktccenter.campusApi.dto.request.scolarite.ModePaiementRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.ModePaiement;
import net.ktccenter.campusApi.entities.scolarite.Paiement;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.scolarite.ModePaiementMapper;
import net.ktccenter.campusApi.service.scolarite.ModePaiementService;
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
public class ModePaiementServiceImpl implements ModePaiementService {
  private final ModePaiementRepository repository;
  private final ModePaiementMapper mapper;
  private final PaiementRepository paiementRepository;

  public ModePaiementServiceImpl(ModePaiementRepository repository, ModePaiementMapper mapper, PaiementRepository paiementRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.paiementRepository = paiementRepository;
  }

  @Override
  public ModePaiementDTO save(ModePaiementRequestDTO dto) {
    return buildModePaiementDto(repository.save(mapper.asEntity(dto)));
  }

  private ModePaiementDTO buildModePaiementDto(ModePaiement modePaiement) {
    ModePaiementDTO dto = mapper.asDTO(modePaiement);
    dto.setPaiements(getAllPaiementsForModePaiement(modePaiement));
    return dto;
  }

  @Override
  public List<LiteModePaiementDTO> save(List<ImportModePaiementRequestDTO> dtos) {
    return  ((List<ModePaiement>) repository.saveAll(mapper.asEntityList(dtos)))
            .stream()
            .map(mapper::asLite)
            .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    ModePaiement modePaiement = findById(id);
    if (!getAllPaiementsForModePaiement(modePaiement).isEmpty())
      throw new ResourceNotFoundException("Le mode de paiement avec l'id " + id + " ne peut pas etre supprimé car elle est utilisé par d'autre ressource");
    repository.delete(modePaiement);
  }

  private Set<LitePaiementDTO> getAllPaiementsForModePaiement(ModePaiement modePaiement) {
    return paiementRepository.findAllByModePaiement(modePaiement).stream().map(this::buildPaiementLiteDto).collect(Collectors.toSet());
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
  public ModePaiement findById(Long id) {
    return repository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Le mode de paiement avec l'id " + id + " n'existe pas")
    );
  }

  @Override
  public ModePaiementDTO getOne(Long id) {
    return buildModePaiementDto(findById(id));
  }

  @Override
  public List<LiteModePaiementDTO> findAll() {
    return ((List<ModePaiement>) repository.findAll()).stream().map(mapper::asLite).collect(Collectors.toList());
  }

  @Override
  public Page<LiteModePaiementDTO> findAll(Pageable pageable) {
    Page<ModePaiement> entityPage = repository.findAll(pageable);
    List<ModePaiement> entities = entityPage.getContent();
    return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
  }


  @Override
  public ModePaiementDTO update(ModePaiementRequestDTO dto, Long id) {
    ModePaiement exist = findById(id);
    dto.setId(exist.getId());
    return buildModePaiementDto(repository.save(mapper.asEntity(dto)));
  }


  @Override
  public boolean existByCode(String code) {
    return repository.findByCode(code).isPresent();
  }

  @Override
  public boolean equalsByDto(ModePaiementRequestDTO dto, Long id) {
    ModePaiement ressource = repository.findByCode(dto.getCode()).orElse(null);
    if (ressource == null) return false;
    return !ressource.getId().equals(id);
  }

  @Override
  public ModePaiement findByCode(String code) {
    return repository.findByCode(code).orElseThrow(
            () ->  new ResourceNotFoundException("Le mode de paiement avec le code " + code + " n'existe pas")
    );
  }

  @Override
  public ModePaiement findByCodeAndLibelle(String code, String libelle) {
    return repository.findByCodeAndLibelle(code, libelle).orElseThrow(
            () ->  new ResourceNotFoundException("Le mode de paiement avec le code " + code + " et le libelle "+ libelle +" n'existe pas")
    );
  }
}