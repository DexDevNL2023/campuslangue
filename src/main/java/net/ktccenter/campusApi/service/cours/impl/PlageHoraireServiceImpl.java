package net.ktccenter.campusApi.service.cours.impl;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.dao.administration.OccupationSalleRepository;
import net.ktccenter.campusApi.dao.cours.PlageHoraireRepository;
import net.ktccenter.campusApi.dto.importation.cours.ImportPlageHoraireRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteOccupationSalleDTO;
import net.ktccenter.campusApi.dto.lite.cours.LitePlageHoraireDTO;
import net.ktccenter.campusApi.dto.reponse.cours.PlageDTO;
import net.ktccenter.campusApi.dto.reponse.cours.PlageHoraireDTO;
import net.ktccenter.campusApi.dto.request.cours.PlageHoraireRequestDTO;
import net.ktccenter.campusApi.entities.administration.OccupationSalle;
import net.ktccenter.campusApi.entities.cours.PlageHoraire;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.cours.PlageHoraireMapper;
import net.ktccenter.campusApi.service.cours.PlageHoraireService;
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
public class PlageHoraireServiceImpl implements PlageHoraireService {
    private final PlageHoraireRepository repository;
    private final PlageHoraireMapper mapper;
    private final OccupationSalleRepository occupationSalleRepository;

    public PlageHoraireServiceImpl(PlageHoraireRepository repository, PlageHoraireMapper mapper, OccupationSalleRepository occupationSalleRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.occupationSalleRepository = occupationSalleRepository;
    }

    @Override
    public PlageHoraireDTO save(PlageHoraireRequestDTO dto) {
        return buildPlageHoraireDto(repository.save(mapper.asEntity(dto)));
    }

    private PlageHoraireDTO buildPlageHoraireDto(PlageHoraire plage) {
        PlageHoraireDTO dto = mapper.asDTO(plage);
        dto.setOccupations(getOccupationsForPlageHoraire(plage));
        return dto;
    }

    @Override
    public List<LitePlageHoraireDTO> save(List<ImportPlageHoraireRequestDTO> dtos) {
        return  ((List<PlageHoraire>) repository.saveAll(mapper.asEntityList(dtos)))
                .stream()
                .map(mapper::asLite)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        PlageHoraire plage = findById(id);
        if (!getOccupationsForPlageHoraire(plage).isEmpty())
            throw new ResourceNotFoundException("La plage horaire avec l'id " + id + " ne peut pas etre supprimée car elle est utilisée par d'autre ressource");
        repository.deleteById(id);
    }

    private Set<LiteOccupationSalleDTO> getOccupationsForPlageHoraire(PlageHoraire plage) {
        return occupationSalleRepository.findAllByPlageHoraire(plage).stream().map(this::buildOccupationLiteDto).collect(Collectors.toSet());
    }

    private LiteOccupationSalleDTO buildOccupationLiteDto(OccupationSalle occupation) {
        LiteOccupationSalleDTO lite = new LiteOccupationSalleDTO(occupation);
        lite.setPlageHoraire(new LitePlageHoraireDTO(occupation.getPlageHoraire()));
        return lite;
    }

    @Override
    public PlageHoraireDTO getOne(Long id) {
        return buildPlageHoraireDto(findById(id));
    }

    @Override
    public PlageHoraire findById(Long id) {
        return repository.findById(id).orElseThrow(
                () ->  new ResourceNotFoundException("La plage horaire avec l'id " + id + " n'existe pas")
        );
    }

    @Override
    public List<LitePlageHoraireDTO> findAll() {
        return ((List<PlageHoraire>) repository.findAll()).stream().map(mapper::asLite).collect(Collectors.toList());
    }

    @Override
    public Page<LitePlageHoraireDTO> findAll(Pageable pageable) {
        Page<PlageHoraire> entityPage = repository.findAll(pageable);
        List<PlageHoraire> entities = entityPage.getContent();
        return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
    }

    @Override
    public PlageHoraireDTO update(PlageHoraireRequestDTO dto, Long id) {
        PlageHoraire exist =  findById(id);
        dto.setId(exist.getId());
        return buildPlageHoraireDto(repository.save(mapper.asEntity(dto)));
    }

    @Override
    public boolean existsByCode(String code) {
        return repository.findByCode(code).isPresent();
    }

    @Override
    public boolean equalsByDto(PlageHoraireRequestDTO dto, Long id) {
        PlageHoraire ressource = repository.findByCode(dto.getCode()).orElse(null);
        if (ressource == null) return false;
        return !ressource.getId().equals(id);
    }

    @Override
    public PlageHoraire findByCode(String code) {
        return repository.findByCode(code).orElseThrow(
                () ->  new ResourceNotFoundException("La plage horaire avec le code " + code + " n'existe pas")
        );
    }

    @Override
    public List<PlageDTO> findByJour(String code) {
        return null;
    }
}
