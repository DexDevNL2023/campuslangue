package net.ktccenter.campusApi.service.cours.impl;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.dao.cours.EpreuveRepository;
import net.ktccenter.campusApi.dao.cours.ExamenRepository;
import net.ktccenter.campusApi.dao.cours.UniteRepository;
import net.ktccenter.campusApi.dao.scolarite.NiveauRepository;
import net.ktccenter.campusApi.dto.importation.cours.ImportExamenRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteEpreuveDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteExamenDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteUniteDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteEtudiantForNoteDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteInscriptionForNoteDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteNiveauDTO;
import net.ktccenter.campusApi.dto.reponse.branch.ExamenBranchDTO;
import net.ktccenter.campusApi.dto.reponse.cours.ExamenDTO;
import net.ktccenter.campusApi.dto.reponse.cours.ExamenForNoteReponseDTO;
import net.ktccenter.campusApi.dto.request.cours.ExamenForNoteDTO;
import net.ktccenter.campusApi.dto.request.cours.ExamenRequestDTO;
import net.ktccenter.campusApi.entities.cours.Epreuve;
import net.ktccenter.campusApi.entities.cours.Examen;
import net.ktccenter.campusApi.entities.cours.Unite;
import net.ktccenter.campusApi.entities.scolarite.Niveau;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.cours.ExamenMapper;
import net.ktccenter.campusApi.service.cours.ExamenService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ExamenServiceImpl implements ExamenService {
  private final ExamenRepository repository;
  private final ExamenMapper mapper;
  private final EpreuveRepository epreuveRepository;
  private final NiveauRepository niveauRepository;
  private final UniteRepository uniteRepository;

  public ExamenServiceImpl(ExamenRepository repository, ExamenMapper mapper, EpreuveRepository epreuveRepository, NiveauRepository niveauRepository, UniteRepository uniteRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.epreuveRepository = epreuveRepository;
    this.niveauRepository = niveauRepository;
    this.uniteRepository = uniteRepository;
  }

  @Override
  public ExamenDTO save(ExamenRequestDTO dto) {
    return buildExamenDto(repository.save(mapper.asEntity(dto)));
  }

  private ExamenDTO buildExamenDto(Examen examen) {
    ExamenDTO dto = mapper.asDTO(examen);
    Set<LiteEpreuveDTO> epreuves = getAllEpreuvessForExamen(examen);
    if (epreuves.isEmpty()) {
      epreuves = createAllEpreuvesForExamen(examen);
    }
    dto.setEpreuves(epreuves);
    dto.setMoyenne(calculMoyenne(epreuves));
    dto.setAppreciation(buildAppreciation(dto.getMoyenne()));
    dto.setTotalFraisPension(calculTotalPension(epreuves));
    dto.setTotalFraisRattrapage(calculTotalRatrappage(epreuves));
    return dto;
  }

  private Set<LiteEpreuveDTO> createAllEpreuvesForExamen(Examen examen) {
    Set<LiteEpreuveDTO> list = new HashSet<>();
    Niveau niveau = niveauRepository.findById(examen.getInscription().getSession().getNiveau().getId()).orElseThrow(
            () ->  new ResourceNotFoundException("Le niveau avec l'id "+examen.getInscription().getSession().getNiveau().getId()+" n'existe pas")
    );
    List<Unite> unites = uniteRepository.findAllByNiveau(niveau);
    if (unites.isEmpty()) throw new ResourceNotFoundException("Avant d'effectuer une inscription veillez ajouter au moins une unité de formation pour le niveau "+niveau.getLibelle());
    log.info("Get all unite by niveau " + unites);
    log.info("add test to test module");
    for (Unite unite : unites) {
      Epreuve epreuve = new Epreuve();
      epreuve.setUnite(unite);
      epreuve.setExamen(examen);
      epreuve = epreuveRepository.save(epreuve);
      list.add(buildEpreuveLiteDto(epreuve));
      log.info("new epreuve " + epreuve);
    }
    return list;
  }

  @Override
  public List<LiteExamenDTO> save(List<ImportExamenRequestDTO> dtos) {
    return  ((List<Examen>) repository.saveAll(mapper.asEntityList(dtos)))
            .stream()
            .map(this::buildExamenLiteDto)
            .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    Examen examen = findById(id);
    if (!getAllEpreuvessForExamen(examen).isEmpty())
        throw new ResourceNotFoundException("L'examen avec l'id " + id + " ne peut pas etre supprimée car elle est utilisée par d'autre ressource");
    repository.deleteById(id);
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

  @Override
  public ExamenDTO getOne(Long id) {
    return buildExamenDto(findById(id));
  }

  @Override
  public Examen findById(Long id) {
    return repository.findById(id).orElseThrow(
            () ->  new ResourceNotFoundException("L'examen avec l'id " + id + " n'existe pas")
    );
  }

  @Override
  public List<ExamenBranchDTO> findAll() {
      //return ((List<Examen>) repository.findAll()).stream().map(this::buildExamenLiteDto).collect(Collectors.toList());
      return null;
  }

  private LiteExamenDTO buildExamenLiteDto(Examen examen) {
    LiteExamenDTO dto = mapper.asLite(examen);
    Set<LiteEpreuveDTO> epreuves = getAllEpreuvessForExamen(examen);
    dto.setEpreuves(epreuves);
    dto.setMoyenne(calculMoyenne(epreuves));
    dto.setAppreciation(buildAppreciation(dto.getMoyenne()));
    dto.setTotalFraisPension(calculTotalPension(epreuves));
    dto.setTotalFraisRattrapage(calculTotalRatrappage(epreuves));
    return dto;
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

  @Override
  public Page<LiteExamenDTO> findAll(Pageable pageable) {
    Page<Examen> entityPage = repository.findAll(pageable);
    List<Examen> entities = entityPage.getContent();
    return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
  }

  @Override
  public void update(ExamenRequestDTO dto, Long id) {
    Examen exist =  findById(id);
    dto.setId(exist.getId());
    buildExamenDto(repository.save(mapper.asEntity(dto)));
  }

  @Override
  public boolean existsByCode(String code) {
    return repository.findByCode(code).isPresent();
  }

  @Override
  public boolean equalsByDto(ExamenRequestDTO dto, Long id) {
    Examen ressource = repository.findByCode(dto.getCode()).orElse(null);
    if (ressource == null) return false;
    return !ressource.getId().equals(id);
  }

  @Override
  public Examen findByCode(String code) {
    return repository.findByCode(code).orElseThrow(
            () ->  new ResourceNotFoundException("L'examen avec le code " + code + " n'existe pas")
    );
  }

  @Override
  public List<ExamenForNoteReponseDTO> getAllExamenBySession(Long sessionId, Long uniteId) {
    return buildExamenForNoteDto(sessionId, uniteId);
  }

  private List<ExamenForNoteReponseDTO>  buildExamenForNoteDto(Long sessionId, Long uniteId) {
    List<ExamenForNoteReponseDTO> listExamenDto = new ArrayList<>();
    List<Examen> listExamen = repository.findAllBySessionId(sessionId);
    for (Examen examen : listExamen) {
      List<Epreuve> epreuves = epreuveRepository.findAllByExamenIdAndUniteId(examen.getId(), uniteId);
      if (epreuves.isEmpty()) {
        continue;
      }
      for (Epreuve epreuve : epreuves) {
        listExamenDto.add(buildExamenForNoteDto(examen, epreuve));
      }
    }
    return listExamenDto;
  }

  @Override
  public List<ExamenForNoteReponseDTO> saisieNotesexamen(List<ExamenForNoteDTO> dtos) {
    List<ExamenForNoteReponseDTO> listDto = new ArrayList<>();
    for (ExamenForNoteDTO dto : dtos) {
      Examen examen = repository.findById(dto.getExamenId()).orElse(null);
      if (examen != null) {
        Epreuve epreuve = epreuveRepository.findById(dto.getEpreuve().getEpreuveId()).orElse(null);
        if (epreuve != null) {
          if (epreuve.getEstRattrapee()) {
            epreuve.setNoteRattrapage(dto.getEpreuve().getNoteRattrapage());
          } else {
            epreuve.setNoteObtenue(dto.getEpreuve().getNoteObtenue());
            boolean success = (dto.getEpreuve().getNoteObtenue() >= epreuve.getUnite().getNoteAdmission());
            epreuve.setEstValidee(success);
            if (!success) epreuve.setEstRattrapee(true);
          }
          epreuve = epreuveRepository.save(epreuve);
        }
        examen.setDateExamen(dto.getDateExamen());
        examen = repository.save(examen);
        listDto.add(buildExamenForNoteDto(examen, epreuve));
      }
    }
    return listDto;
  }

  private ExamenForNoteReponseDTO buildExamenForNoteDto(Examen examen, Epreuve epreuve) {
    ExamenForNoteReponseDTO dto = new ExamenForNoteReponseDTO(examen);
    Set<LiteEpreuveDTO> epreuves = getAllEpreuvessForExamen(examen);
    LiteEpreuveDTO epreuveLite = new LiteEpreuveDTO(epreuve);
    epreuveLite.setUnite(new LiteUniteDTO(epreuve.getUnite()));
    dto.setEpreuve(epreuveLite);
    LiteInscriptionForNoteDTO inscriptionLite = new LiteInscriptionForNoteDTO(examen.getInscription());
    inscriptionLite.setEtudiant(new LiteEtudiantForNoteDTO(examen.getInscription().getEtudiant()));
    dto.setInscription(inscriptionLite);
    dto.setMoyenne(calculMoyenne(epreuves));
    dto.setAppreciation(buildAppreciation(dto.getMoyenne()));
    dto.setTotalFraisPension(calculTotalPension(epreuves));
    dto.setTotalFraisRattrapage(calculTotalRatrappage(epreuves));
    return dto;
  }

  private Float calculMoyenne(Set<LiteEpreuveDTO> epreuves) {
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
}
