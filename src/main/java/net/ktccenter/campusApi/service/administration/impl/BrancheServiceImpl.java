package net.ktccenter.campusApi.service.administration.impl;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.dao.administration.BrancheRepository;
import net.ktccenter.campusApi.dao.administration.CampusRepository;
import net.ktccenter.campusApi.dao.scolarite.SessionRepository;
import net.ktccenter.campusApi.dto.importation.administration.ImportBrancheRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteBrancheDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteCampusDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteSessionDTO;
import net.ktccenter.campusApi.dto.reponse.administration.BrancheDTO;
import net.ktccenter.campusApi.dto.request.administration.BrancheRequestDTO;
import net.ktccenter.campusApi.entities.administration.Branche;
import net.ktccenter.campusApi.entities.administration.Campus;
import net.ktccenter.campusApi.entities.scolarite.Session;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.administration.BrancheMapper;
import net.ktccenter.campusApi.service.administration.BrancheService;
import net.ktccenter.campusApi.utils.MyUtils;
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
@Slf4j
public class BrancheServiceImpl implements BrancheService {
    private final BrancheRepository repository;
    private final BrancheMapper mapper;
    private final CampusRepository campusRepository;
    private final SessionRepository sessionRepository;

    public BrancheServiceImpl(BrancheRepository repository, BrancheMapper mapper, CampusRepository campusRepository, SessionRepository sessionRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.campusRepository = campusRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public BrancheDTO save(BrancheRequestDTO dto) {
        if(!MyUtils.isValidEmailAddress(dto.getEmail()))
            throw new ResourceNotFoundException("L'email " + dto.getEmail() + " est invalide.");
        if (dto.getParDefaut()) changeDefaultBranche();
        return buildBrancheDto(repository.save(mapper.asEntity(dto)));
    }

    private void changeDefaultBranche() {
        Branche branche = repository.findByParDefaut(true);
        branche.setParDefaut(false);
        repository.save(branche);
    }

    private BrancheDTO buildBrancheDto(Branche branche) {
        BrancheDTO dto = mapper.asDTO(branche);
        dto.setCampus(getAllCampusForBranche(branche));
        dto.setSessions(getSessionsForBranche(branche));
        return dto;
    }

    @Override
    public List<LiteBrancheDTO> save(List<ImportBrancheRequestDTO> dtos) {
        for (ImportBrancheRequestDTO dto : dtos) {
            if(!MyUtils.isValidEmailAddress(dto.getEmail()))
                throw new ResourceNotFoundException("L'email " + dto.getEmail() + " est invalide.");
        }
        return  ((List<Branche>) repository.saveAll(mapper.asEntityList(dtos)))
                .stream()
                .map(mapper::asLite)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        Branche branche = findById(id);
        if (branche.getParDefaut())
            throw new ResourceNotFoundException("Vous ne pouvez pas supprimer cette branche, car c'est une branche par défaut!");
        if (!getAllCampusForBranche(branche).isEmpty() || !getSessionsForBranche(branche).isEmpty())
            throw new ResourceNotFoundException("La branche avec l'id " + id + " ne peut pas être supprimée car elle est utilisée par d'autres ressources");
        repository.deleteById(id);
    }

    private Set<LiteCampusDTO> getAllCampusForBranche(Branche branche) {
        return campusRepository.findAllByBranche(branche).stream().map(this::buildCampusLiteDto).collect(Collectors.toSet());
    }

    private LiteCampusDTO buildCampusLiteDto(Campus campus) {
        LiteCampusDTO lite = new LiteCampusDTO();
        lite.setId(campus.getId());
        lite.setCode(campus.getCode());
        lite.setLibelle(campus.getLibelle());
        lite.setAdresse(campus.getAdresse());
        return lite;
    }

    private Set<LiteSessionDTO> getSessionsForBranche(Branche branche) {
        return sessionRepository.findAllByBranche(branche).stream().map(this::buildSessionLiteDto).collect(Collectors.toSet());
    }

    private LiteSessionDTO buildSessionLiteDto(Session session) {
        return new LiteSessionDTO(session);
    }

    @Override
    public BrancheDTO getOne(Long id) {
        return buildBrancheDto(findById(id));
    }

    @Override
    public Branche findById(Long id) {
        return repository.findById(id).orElseThrow(
                () ->  new ResourceNotFoundException("La Branche avec l'id " + id + " n'existe pas")
        );
    }

    @Override
    public List<LiteBrancheDTO> findAll() {
        return ((List<Branche>) repository.findAll()).stream().map(mapper::asLite).collect(Collectors.toList());
    }

    @Override
    public Page<LiteBrancheDTO> findAll(Pageable pageable) {
        Page<Branche> entityPage = repository.findAll(pageable);
        List<Branche> entities = entityPage.getContent();
        return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
    }

    @Override
    public BrancheDTO update(BrancheRequestDTO dto, Long id) {
        if(!MyUtils.isValidEmailAddress(dto.getEmail()))
            throw new ResourceNotFoundException("L'email " + dto.getEmail() + " est invalide.");
        if (dto.getParDefaut()) changeDefaultBranche();
        Branche exist =  findById(id);
        dto.setId(exist.getId());
        return buildBrancheDto(repository.save(mapper.asEntity(dto)));
    }

    @Override
    public boolean existsByCodeAndVille(String code, String ville) {
        return repository.findByCodeAndVille(code, ville).isPresent();
    }

    @Override
    public boolean equalsByDto(BrancheRequestDTO dto, Long id) {
        Branche ressource = repository.findByCodeAndVille(dto.getCode(), dto.getVille()).orElse(null);
        if (ressource == null) return false;
        return !ressource.getId().equals(id);
    }

    @Override
    public Branche findByCode(String code) {
        return repository.findByCode(code).orElseThrow(
                () ->  new ResourceNotFoundException("Le branche avec le code " + code + " n'existe pas")
        );
    }
}
