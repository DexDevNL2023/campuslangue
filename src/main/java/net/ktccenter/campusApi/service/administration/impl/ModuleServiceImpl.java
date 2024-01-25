package net.ktccenter.campusApi.service.administration.impl;

import net.ktccenter.campusApi.dao.administration.DroitRepository;
import net.ktccenter.campusApi.dao.administration.ModuleRepository;
import net.ktccenter.campusApi.dto.importation.administration.ImportModuleRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteModuleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.ModuleDTO;
import net.ktccenter.campusApi.dto.request.administration.ModuleRequestDTO;
import net.ktccenter.campusApi.entities.administration.Droit;
import net.ktccenter.campusApi.entities.administration.Module;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.administration.ModuleMapper;
import net.ktccenter.campusApi.service.administration.ModuleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ModuleServiceImpl implements ModuleService {
    private final ModuleRepository repository;
    private final ModuleMapper mapper;
    private final DroitRepository droitRepository;

    public ModuleServiceImpl(ModuleRepository repository, ModuleMapper mapper, DroitRepository droitRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.droitRepository = droitRepository;
    }

    @Override
    public ModuleDTO save(ModuleRequestDTO entity) {
        return mapper.asDTO(repository.save(mapper.asEntity(entity)));
    }

    @Override
    public List<LiteModuleDTO> save(List<ImportModuleRequestDTO> dtos) {
        return  ((List<Module>) repository.saveAll(mapper.asEntityList(dtos)))
                .stream()
                .map(mapper::asLite)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        Module module = findById(id);
        if (!getAllDroitsByModule(module).isEmpty())
            throw new ResourceNotFoundException("Le module avec l'id " + id + " ne peut pas etre supprimé car il est utilisé par d'autre ressource");
        repository.deleteById(id);
    }

    private List<Droit> getAllDroitsByModule(Module module) {
        return droitRepository.findAllDroitsByModule(module);
    }

    @Override
    public Module findById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Le module avec l'id " + id + " n'existe pas")
        );

    }

    @Override
    public ModuleDTO getOne(Long id) {
        return mapper.asDTO(findById(id));
    }

    @Override
    public List<LiteModuleDTO> findAll() {
        return ((List<Module>) repository.findAll()).stream().map(mapper::asLite).collect(Collectors.toList());
    }

    @Override
    public Page<LiteModuleDTO> findAll(Pageable pageable) {
        Page<Module> entityPage = repository.findAll(pageable);
        List<Module> entities = entityPage.getContent();
        return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
    }


    @Override
    public ModuleDTO update(ModuleRequestDTO entity, Long id) {
      Module exist = findById(id);
      entity.setId(exist.getId());
      return mapper.asDTO(repository.save(mapper.asEntity(entity)));
    }

    @Override
    public boolean existsByName(String name) {
        return repository.findByName(name).isPresent();
    }

    @Override
    public boolean equalsByDto(ModuleRequestDTO dto, Long id) {
        Module ressource = repository.findByName(dto.getName()).orElse(null);
        if (ressource == null) return false;
        return !ressource.getId().equals(id);
    }

    @Override
    public Module findByName(String name) {
        return repository.findByName(name).orElseThrow(
                () ->  new ResourceNotFoundException("Le module avec le nom " + name + " n'existe pas")
        );
    }
}
