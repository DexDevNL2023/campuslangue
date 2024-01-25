package net.ktccenter.campusApi.service.administration.impl;

import net.ktccenter.campusApi.dao.administration.RoleRepository;
import net.ktccenter.campusApi.dto.importation.administration.ImportRoleRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteRoleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.RoleDTO;
import net.ktccenter.campusApi.dto.request.administration.RoleRequestDTO;
import net.ktccenter.campusApi.entities.administration.Role;
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

    public RoleServiceImpl(RoleRepository repository, RoleMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public RoleDTO save(RoleRequestDTO entity) {
        return mapper.asDTO(repository.save(mapper.asEntity(entity)));
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
        if (!role.getPermissions().isEmpty())
            throw new ResourceNotFoundException("Le role avec l'id " + id + " ne peut pas etre supprimé car elle est utilisé par d'autre ressource");
        repository.deleteById(id);
    }

    @Override
    public RoleDTO getOne(Long id) {

        return mapper.asDTO(findById(id));
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
    public RoleDTO update(RoleRequestDTO entity, Long id) {
      Role exist =  findById(id);
      entity.setId(exist.getId());
      return mapper.asDTO(repository.save(mapper.asEntity(entity)));
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
