package net.ktccenter.campusApi.service.administration.impl;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.dao.administration.InstitutionRepository;
import net.ktccenter.campusApi.dao.administration.JourOuvrableRepository;
import net.ktccenter.campusApi.dao.administration.ParametreInstitutionRepository;
import net.ktccenter.campusApi.dto.importation.administration.ImportInstitutionRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteInstitutionDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteJourOuvrableDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteParametreInstitutionDTO;
import net.ktccenter.campusApi.dto.reponse.administration.InstitutionDTO;
import net.ktccenter.campusApi.dto.request.administration.InstitutionRequestDTO;
import net.ktccenter.campusApi.dto.request.administration.ParametreInstitutionRequestDTO;
import net.ktccenter.campusApi.entities.administration.Institution;
import net.ktccenter.campusApi.entities.administration.JourOuvrable;
import net.ktccenter.campusApi.entities.administration.ParametreInstitution;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.administration.InstitutionMapper;
import net.ktccenter.campusApi.service.administration.InstitutionService;
import net.ktccenter.campusApi.utils.MyUtils;
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
public class InstitutionServiceImpl implements InstitutionService {
  private final InstitutionRepository repository;
  private final InstitutionMapper mapper;
    private final ParametreInstitutionRepository parametreRepository;
    private final JourOuvrableRepository jourOuvrablesRepository;

    public InstitutionServiceImpl(InstitutionRepository repository, InstitutionMapper mapper, ParametreInstitutionRepository parametreRepository, JourOuvrableRepository jourOuvrablesRepository) {
      this.repository = repository;
      this.mapper = mapper;
        this.parametreRepository = parametreRepository;
        this.jourOuvrablesRepository = jourOuvrablesRepository;
  }

  @Override
  public InstitutionDTO save(InstitutionRequestDTO dto) {
      if(!MyUtils.isValidEmailAddress(dto.getEmail()))
          throw new ResourceNotFoundException("L'email " + dto.getEmail() + " est invalide.");
      return mapper.asDTO(repository.save(mapper.asEntity(dto)));
  }

  @Override
  public List<LiteInstitutionDTO> save(List<ImportInstitutionRequestDTO> dtos) {
      for (ImportInstitutionRequestDTO dto : dtos) {
          if(!MyUtils.isValidEmailAddress(dto.getEmail()))
              throw new ResourceNotFoundException("L'email " + dto.getEmail() + " est invalide.");
      }
      return  ((List<Institution>) repository.saveAll(mapper.asEntityList(dtos)))
              .stream()
              .map(mapper::asLite)
              .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    //  repository.deleteById(id);
  }

  @Override
  public Institution findById(Long id) {
      return repository.findById(id).orElseThrow(
              () ->  new ResourceNotFoundException("L'institution avec l'id " + id + " n'existe pas")
      );
  }

  @Override
  public InstitutionDTO getOne(Long id) {
      return mapper.asDTO(findById(id));
  }

  @Override
  public List<LiteInstitutionDTO> findAll() {
      return ((List<Institution>) repository.findAll()).stream().map(mapper::asLite).collect(Collectors.toList());
  }

  @Override
  public Page<LiteInstitutionDTO> findAll(Pageable pageable) {
      Page<Institution> entityPage = repository.findAll(pageable);
      List<Institution> entities = entityPage.getContent();
      return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
  }


  @Override
  public InstitutionDTO update(InstitutionRequestDTO dto, Long id) {
      if(!MyUtils.isValidEmailAddress(dto.getEmail()))
          throw new ResourceNotFoundException("L'email " + dto.getEmail() + " est invalide.");
    Institution exist = findById(id);
    dto.setId(exist.getId());
      return mapper.asDTO(repository.save(mapper.asEntity(dto)));
  }

  @Override
  public InstitutionDTO getCurrentInstitution() {
      Institution institution = repository.findFirstByOrderByName().orElseThrow(
              () ->  new ResourceNotFoundException("Aucune institution n'est enregistrée")
      );
      InstitutionDTO result = mapper.asDTO(institution);
      result.setJourOuvrables(buildJoursOuvrable());
      result.setParametres(buildParametres());
      return result;
  }

    private LiteParametreInstitutionDTO buildParametres() {
        return new LiteParametreInstitutionDTO(parametreRepository.findFirstByOrderById());
    }

    private List<LiteJourOuvrableDTO> buildJoursOuvrable() {
        List<JourOuvrable> jourOuvrables = (List<JourOuvrable>) jourOuvrablesRepository.findAll();
        return jourOuvrables.stream().map(j -> new LiteJourOuvrableDTO(j)).collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return repository.findByName(name).isPresent();
    }

    @Override
    public boolean equalsByDto(InstitutionRequestDTO dto, Long id) {
        Institution ressource = repository.findByName(dto.getName()).orElse(null);
        if (ressource == null) return false;
        return !ressource.getId().equals(id);
    }

    @Override
    public void updateParametres(ParametreInstitutionRequestDTO dto) {
        ParametreInstitution parametres = parametreRepository.findFirstByOrderById();
        parametres.setBareme(dto.getBareme());
        parametres.setDevise(dto.getDevise());
        parametres.setDureeCours(dto.getDureeCours());
        parametreRepository.save(parametres);
    }
}
