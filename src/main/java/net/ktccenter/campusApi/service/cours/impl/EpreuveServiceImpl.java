package net.ktccenter.campusApi.service.cours.impl;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.dao.cours.EpreuveRepository;
import net.ktccenter.campusApi.dto.importation.cours.ImportEpreuveRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteEpreuveDTO;
import net.ktccenter.campusApi.dto.reponse.cours.EpreuveDTO;
import net.ktccenter.campusApi.dto.request.cours.EpreuveRequestDTO;
import net.ktccenter.campusApi.entities.cours.Epreuve;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.cours.EpreuveMapper;
import net.ktccenter.campusApi.service.cours.EpreuveService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class EpreuveServiceImpl implements EpreuveService {
  private final EpreuveRepository repository;
  private final EpreuveMapper mapper;

  public EpreuveServiceImpl(EpreuveRepository repository, EpreuveMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public EpreuveDTO save(EpreuveRequestDTO dto) {
    return mapper.asDTO(repository.save(mapper.asEntity(dto)));
  }

  @Override
  public List<LiteEpreuveDTO> save(List<ImportEpreuveRequestDTO> dtos) {
    return  ((List<Epreuve>) repository.saveAll(mapper.asEntityList(dtos)))
            .stream()
            .map(mapper::asLite)
            .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    repository.deleteById(id);
  }

  @Override
  public EpreuveDTO getOne(Long id) {
    return mapper.asDTO(findById(id));
  }

  @Override
  public Epreuve findById(Long id) {
    return repository.findById(id).orElseThrow(
            () ->  new ResourceNotFoundException("L'epreuve avec l'id " + id + " n'existe pas")
    );
  }

  @Override
  public List<LiteEpreuveDTO> findAll() {
    return ((List<Epreuve>) repository.findAll()).stream().map(mapper::asLite).collect(Collectors.toList());
  }

  @Override
  public Page<LiteEpreuveDTO> findAll(Pageable pageable) {
    Page<Epreuve> entityPage = repository.findAll(pageable);
    List<Epreuve> entities = entityPage.getContent();
    return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
  }

  @Override
  public EpreuveDTO update(EpreuveRequestDTO dto, Long id) {
    Epreuve exist =  findById(id);
    dto.setId(exist.getId());
    return mapper.asDTO(repository.save(mapper.asEntity(dto)));
  }

  @Override
  public boolean equalsByDto(EpreuveRequestDTO dto, Long id) {
    Epreuve ressource = repository.findById(id).orElse(null);
    if (ressource == null) return false;
    return !ressource.equals(dto);
  }
}
