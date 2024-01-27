package net.ktccenter.campusApi.service.scolarite.impl;

import net.ktccenter.campusApi.dao.cours.UniteRepository;
import net.ktccenter.campusApi.dao.scolarite.ModuleFormationRepository;
import net.ktccenter.campusApi.dao.scolarite.NiveauRepository;
import net.ktccenter.campusApi.dao.scolarite.SessionRepository;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportNiveauRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteUniteDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteDiplomeDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteModuleFormationDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteNiveauDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteSessionDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.NiveauDTO;
import net.ktccenter.campusApi.dto.request.scolarite.NiveauRequestDTO;
import net.ktccenter.campusApi.entities.cours.Unite;
import net.ktccenter.campusApi.entities.scolarite.ModuleFormation;
import net.ktccenter.campusApi.entities.scolarite.Niveau;
import net.ktccenter.campusApi.entities.scolarite.Session;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.scolarite.NiveauMapper;
import net.ktccenter.campusApi.service.scolarite.NiveauService;
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
public class NiveauServiceImpl implements NiveauService {
    private final NiveauRepository repository;
    private final NiveauMapper mapper;
    private final ModuleFormationRepository moduleFormationRepository;
    private final SessionRepository sessionRepository;
    private final UniteRepository uniteRepository;

    public NiveauServiceImpl(NiveauRepository repository, NiveauMapper mapper, ModuleFormationRepository moduleFormationRepository, SessionRepository sessionRepository, UniteRepository uniteRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.moduleFormationRepository = moduleFormationRepository;
        this.sessionRepository = sessionRepository;
        this.uniteRepository = uniteRepository;
    }

    @Override
    public NiveauDTO save(NiveauRequestDTO dto) {
        return buildNiveauDto(repository.save(mapper.asEntity(dto)));
    }

    @Override
    public List<LiteNiveauDTO> save(List<ImportNiveauRequestDTO> dtos) {
        return  ((List<Niveau>) repository.saveAll(mapper.asEntityList(dtos)))
                .stream()
                .map(this::buildLiteNiveauDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        Niveau niveau = findById(id);
        if (!getAllModulesForNiveau(niveau).isEmpty() || !getAllSessionsForNiveau(niveau).isEmpty() || !getAllUnitesForNiveau(niveau).isEmpty())
            throw new ResourceNotFoundException("Le niveau avec l'id " + id + " ne peut pas etre supprimé car elle est utilisé par d'autre ressource");
        repository.delete(niveau);
    }

    private Set<LiteModuleFormationDTO> getAllModulesForNiveau(Niveau entity) {
        return moduleFormationRepository.findAllByNiveau(entity).stream().map(this::builditeModuleFormationDTO).collect(Collectors.toSet());
    }

    private LiteModuleFormationDTO builditeModuleFormationDTO(ModuleFormation entity) {
        LiteModuleFormationDTO lite = new LiteModuleFormationDTO();
        lite.setId(entity.getId());
        lite.setCode(entity.getCode());
        lite.setLibelle(entity.getLibelle());
        return lite;
    }

    private Set<LiteSessionDTO> getAllSessionsForNiveau(Niveau entity) {
        return sessionRepository.findAllByNiveau(entity).stream().map(this::builditeLiteSessionDTO).collect(Collectors.toSet());
    }

    private LiteSessionDTO builditeLiteSessionDTO(Session entity) {
        return new LiteSessionDTO(entity);
    }

    private Set<LiteUniteDTO> getAllUnitesForNiveau(Niveau entity) {
        return uniteRepository.findAllByNiveau(entity).stream().map(this::builditeLiteUniteDTO).collect(Collectors.toSet());
    }

    private LiteUniteDTO builditeLiteUniteDTO(Unite entity) {
        LiteUniteDTO lite = new LiteUniteDTO();
        lite.setId(entity.getId());
        lite.setCode(entity.getCode());
        lite.setLibelle(entity.getLibelle());
        return lite;
    }

    @Override
    public Niveau findById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Le niveau avec l'id " + id + " n'existe pas")
        );
    }

    @Override
    public NiveauDTO getOne(Long id) {
        return buildNiveauDto(findById(id));
    }

    @Override
    public List<LiteNiveauDTO> findAll() {
        return ((List<Niveau>) repository.findAll()).stream().map(this::buildLiteNiveauDto).collect(Collectors.toList());
    }

    @Override
    public Page<LiteNiveauDTO> findAll(Pageable pageable) {
        Page<Niveau> entityPage = repository.findAll(pageable);
        List<Niveau> entities = entityPage.getContent();
        return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
    }

    @Override
    public void update(NiveauRequestDTO dto, Long id) {
        Niveau exist = findById(id);
        dto.setId(exist.getId());
        buildNiveauDto(repository.save(mapper.asEntity(dto)));
    }

    @Override
    public boolean existByCode(String code) {
        return repository.findByCode(code).isPresent();
    }

    @Override
    public boolean equalsByDto(NiveauRequestDTO dto, Long id) {
        Niveau ressource = repository.findByCode(dto.getCode()).orElse(null);
        if (ressource == null) return false;
        return !ressource.getId().equals(id);
    }

    @Override
    public Niveau findByCode(String code) {
        return repository.findByCode(code).orElseThrow(
                () ->  new ResourceNotFoundException("Le niveau avec le code " + code + " n'existe pas")
        );
    }

    @Override
    public Niveau findByCodeAndLibelle(String code, String libelle) {
        return repository.findByCodeAndLibelle(code, libelle).orElseThrow(
                () ->  new ResourceNotFoundException("Le niveau avec le code " + code + " et le libelle "+ libelle +" n'existe pas")
        );
    }

    private NiveauDTO buildNiveauDto(Niveau niveau) {
        NiveauDTO dto = mapper.asDTO(niveau);
        dto.setDiplomeFinFormation(new LiteDiplomeDTO(niveau.getDiplomeFinFormation().getId(),
                niveau.getDiplomeFinFormation().getCode(),
                niveau.getDiplomeFinFormation().getLibelle()));
        dto.setDiplomeRequis(new LiteDiplomeDTO(niveau.getDiplomeRequis().getId(),
                niveau.getDiplomeRequis().getCode(),
                niveau.getDiplomeRequis().getLibelle()));
        dto.setUnites(getAllUnitesForNiveau(niveau));
        dto.setSessions(getAllSessionsForNiveau(niveau));
        dto.setModules(getAllModulesForNiveau(niveau));
        return dto;
    }

    private LiteNiveauDTO buildLiteNiveauDto(Niveau niveau) {
        LiteNiveauDTO dto = mapper.asLite(niveau);
        dto.setDiplomeFinFormation(new LiteDiplomeDTO(niveau.getDiplomeFinFormation().getId(),
                niveau.getDiplomeFinFormation().getCode(),
                niveau.getDiplomeFinFormation().getLibelle()));
        dto.setDiplomeRequis(new LiteDiplomeDTO(niveau.getDiplomeRequis().getId(),
                niveau.getDiplomeRequis().getCode(),
                niveau.getDiplomeRequis().getLibelle()));
        return dto;
    }
}
