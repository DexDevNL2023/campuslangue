package net.ktccenter.campusApi.service.administration.impl;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.dao.administration.JourOuvrableRepository;
import net.ktccenter.campusApi.dto.importation.administration.ImportJourOuvrableRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteJourOuvrableDTO;
import net.ktccenter.campusApi.dto.reponse.administration.JourOuvrableDTO;
import net.ktccenter.campusApi.dto.reponse.administration.RequestJourOuvrableDTO;
import net.ktccenter.campusApi.dto.request.administration.JourOuvrableRequestDTO;
import net.ktccenter.campusApi.entities.administration.JourOuvrable;
import net.ktccenter.campusApi.enums.Jour;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.administration.JourOuvrableMapper;
import net.ktccenter.campusApi.service.administration.JourOuvrableService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class JourOuvrableServiceImpl implements JourOuvrableService {
  private final JourOuvrableRepository repository;
  private final JourOuvrableMapper mapper;

  public JourOuvrableServiceImpl(JourOuvrableRepository repository, JourOuvrableMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public JourOuvrableDTO save(JourOuvrableRequestDTO dto) {
    return mapper.asDTO(repository.save(mapper.asEntity(dto)));
  }


  @Override
  public List<LiteJourOuvrableDTO> save(List<ImportJourOuvrableRequestDTO> dtos) {
    return ((List<JourOuvrable>) repository.saveAll(mapper.asEntityList(dtos)))
            .stream()
            .map(mapper::asLite)
            .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    repository.deleteById(id);
  }

  @Override
  public JourOuvrableDTO getOne(Long id) {
    return mapper.asDTO(findById(id));
  }

  @Override
  public JourOuvrable findById(Long id) {
    return repository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Le campus avec l'id " + id + " n'existe pas")
    );
  }

  @Override
  public Page<LiteJourOuvrableDTO> findAll(Pageable pageable) {
    Page<JourOuvrable> entityPage = repository.findAll(pageable);
    List<JourOuvrable> entities = entityPage.getContent();
    return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
  }


  @Override
  public JourOuvrableDTO update(JourOuvrableRequestDTO dto, Long id) {
    JourOuvrable exist = findById(id);
    dto.setId(exist.getId());
    return mapper.asDTO(repository.save(mapper.asEntity(dto)));
  }

  @Override
  public boolean equalsByDto(JourOuvrableRequestDTO dto, Long id) {
    JourOuvrable ressource = repository.findByJour(dto.getJour());
    if (ressource == null) return false;
    return !ressource.getId().equals(id);
  }

  @Override
  public List<LiteJourOuvrableDTO> findAll() {
    return ((List<JourOuvrable>) repository.findAll()).stream().map(this::buildLiteJourOuvrable).collect(Collectors.toList());
  }

  private LiteJourOuvrableDTO buildLiteJourOuvrable(JourOuvrable jourOuvrable) {
    return null;
  }

  @Override
  public List<RequestJourOuvrableDTO> getDefaultJour() {
    List<RequestJourOuvrableDTO> requests = new ArrayList<>();
    for (Jour j : Jour.orderedValues.stream().collect(Collectors.toList())) {
      requests.add(new RequestJourOuvrableDTO(j));
    }
    return requests;
  }
}
