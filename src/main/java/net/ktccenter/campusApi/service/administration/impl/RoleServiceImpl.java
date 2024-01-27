package net.ktccenter.campusApi.service.administration.impl;

import net.ktccenter.campusApi.dao.administration.DroitRepository;
import net.ktccenter.campusApi.dao.administration.RoleDroitRepository;
import net.ktccenter.campusApi.dao.administration.RoleRepository;
import net.ktccenter.campusApi.dto.importation.administration.ImportRoleRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteDroitDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteRoleDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteRoleDroitDTO;
import net.ktccenter.campusApi.dto.reponse.administration.RoleDTO;
import net.ktccenter.campusApi.dto.request.administration.RoleRequestDTO;
import net.ktccenter.campusApi.entities.administration.Droit;
import net.ktccenter.campusApi.entities.administration.Role;
import net.ktccenter.campusApi.entities.administration.RoleDroit;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.administration.RoleMapper;
import net.ktccenter.campusApi.service.administration.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;
    private final RoleMapper mapper;
    private final DroitRepository droitRepository;
    private final RoleDroitRepository roleDroitRepository;

    public RoleServiceImpl(RoleRepository repository, RoleMapper mapper, DroitRepository droitRepository, RoleDroitRepository roleDroitRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.droitRepository = droitRepository;
        this.roleDroitRepository = roleDroitRepository;
    }

    @Override
    public RoleDTO save(RoleRequestDTO entity) {
        Role role = mapper.asEntity(entity);
        List<Droit> list = (List<Droit>) droitRepository.findAll();
        for (Droit droit : list) {
            if (droit.getIsDefault()) {
                RoleDroit permission = new RoleDroit(role, droit, droit.getIsDefault());
                roleDroitRepository.save(permission);
            }
        }
        return mapper.asDTO(repository.save(role));
    }

    @Override
    public List<LiteRoleDTO> save(List<ImportRoleRequestDTO> dtos) {
        return  ((List<Role>) repository.saveAll(mapper.asEntityList(dtos)))
                .stream()
                .map(mapper::asLite)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        Role role = findById(id);
        if (!getAllPermissionsByRole(role).isEmpty())
            throw new ResourceNotFoundException("Le role avec l'id " + id + " ne peut pas etre supprimé car elle est utilisé par d'autre ressource");
        repository.deleteById(id);
    }

    private List<LiteRoleDroitDTO> getAllPermissionsByRole(Role role) {
        return roleDroitRepository.findAllByRole(role).stream().map(this::buildPermissionLiteDto).collect(Collectors.toList());
    }

    private LiteRoleDroitDTO buildPermissionLiteDto(RoleDroit roleDroit) {
        LiteRoleDroitDTO permissionLiteDto = new LiteRoleDroitDTO(roleDroit);
        permissionLiteDto.setDroit(new LiteDroitDTO(roleDroit.getDroit()));
        return permissionLiteDto;
    }

    @Override
    public RoleDTO getOne(Long id) {
        return buildRoleDto(findById(id));
    }

    private RoleDTO buildRoleDto(Role role) {
        RoleDTO dto = mapper.asDTO(role);
        dto.setPermissions(getAllPermissionsByRole(role));
        return dto;
    }

    @Override
    public Role findById(Long id) {
        return repository.findById(id).orElseThrow(
                () ->  new ResourceNotFoundException("Le role avec l'id " + id + " n'existe pas")
        );
    }

    @Override
    public List<LiteRoleDTO> findAll() {
        return ((List<Role>) repository.findAll()).stream().map(mapper::asLite).collect(Collectors.toList());
    }

    @Override
    public Page<LiteRoleDTO> findAll(Pageable pageable) {
        Page<Role> entityPage = repository.findAll(pageable);
        List<Role> entities = entityPage.getContent();
        return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
    }


    @Override
    public void update(RoleRequestDTO entity, Long id) {
        Role exist = findById(id);
        Role role = mapper.asEntity(entity);
        role.setId(exist.getId());
        List<Droit> list = (List<Droit>) droitRepository.findAll();
        for (Droit droit : list) {
            if (droit.getIsDefault()) {
                RoleDroit permission = new RoleDroit(role, droit, droit.getIsDefault());
                roleDroitRepository.save(permission);
            }
        }
        mapper.asDTO(repository.save(role));
    }

    @Override
    public boolean existsByRoleName(String libelle) {
        Role role = repository.findByRoleName(libelle);
        return (role == null);
    }

    @Override
    public boolean equalsByDto(RoleRequestDTO dto, Long id) {
        Role ressource = repository.findByRoleName(dto.getLibelle());
        if (ressource == null) return false;
        return !ressource.getId().equals(id);
    }

    @Override
    public Role findByLibelle(String libelle) {
        return repository.findByLibelle(libelle).orElseThrow(
                () ->  new ResourceNotFoundException("Le role avec le nom " + libelle + " n'existe pas")
        );
    }
}
