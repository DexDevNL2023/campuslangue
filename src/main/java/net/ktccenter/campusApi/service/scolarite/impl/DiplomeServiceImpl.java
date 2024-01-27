package net.ktccenter.campusApi.service.scolarite.impl;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.dao.scolarite.DiplomeRepository;
import net.ktccenter.campusApi.dao.scolarite.EtudiantRepository;
import net.ktccenter.campusApi.dao.scolarite.FormateurRepository;
import net.ktccenter.campusApi.dao.scolarite.NiveauRepository;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportDiplomeRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteDiplomeDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.DiplomeDTO;
import net.ktccenter.campusApi.dto.request.scolarite.DiplomeRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.Diplome;
import net.ktccenter.campusApi.entities.scolarite.Etudiant;
import net.ktccenter.campusApi.entities.scolarite.Formateur;
import net.ktccenter.campusApi.entities.scolarite.Niveau;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.scolarite.DiplomeMapper;
import net.ktccenter.campusApi.service.scolarite.DiplomeService;
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
public class DiplomeServiceImpl implements DiplomeService {
    private final DiplomeRepository repository;
    private final DiplomeMapper mapper;
    private final NiveauRepository niveauRepository;
    private final EtudiantRepository etudiantRepository;
    private final FormateurRepository formateurRepository;

    public DiplomeServiceImpl(DiplomeRepository repository, DiplomeMapper mapper, NiveauRepository niveauRepository, EtudiantRepository etudiantRepository, FormateurRepository formateurRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.niveauRepository = niveauRepository;
        this.etudiantRepository = etudiantRepository;
        this.formateurRepository = formateurRepository;
    }

    @Override
    public DiplomeDTO save(DiplomeRequestDTO dto) {
        return mapper.asDTO(repository.save(mapper.asEntity(dto)));
    }

    @Override
    public List<LiteDiplomeDTO> save(List<ImportDiplomeRequestDTO> dtos) {
        return  ((List<Diplome>) repository.saveAll(mapper.asEntityList(dtos)))
                .stream()
                .map(mapper::asLite)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        Diplome diplome = findById(id);
        log.info(diplome.toString());
        if (!getAllNiveauForDiplome(diplome).isEmpty() || !getEtudiantsForDiplome(diplome).isEmpty() || !getFormateursForDiplome(diplome).isEmpty())
            throw new ResourceNotFoundException("Le diplome avec l'id " + id + " ne peut pas etre supprimé car elle est utilisé par d'autre ressource");
        repository.delete(diplome);
    }

    private Set<Etudiant> getEtudiantsForDiplome(Diplome diplome) {
        return new HashSet<>(etudiantRepository.findAllByDiplome(diplome));
    }

    private Set<Formateur> getFormateursForDiplome(Diplome diplome) {
        return new HashSet<>(formateurRepository.findAllByDiplome(diplome));
    }

    private Set<Niveau> getAllNiveauForDiplome(Diplome diplome) {
        return new HashSet<>(niveauRepository.findAllByDiplome(diplome));
    }

    @Override
    public Diplome findById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Le diplôme avec l'id " + id + " n'existe pas")
        );
    }

    @Override
    public DiplomeDTO getOne(Long id) {
        return mapper.asDTO(findById(id));
    }

    @Override
    public List<LiteDiplomeDTO> findAll() {
        return ((List<Diplome>) repository.findAll()).stream().map(mapper::asLite).collect(Collectors.toList());
    }

    @Override
    public Page<LiteDiplomeDTO> findAll(Pageable pageable) {
        Page<Diplome> entityPage = repository.findAll(pageable);
        List<Diplome> entities = entityPage.getContent();
        return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
    }


    @Override
    public void update(DiplomeRequestDTO dto, Long id) {
        Diplome exist = findById(id);
        dto.setId(exist.getId());
        mapper.asDTO(repository.save(mapper.asEntity(dto)));
    }


    @Override
    public boolean existByCode(String code) {
        return repository.findByCode(code).isPresent();
    }

    @Override
    public boolean equalsByDto(DiplomeRequestDTO dto, Long id) {
        Diplome ressource = repository.findByCode(dto.getCode()).orElse(null);
        if (ressource == null) return false;
        return !ressource.getId().equals(id);
    }

    @Override
    public Diplome findByCode(String code) {
        return repository.findByCode(code).orElseThrow(
                () ->  new ResourceNotFoundException("Le diplôme avec le code " + code + " n'existe pas")
        );
    }

    @Override
    public Diplome findByCodeAndLibelle(String code, String libelle) {
        return repository.findByCodeAndLibelle(code, libelle).orElseThrow(
                () ->  new ResourceNotFoundException("Le diplôme avec le code " + code + " et le libelle "+ libelle +" n'existe pas")
        );
    }
}
