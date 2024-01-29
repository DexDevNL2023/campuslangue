package net.ktccenter.campusApi.service.cours.impl;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.dao.cours.EpreuveRepository;
import net.ktccenter.campusApi.dto.importation.cours.ImportEpreuveRequestDTO;
import net.ktccenter.campusApi.dto.lite.cours.LiteEpreuveDTO;
import net.ktccenter.campusApi.dto.reponse.branch.EpreuveBranchDTO;
import net.ktccenter.campusApi.dto.reponse.cours.EpreuveDTO;
import net.ktccenter.campusApi.dto.request.cours.EpreuveRequestDTO;
import net.ktccenter.campusApi.entities.administration.Branche;
import net.ktccenter.campusApi.entities.cours.Epreuve;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.cours.EpreuveMapper;
import net.ktccenter.campusApi.service.MainService;
import net.ktccenter.campusApi.service.cours.EpreuveService;
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
public class EpreuveServiceImpl extends MainService implements EpreuveService {
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
  public List<EpreuveBranchDTO> findAll() {
    List<Epreuve> epreuves = (List<Epreuve>) repository.findAll();
    List<EpreuveBranchDTO> result = new ArrayList<>();
    if (hasGrantAuthorized()) {
      for (Branche b : getAllBranches()) {
        result.add(buildData(b, epreuves));
      }
    } else {
      result.add(buildData(getCurrentUserBranch(), epreuves));
    }
    return result;
  }

  private EpreuveBranchDTO buildData(Branche branche, List<Epreuve> epreuves) {
    EpreuveBranchDTO dto = new EpreuveBranchDTO();
    dto.setBranche(brancheMapper.asLite(branche));
    dto.setData(epreuves.stream()
            .filter(e -> belongsToTheCurrentBranch(branche, e))
            .map(mapper::asLite)
            .collect(Collectors.toList()));
    return dto;
  }

  private boolean belongsToTheCurrentBranch(Branche branche, Epreuve e) {
    return e.getExamen().getInscription().getSession().getBranche().getId().equals(branche.getId());
  }

  @Override
  public Page<LiteEpreuveDTO> findAll(Pageable pageable) {
    Page<Epreuve> entityPage = repository.findAll(pageable);
    List<Epreuve> entities = entityPage.getContent();
    return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
  }

  @Override
  public void update(EpreuveRequestDTO dto, Long id) {
    Epreuve exist =  findById(id);
    dto.setId(exist.getId());
    mapper.asDTO(repository.save(mapper.asEntity(dto)));
  }

  @Override
  public boolean equalsByDto(EpreuveRequestDTO dto, Long id) {
    Epreuve ressource = repository.findById(id).orElse(null);
    if (ressource == null) return false;
    return !ressource.equals(dto);
  }
}
