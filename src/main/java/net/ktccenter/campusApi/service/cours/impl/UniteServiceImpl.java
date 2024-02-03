package net.ktccenter.campusApi.service.cours.impl;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.dao.cours.EpreuveRepository;
import net.ktccenter.campusApi.dao.cours.ExamenRepository;
import net.ktccenter.campusApi.dao.cours.UniteRepository;
import net.ktccenter.campusApi.dao.scolarite.EtudiantRepository;
import net.ktccenter.campusApi.dao.scolarite.InscriptionRepository;
import net.ktccenter.campusApi.dto.importation.cours.ImportUniteRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteUniteDTO;
import net.ktccenter.campusApi.dto.reponse.cours.UniteDTO;
import net.ktccenter.campusApi.dto.request.cours.UniteRequestDTO;
import net.ktccenter.campusApi.entities.cours.Epreuve;
import net.ktccenter.campusApi.entities.cours.Examen;
import net.ktccenter.campusApi.entities.cours.Unite;
import net.ktccenter.campusApi.entities.scolarite.Etudiant;
import net.ktccenter.campusApi.entities.scolarite.Inscription;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.cours.UniteMapper;
import net.ktccenter.campusApi.service.cours.UniteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UniteServiceImpl implements UniteService {
  private final UniteRepository repository;
  private final UniteMapper mapper;
  private final EpreuveRepository epreuveRepository;
  private final EtudiantRepository etudiantRepository;
  private final InscriptionRepository inscriptionRepository;
  private final ExamenRepository examenRepository;

  public UniteServiceImpl(UniteRepository repository, UniteMapper mapper, EpreuveRepository epreuveRepository, EtudiantRepository etudiantRepository, InscriptionRepository inscriptionRepository, ExamenRepository examenRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.epreuveRepository = epreuveRepository;
    this.etudiantRepository = etudiantRepository;
    this.inscriptionRepository = inscriptionRepository;
    this.examenRepository = examenRepository;
  }

  @Override
  public UniteDTO save(UniteRequestDTO dto) {
    Unite unite = repository.save(mapper.asEntity(dto));
    verifyExamen(unite);
    return mapper.asDTO(unite);
  }

  private void verifyExamen(Unite unite) {
    List<Etudiant> etudiants = (List<Etudiant>) etudiantRepository.findAll();
    for (Etudiant etudiant : etudiants) {
      List<Inscription> inscriptions = inscriptionRepository.findAllByEtudiant(etudiant);
      for (Inscription inscription : inscriptions) {
        Examen examen = examenRepository.findByInscription(inscription);
        if (examen != null) {
          List<Epreuve> epreuves = epreuveRepository.findAllByExamenIdAndUniteId(examen.getId(), unite.getId());
          if (epreuves.isEmpty()) {
            createAllEpreuvesForExamen(examen, unite);
          }
        }
      }
    }
  }

  private void createAllEpreuvesForExamen(Examen examen, Unite unite) {
    Epreuve epreuve = new Epreuve();
    epreuve.setUnite(unite);
    epreuve.setExamen(examen);
    epreuveRepository.save(epreuve);
  }

  @Override
  public List<LiteUniteDTO> save(List<ImportUniteRequestDTO> dtos) {
    return  ((List<Unite>) repository.saveAll(mapper.asEntityList(dtos)))
            .stream()
            .map(mapper::asLite)
            .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    Unite unite = findById(id);
    if (!getAllEpreuvesForUnite(unite).isEmpty())
        throw new ResourceNotFoundException("L'unite avec l'id " + id + " ne peut pas etre supprimée car elle est utilisée par d'autre ressource");
    repository.deleteById(id);
  }

  private Set<Epreuve> getAllEpreuvesForUnite(Unite unite) {
    return new HashSet<>(epreuveRepository.findAllByUnite(unite));
  }

  @Override
  public UniteDTO getOne(Long id) {
    return mapper.asDTO(findById(id));
  }

  @Override
  public Unite findById(Long id) {
    return repository.findById(id).orElseThrow(
            () ->  new ResourceNotFoundException("L'unite avec l'id " + id + " n'existe pas")
    );
  }

  @Override
  public List<LiteUniteDTO> findAll() {
    return ((List<Unite>) repository.findAll()).stream().map(mapper::asLite).collect(Collectors.toList());
  }

  @Override
  public Page<LiteUniteDTO> findAll(Pageable pageable) {
    Page<Unite> entityPage = repository.findAll(pageable);
    List<Unite> entities = entityPage.getContent();
    return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
  }

  @Override
  public UniteDTO update(UniteRequestDTO dto, Long id) {
    Unite exist =  findById(id);
    dto.setId(exist.getId());
    Unite unite = repository.save(mapper.asEntity(dto));
    verifyExamen(unite);
    return mapper.asDTO(unite);
  }

  @Override
  public boolean existsByCode(String code) {
    return repository.findByCode(code).isPresent();
  }

  @Override
  public boolean equalsByDto(UniteRequestDTO dto, Long id) {
    Unite ressource = repository.findByCode(dto.getCode()).orElse(null);
    if (ressource == null) return false;
    return !ressource.getId().equals(id);
  }

  @Override
  public Unite findByCode(String code) {
    return repository.findByCode(code).orElseThrow(
            () ->  new ResourceNotFoundException("L'unite avec le code " + code + " n'existe pas")
    );
  }
}