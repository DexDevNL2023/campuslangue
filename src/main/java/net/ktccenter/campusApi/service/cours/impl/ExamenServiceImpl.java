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
import net.ktccenter.campusApi.dto.lite.scolarite.LiteNiveauForSessionDTO;
import net.ktccenter.campusApi.dto.reponse.branch.ExamenBranchDTO;
import net.ktccenter.campusApi.dto.reponse.cours.EpreuveForResultatDTO;
import net.ktccenter.campusApi.dto.reponse.cours.ExamenDTO;
import net.ktccenter.campusApi.dto.reponse.cours.ExamenForNoteReponseDTO;
import net.ktccenter.campusApi.dto.reponse.cours.ExamenForResultatReponseDTO;
import net.ktccenter.campusApi.dto.request.cours.*;
import net.ktccenter.campusApi.entities.administration.Branche;
import net.ktccenter.campusApi.entities.cours.Epreuve;
import net.ktccenter.campusApi.entities.cours.Examen;
import net.ktccenter.campusApi.entities.cours.Unite;
import net.ktccenter.campusApi.entities.scolarite.Etudiant;
import net.ktccenter.campusApi.entities.scolarite.Niveau;
import net.ktccenter.campusApi.enums.ResultatState;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.cours.ExamenMapper;
import net.ktccenter.campusApi.service.MainService;
import net.ktccenter.campusApi.service.cours.ExamenService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ExamenServiceImpl extends MainService implements ExamenService {
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

    private Set<LiteEpreuveDTO> buildAllEpreuvesDto(List<Epreuve> epreuves) {
        return epreuves.stream().map(this::buildEpreuveLiteDto).collect(Collectors.toSet());
    }

    private ExamenDTO buildExamenDto(Examen examen) {
        ExamenDTO dto = mapper.asDTO(examen);
        List<Epreuve> epreuves = getAllEpreuvessForExamen(examen);
        if (epreuves.isEmpty()) {
            epreuves = createAllEpreuvesForExamen(examen);
        }
        dto.setEpreuves(buildAllEpreuvesDto(epreuves));
        dto.setMoyenne(calculMoyenne(epreuves));
        dto.setAppreciation(buildAppreciation(dto.getMoyenne()));
        return dto;
    }

    private List<Epreuve> createAllEpreuvesForExamen(Examen examen) {
        List<Epreuve> list = new ArrayList<>();
        Niveau niveau = niveauRepository.findById(examen.getInscription().getSession().getNiveau().getId()).orElseThrow(
                () -> new ResourceNotFoundException("Le niveau avec l'id " + examen.getInscription().getSession().getNiveau().getId() + " n'existe pas")
        );
        List<Unite> unites = uniteRepository.findAllByNiveau(niveau);
        if (unites.isEmpty())
            throw new ResourceNotFoundException("Avant d'effectuer une inscription veillez ajouter au moins une unité de formation pour le niveau " + niveau.getLibelle());
        log.info("Get all unite by niveau " + unites);
        log.info("add test to test module");
        for (Unite unite : unites) {
            Epreuve epreuve = new Epreuve();
            epreuve.setUnite(unite);
            epreuve.setExamen(examen);
            epreuve.setEstRattrapee(false);
            epreuve.setEstValidee(false);
            epreuve.setNoteRattrapage(0F);
            epreuve.setNoteObtenue(0F);
            epreuve = epreuveRepository.save(epreuve);
            list.add(epreuve);
            log.info("new epreuve " + epreuve);
        }
        return list;
    }

    @Override
    public List<LiteExamenDTO> save(List<ImportExamenRequestDTO> dtos) {
        return ((List<Examen>) repository.saveAll(mapper.asEntityList(dtos)))
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

    private List<Epreuve> getAllEpreuvessForExamen(Examen examen) {
        return epreuveRepository.findAllByExamen(examen);
    }

    private LiteEpreuveDTO buildEpreuveLiteDto(Epreuve entity) {
        LiteEpreuveDTO lite = new LiteEpreuveDTO(entity);
        LiteUniteDTO liteUnite = new LiteUniteDTO(entity.getUnite());
        liteUnite.setNiveau(new LiteNiveauForSessionDTO(entity.getUnite().getNiveau()));
        lite.setUnite(liteUnite);
        return lite;
    }

    @Override
    public ExamenDTO getOne(Long id) {
        return buildExamenDto(findById(id));
    }

    @Override
    public Examen findById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("L'examen avec l'id " + id + " n'existe pas")
        );
    }

    @Override
    public List<ExamenBranchDTO> findAll() {
        List<Examen> examens = (List<Examen>) repository.findAll();
        List<ExamenBranchDTO> result = new ArrayList<>();
        if (hasGrantAuthorized()) {
            for (Branche b : getAllBranches()) {
                result.add(buildData(b, examens));
            }
        } else {
            result.add(buildData(getCurrentUserBranch(), examens));
        }
        return result;
    }

    private ExamenBranchDTO buildData(Branche branche, List<Examen> examens) {
        ExamenBranchDTO dto = new ExamenBranchDTO();
        dto.setBranche(brancheMapper.asLite(branche));
        dto.setData(examens.stream()
                .filter(e -> belongsToTheCurrentBranch(branche, e))
                .map(mapper::asLite)
                .collect(Collectors.toList()));
        return dto;
    }

    private boolean belongsToTheCurrentBranch(Branche branche, Examen e) {
        return e.getInscription().getSession().getBranche().getId().equals(branche.getId());
    }

    private LiteExamenDTO buildExamenLiteDto(Examen examen) {
        LiteExamenDTO dto = mapper.asLite(examen);
        List<Epreuve> epreuves = getAllEpreuvessForExamen(examen);
        dto.setEpreuves(buildAllEpreuvesDto(epreuves));
        dto.setMoyenne(calculMoyenne(epreuves));
        dto.setAppreciation(buildAppreciation(dto.getMoyenne()));
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
    public ExamenDTO update(ExamenRequestDTO dto, Long id) {
        Examen exist = findById(id);
        dto.setId(exist.getId());
        return buildExamenDto(repository.save(mapper.asEntity(dto)));
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
                () -> new ResourceNotFoundException("L'examen avec le code " + code + " n'existe pas")
        );
    }

    @Override
    public FullExamenForNoteDTO getAllExamenBySessionAndUnite(Long sessionId, Long uniteId) {
        return buildExamenForNoteDto(sessionId, uniteId);
    }

    private FullExamenForNoteDTO buildExamenForNoteDto(Long sessionId, Long uniteId) {
        Date dateExamen = new Date();
        List<ExamenForNoteDTO> listExamenDto = new ArrayList<>();
        List<Examen> listExamen = repository.findAllBySessionId(sessionId);
        for (Examen examen : listExamen) {
            dateExamen = examen.getDateExamen();
            List<Epreuve> epreuves = epreuveRepository.findAllByExamenIdAndUniteId(examen.getId(), uniteId);
            if (epreuves.isEmpty()) {
                continue;
            }
            for (Epreuve epreuve : epreuves) {
                listExamenDto.add(buildExamenForNoteDto(examen, epreuve));
            }
        }
        return new FullExamenForNoteDTO(dateExamen, listExamenDto);
    }

    @Override
    public List<ExamenForNoteReponseDTO> saisieNotesExamen(FullExamenForNoteDTO dto) {
        List<ExamenForNoteReponseDTO> listDto = new ArrayList<>();
        List<ExamenForNoteDTO> listExamenDto = dto.getExamens();
        for (ExamenForNoteDTO examenDto : listExamenDto) {
            Examen examen = repository.findById(examenDto.getExamenId()).orElse(null);
            if (examen != null) {
                Epreuve epreuve = epreuveRepository.findById(examenDto.getEpreuve().getEpreuveId()).orElse(null);
                if (epreuve != null) {
                    epreuve.setNoteObtenue(examenDto.getEpreuve().getNoteObtenue());
                    epreuve.setNoteRattrapage(examenDto.getEpreuve().getNoteRattrapage());
                    boolean success = false;
                    if (epreuve.getEstRattrapee()) {
                        success = (examenDto.getEpreuve().getNoteRattrapage() >= epreuve.getUnite().getNoteAdmission());
                    } else {
                        success = (examenDto.getEpreuve().getNoteObtenue() >= epreuve.getUnite().getNoteAdmission());
                    }
                    epreuve.setEstValidee(success);
                    epreuve.setEstRattrapee(!success);
                    epreuveRepository.save(epreuve);
                }
                examen.setDateExamen(dto.getDateExamen());
                examen = repository.save(examen);
                listDto.add(buildExamenForNoteReponseDto(examen));
            }
        }
        return listDto;
    }

    @Override
    public void importNotesExamen(FullExamenForNoteImportDTO dto) {
        List<EpreuveForNoteImportDTO> listEpreuvesDto = dto.getEpreuves();
        for (EpreuveForNoteImportDTO epreuveDto : listEpreuvesDto) {
            Epreuve epreuve = epreuveRepository.findById(epreuveDto.getEpreuveId()).orElse(null);
            if (epreuve != null) {
                epreuve.setNoteObtenue(epreuveDto.getNoteObtenue());
                epreuve.setNoteRattrapage(epreuveDto.getNoteRattrapage());
                boolean success = false;
                if (epreuve.getEstRattrapee()) {
                    success = (epreuveDto.getNoteRattrapage() >= epreuve.getUnite().getNoteAdmission());
                } else {
                    success = (epreuveDto.getNoteObtenue() >= epreuve.getUnite().getNoteAdmission());
                }
                epreuve.setEstValidee(success);
                epreuve.setEstRattrapee(!success);
                epreuve = epreuveRepository.save(epreuve);
                Examen examen = repository.findById(epreuve.getExamen().getId()).orElse(null);
                if (examen != null) {
                    examen.setDateExamen(dto.getDateExamen());
                    repository.save(examen);
                }
            }
        }
    }

    @Override
    public FullExamenForNoteDTO getAllNoteStudentIsRattapage(Long sessionId, Long uniteId) {
        return buildExamenForNoteIsRattapageDto(sessionId, uniteId);
    }

    @Override
    public List<ExamenForResultatReponseDTO> getAllResultatExamenBySession(Long sessionId, ResultatState state) {
        return repository.findAllBySessionId(sessionId)
                .stream()
                .filter(e -> getByAppreciation(e, state))
                .map(this::buildExamenResultatDto)
                .collect(Collectors.toList());
    }

    private boolean getByAppreciation(Examen examen, ResultatState state) {
        List<Epreuve> epreuves = epreuveRepository.findAllByExamen(examen);
        Float moyenne = 0F;
        boolean isFail = false;
        for (Epreuve epreuve : epreuves) {
            if (!epreuve.getEstValidee()) {
                isFail = true;
            }
            moyenne += epreuve.getNoteObtenue();
        }
        moyenne = !epreuves.isEmpty() && !isFail ? moyenne / epreuves.size() : 0;
        int note = moyenne.intValue();
        switch (state) {
            case ALL:
                return true;
            case WIN:
                return (note >= 10);
            case FAIL:
                return (note < 10);
            default:
                return false;
        }
    }

    private ExamenForResultatReponseDTO buildExamenResultatDto(Examen examen) {
        Set<EpreuveForResultatDTO> listEpreuveDto = new HashSet<>();
        ExamenForResultatReponseDTO dto = new ExamenForResultatReponseDTO();
        dto.setMatricule(examen.getInscription().getEtudiant().getMatricule());
        dto.setFullName(getFullName(examen.getInscription().getEtudiant()));
        List<Epreuve> epreuves = epreuveRepository.findAllByExamen(examen);
        for (Epreuve epreuve : epreuves) {
            listEpreuveDto.add(buildEpreuveForResultatDto(epreuve));
        }
        dto.setEpreuves(listEpreuveDto);
        dto.setMoyenne(calculMoyenne(epreuves));
        dto.setAppreciation(buildAppreciation(dto.getMoyenne()));
        return dto;
    }

    private EpreuveForResultatDTO buildEpreuveForResultatDto(Epreuve epreuve) {
        EpreuveForResultatDTO epreuveLite = new EpreuveForResultatDTO(epreuve);
        if (epreuve.getEstRattrapee()) {
            epreuveLite.setNoteFinal(epreuve.getNoteRattrapage());
        } else {
            epreuveLite.setNoteFinal(epreuve.getNoteObtenue());
        }
        epreuveLite.setUniteCode(epreuve.getUnite().getCode());
        return epreuveLite;
    }

    public String getFullName(Etudiant etudiant) {
        return !etudiant.getPrenom().isEmpty() ? etudiant.getNom() + " " + etudiant.getPrenom() : etudiant.getNom();
    }

    private FullExamenForNoteDTO buildExamenForNoteIsRattapageDto(Long sessionId, Long uniteId) {
        Date dateExamen = new Date();
        List<ExamenForNoteDTO> listExamenDto = new ArrayList<>();
        List<Examen> listExamen = repository.findAllBySessionId(sessionId);
        for (Examen examen : listExamen) {
            dateExamen = examen.getDateExamen();
            List<Epreuve> epreuves = epreuveRepository.findAllByExamenIdAndUniteId(examen.getId(), uniteId);
            for (Epreuve epreuve : epreuves) {
                if (epreuve.getEstRattrapee()) {
                    listExamenDto.add(buildExamenForNoteDto(examen, epreuve));
                }
            }
        }
        return new FullExamenForNoteDTO(dateExamen, listExamenDto);
    }

    private ExamenForNoteDTO buildExamenForNoteDto(Examen examen, Epreuve epreuve) {
        ExamenForNoteDTO dto = new ExamenForNoteDTO(examen);
        EpreuveForNoteDTO epreuveLite = new EpreuveForNoteDTO(epreuve);
        epreuveLite.setUniteCode(epreuve.getUnite().getCode());
        dto.setEpreuve(epreuveLite);
        dto.setMatricule(examen.getInscription().getEtudiant().getMatricule());
        dto.setNom(examen.getInscription().getEtudiant().getNom());
        dto.setPrenom(examen.getInscription().getEtudiant().getPrenom());
        return dto;
    }

    private Float calculMoyenne(List<Epreuve> epreuves) {
        Float moyenne = 0F;
        for (Epreuve epreuve : epreuves) {
            if (!epreuve.getEstValidee()) {
                return 0F;
            }
            moyenne += epreuve.getNoteObtenue();
        }
        return !epreuves.isEmpty() ? moyenne / epreuves.size() : 0;
    }

    @Override
    public FullExamen2ForNoteDTO getAllExamenBySession(Long sessionId) {
        return buildExamen2ForNoteDto(sessionId);
    }

    private FullExamen2ForNoteDTO buildExamen2ForNoteDto(Long sessionId) {
        Date dateExamen = new Date();
        List<Examen2ForNoteDTO> listExamenDto = new ArrayList<>();
        List<Examen> listExamen = repository.findAllBySessionId(sessionId);
        for (Examen examen : listExamen) {
            List<EpreuveForNoteDTO> listEpreuveDto = new ArrayList<>();
            Examen2ForNoteDTO dto = new Examen2ForNoteDTO(examen);
            dto.setMatricule(examen.getInscription().getEtudiant().getMatricule());
            dto.setNom(examen.getInscription().getEtudiant().getNom());
            dto.setPrenom(examen.getInscription().getEtudiant().getPrenom());
            dateExamen = examen.getDateExamen();
            List<Epreuve> epreuves = epreuveRepository.findAllByExamen(examen);
            if (epreuves.isEmpty()) {
                continue;
            }
            for (Epreuve epreuve : epreuves) {
                listEpreuveDto.add(buildExamenForNoteDto(epreuve));
            }
            dto.setEpreuves(listEpreuveDto);
            listExamenDto.add(dto);
        }
        return new FullExamen2ForNoteDTO(dateExamen, listExamenDto);
    }

    private EpreuveForNoteDTO buildExamenForNoteDto(Epreuve epreuve) {
        EpreuveForNoteDTO epreuveLite = new EpreuveForNoteDTO(epreuve);
        epreuveLite.setUniteCode(epreuve.getUnite().getCode());
        return epreuveLite;
    }

    @Override
    public List<ExamenForNoteReponseDTO> saisieNotesExamen2(FullExamen2ForNoteDTO dto) {
        List<ExamenForNoteReponseDTO> listDto = new ArrayList<>();
        List<Examen2ForNoteDTO> listExamenDto = dto.getExamens();
        for (Examen2ForNoteDTO examenDto : listExamenDto) {
            Examen examen = repository.findById(examenDto.getExamenId()).orElse(null);
            if (examen != null) {
                List<EpreuveForNoteDTO> listEpreuveDto = examenDto.getEpreuves();
                for (EpreuveForNoteDTO epreuveDto : listEpreuveDto) {
                    Epreuve epreuve = epreuveRepository.findById(epreuveDto.getEpreuveId()).orElse(null);
                    if (epreuve != null) {
                        epreuve.setNoteObtenue(epreuveDto.getNoteObtenue());
                        epreuve.setNoteRattrapage(epreuveDto.getNoteRattrapage());
                        boolean success = false;
                        if (epreuve.getEstRattrapee()) {
                            success = (epreuveDto.getNoteRattrapage() >= epreuve.getUnite().getNoteAdmission());
                        } else {
                            success = (epreuveDto.getNoteObtenue() >= epreuve.getUnite().getNoteAdmission());
                        }
                        epreuve.setEstValidee(success);
                        epreuve.setEstRattrapee(!success);
                        epreuveRepository.save(epreuve);
                    }
                }
                examen.setDateExamen(dto.getDateExamen());
                examen = repository.save(examen);
                listDto.add(buildExamenForNoteReponseDto(examen));
            }
        }
        return listDto;
    }

    private ExamenForNoteReponseDTO buildExamenForNoteReponseDto(Examen examen) {
        ExamenForNoteReponseDTO dto = new ExamenForNoteReponseDTO(examen);
        List<Epreuve> epreuves = getAllEpreuvessForExamen(examen);
        dto.setMatricule(examen.getInscription().getEtudiant().getMatricule());
        dto.setNom(examen.getInscription().getEtudiant().getNom());
        dto.setPrenom(examen.getInscription().getEtudiant().getPrenom());
        dto.setEpreuves(buildAllEpreuvesDto(epreuves));
        dto.setMoyenne(calculMoyenne(epreuves));
        dto.setAppreciation(buildAppreciation(dto.getMoyenne()));
        return dto;
    }
}
