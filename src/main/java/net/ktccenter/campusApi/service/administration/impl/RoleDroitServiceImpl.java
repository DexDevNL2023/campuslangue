package net.ktccenter.campusApi.service.administration.impl;

import net.ktccenter.campusApi.dao.administration.ModuleRepository;
import net.ktccenter.campusApi.dao.administration.RoleDroitRepository;
import net.ktccenter.campusApi.dto.importation.administration.ImportRoleDroitRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteModuleDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteRoleDroitDTO;
import net.ktccenter.campusApi.dto.reponse.PermissionModuleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.RoleDroitDTO;
import net.ktccenter.campusApi.dto.request.administration.RoleDroitRequestDTO;
import net.ktccenter.campusApi.entities.administration.Module;
import net.ktccenter.campusApi.entities.administration.RoleDroit;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.administration.RoleDroitMapper;
import net.ktccenter.campusApi.service.administration.RoleDroitService;
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
public class RoleDroitServiceImpl implements RoleDroitService {
    private final RoleDroitRepository repository;
    private final RoleDroitMapper mapper;
    private final ModuleRepository moduleRepository;

    public RoleDroitServiceImpl(RoleDroitRepository repository, RoleDroitMapper mapper, ModuleRepository moduleRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.moduleRepository = moduleRepository;
    }

    @Override
    public RoleDroitDTO save(RoleDroitRequestDTO entity) {
        return mapper.asDTO(repository.save(mapper.asEntity(entity)));
    }

    @Override
    public List<LiteRoleDroitDTO> save(List<ImportRoleDroitRequestDTO> dtos) {
        return  ((List<RoleDroit>) repository.saveAll(mapper.asEntityList(dtos)))
                .stream()
                .map(mapper::asLite)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public RoleDroit findById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Le role droit avec l'id " + id + " n'existe pas")
        );
    }

    @Override
    public RoleDroitDTO getOne(Long id) {
        return mapper.asDTO(findById(id));
    }

    @Override
    public List<PermissionModuleDTO> findAll() {
        List<PermissionModuleDTO> list = new ArrayList<>();
        List<Module> modules = (List<Module>) moduleRepository.findAll();
        for (Module module : modules) {
            PermissionModuleDTO dto = new PermissionModuleDTO();
            dto.setModule(new LiteModuleDTO(module));
            List<LiteRoleDroitDTO> data = repository.findAllByModule(module).stream().map(mapper::asLite).collect(Collectors.toList());
            dto.setData(data);
            list.add(dto);
        }
        return list;
    }

    @Override
    public void changePermissionStatus(Long permissionId) {
        RoleDroit exist = findById(permissionId);
        exist.setHasDroit(!exist.getHasDroit());
        repository.save(exist);
    }

    @Override
    public Page<LiteRoleDroitDTO> findAll(Pageable pageable) {
        Page<RoleDroit> entityPage = repository.findAll(pageable);
        List<RoleDroit> entities = entityPage.getContent();
        return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
    }

    @Override
    public RoleDroitDTO update(RoleDroitRequestDTO entity, Long id) {
      RoleDroit exist = findById(id);
      entity.setId(exist.getId());
        return mapper.asDTO(repository.save(mapper.asEntity(entity)));
    }
}
