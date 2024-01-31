package net.ktccenter.campusApi.service.administration.impl;

import net.ktccenter.campusApi.dao.administration.DroitRepository;
import net.ktccenter.campusApi.dto.importation.administration.ImportDroitRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteDroitDTO;
import net.ktccenter.campusApi.dto.reponse.administration.DroitDTO;
import net.ktccenter.campusApi.dto.request.administration.DroitRequestDTO;
import net.ktccenter.campusApi.entities.administration.Droit;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.administration.DroitMapper;
import net.ktccenter.campusApi.service.MainService;
import net.ktccenter.campusApi.service.administration.DroitService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DroitServiceImpl extends MainService implements DroitService {
    private final DroitRepository repository;
    private final DroitMapper mapper;

    public DroitServiceImpl(DroitRepository repository, DroitMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public DroitDTO save(DroitRequestDTO entity) {
        return mapper.asDTO(repository.save(mapper.asEntity(entity)));
    }

    @Override
    public List<LiteDroitDTO> save(List<ImportDroitRequestDTO> dtos) {
        return  ((List<Droit>) repository.saveAll(mapper.asEntityList(dtos)))
                .stream()
                .map(mapper::asLite)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public DroitDTO getOne(Long id) {
        return mapper.asDTO(findById(id));
    }

    @Override
    public Droit findById(Long id) {
        return repository.findById(id).orElseThrow(
                () ->  new ResourceNotFoundException("Le droit avec l'id " + id + " n'existe pas")
        );
    }

    @Override
    public List<LiteDroitDTO> findAll() {
        return ((List<Droit>) repository.findAll()).stream().map(mapper::asLite).collect(Collectors.toList());
    }

    @Override
    public Page<LiteDroitDTO> findAll(Pageable pageable) {
        Page<Droit> entityPage = repository.findAll(pageable);
        List<Droit> entities = entityPage.getContent();
        return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
    }


    @Override
    public DroitDTO update(DroitRequestDTO entity, Long id) {
      Droit exist =  findById(id);
      entity.setId(exist.getId());
        return mapper.asDTO(repository.save(mapper.asEntity(entity)));
    }

    @Override
    public boolean existsByVerbeAndKeyAndLibelle(String verbe, String key, String libelle) {
        return repository.findByVerbeAndKeyAndLibelle(verbe, key, libelle).isPresent();
    }

    @Override
    public boolean equalsByDto(DroitRequestDTO dto, Long id) {
        Droit ressource = repository.findByVerbeAndKeyAndLibelle(dto.getVerbe(), dto.getKey(), dto.getLibelle()).orElse(null);
        if (ressource == null) return false;
        return !ressource.getId().equals(id);
    }

    @Override
    public Droit findByLibelle(String libelle) {
        return repository.findByLibelle(libelle).orElseThrow(
                () ->  new ResourceNotFoundException("Le droit avec le nom " + libelle + " n'existe pas")
        );
    }
}
