package net.ktccenter.campusApi.service.administration.impl;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.dao.administration.DroitRepository;
import net.ktccenter.campusApi.dao.administration.ModuleRepository;
import net.ktccenter.campusApi.dao.administration.RoleDroitRepository;
import net.ktccenter.campusApi.dao.administration.RoleRepository;
import net.ktccenter.campusApi.dto.reponse.administration.*;
import net.ktccenter.campusApi.dto.request.administration.PermissionDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.entities.administration.Droit;
import net.ktccenter.campusApi.entities.administration.Module;
import net.ktccenter.campusApi.entities.administration.Role;
import net.ktccenter.campusApi.entities.administration.RoleDroit;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.administration.ModuleMapper;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class AutorisationServiceImpl implements AutorisationService {
    private final RoleRepository roleRepository;
    private final DroitRepository droitRepository;
    private final ModuleRepository moduleRepository;
    private final ModuleMapper moduleMapper;
    private final RoleDroitRepository roleDroitRepository;


    public AutorisationServiceImpl(RoleRepository roleRepository, DroitRepository droitRepository, ModuleRepository moduleRepository, ModuleMapper moduleMapper, RoleDroitRepository roleDroitRepository) {
        this.roleRepository = roleRepository;
        this.droitRepository = droitRepository;
        this.moduleRepository = moduleRepository;
        this.moduleMapper = moduleMapper;
        this.roleDroitRepository = roleDroitRepository;
    }

    @Override
    public List<RoleDTO> findAll() {
        try {
            List<RoleDTO> roleResponseDTOS = new ArrayList<>();
            List<Role> roles = (List<Role>) roleRepository.findAll();
            for (Role role : roles) {
                RoleDTO roleResponseDTO = new RoleDTO();
                roleResponseDTO.setId(role.getId());
                roleResponseDTO.setLibelle(role.getLibelle());
                roleResponseDTOS.add(roleResponseDTO);
            }
            return roleResponseDTOS;
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public RoleDetailsDTO getOne(Long id) {
        log.info(id.toString());
        Role role = roleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Le rôle avec l'id "+id+" introuvable")
        );
        RoleDetailsDTO dto = new RoleDetailsDTO();
        dto.setId(role.getId());
        dto.setLibelle(role.getLibelle());
        List<ModulePermissionDTO> modulePermissions = new ArrayList<>();
        for(Module m : moduleRepository.findAll()){
            ModulePermissionDTO modulePermissionDTO = new ModulePermissionDTO();
            modulePermissionDTO.setId(m.getId());
            modulePermissionDTO.setName(m.getName());
            List<PermissionResponseDTO> permissions = new ArrayList<>();
            for(Droit d : m.getDroits()){
                RoleDroit r = roleDroitRepository.findByRoleAndDroit(role, d);
                PermissionResponseDTO permissionResponseDTO = new PermissionResponseDTO();
                permissionResponseDTO.setId(r.getId());
                permissionResponseDTO.setPermission(d.getLibelle());
                permissionResponseDTO.setHasPermission(r.getHasDroit());
                permissions.add(permissionResponseDTO);
            }
            modulePermissionDTO.setPermissions(permissions);
            modulePermissions.add(modulePermissionDTO);
        }
        dto.setModules(modulePermissions);
        return dto;
    }

    @Override
    public void save(RoleDTO roleDTO) {
        try{
            Role exitRole = roleRepository.findByLibelle(roleDTO.getLibelle()).orElse(null);
            if(exitRole != null) {
                throw new APIException("Le rôle "+roleDTO.getLibelle()+" existe déja.");
            }

            Role role = new Role();
            role.setLibelle(roleDTO.getLibelle());
            List<Droit> list = (List<Droit>) droitRepository.findAll();
            for (Droit d : list) {
                RoleDroit permission = new RoleDroit(role, d, d.getIsDefault());
                role.getPermissions().add(permission);
            }
            roleRepository.save(role);
        }catch (Exception e){
            throw new APIException(e.getMessage());
        }
    }

    @Override
    public void update(RoleDTO roleDTO) {
        try{
            Role role = roleRepository.findById(roleDTO.getId()).orElse(null);
            if(role != null){
                Role exitByLibelle = roleRepository.findByLibelle(roleDTO.getLibelle()).orElse(null);
                if(exitByLibelle == null){
                    role.setLibelle(roleDTO.getLibelle());
                    roleRepository.save(role);
                }else if (!Objects.equals(exitByLibelle.getId(), roleDTO.getId())) {
                    throw new APIException("Le rôle "+roleDTO.getLibelle()+" existe déja.");
                }

            }else{
                throw new ResourceNotFoundException("le rôle avec l'id "+roleDTO.getId()+" n'existe pas");
            }
        }catch (Exception e){
            throw new APIException(e.getMessage());
        }
    }

    @Override
    public void changeAutorisation(PermissionDTO permissionDTO) {
        log.info("1");
        RoleDroit roleDroit = roleDroitRepository.findById(permissionDTO.getPermissionId()).orElseThrow(
                () -> new ResourceNotFoundException("Autorisation introuvable pour l'id "+permissionDTO.getPermissionId())
        );
        log.info("2");
        roleDroit.setHasDroit(permissionDTO.getHasPermission());
        log.info("3");
        roleDroitRepository.save(roleDroit);
        log.info("4");
    }

    @Override
    public void changeIsDefaultDroit(DroitDTO droitDTO) {
        Droit droit = droitRepository.findById(droitDTO.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Autorisation introuvable")
        );
        droit.setIsDefault(droitDTO.getIsDefault());
        droitRepository.save(droit);
        if(droit.getIsDefault()) {
            List<RoleDroit> permissionsToSAVe = new ArrayList<>();
            List<RoleDroit> permissions = roleDroitRepository.findAllByDroit(droit);
            for(RoleDroit  r: permissions){
                r.setHasDroit(true);
                permissionsToSAVe.add(r);
            }
            roleDroitRepository.saveAll(permissionsToSAVe);
        }


    }

    @Override
    public List<ModuleDTO> allModules() {
        List<Module> list = (List<Module>) moduleRepository.findAll();
        return list.stream()
                .map(moduleMapper::asDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void addDroit(SaveDroitDTO saveDroitDTO) {
        try{
            Module module = moduleRepository.findByName(saveDroitDTO.getModule()).orElse(null);

            if(module == null){
                module = new Module();
                module.setName(saveDroitDTO.getModule());
                moduleRepository.save(module);
            }
            Droit droit = droitRepository.findByKey(saveDroitDTO.getKey()).orElse(null);

            if (droit == null){
                droit = new Droit();
                droit.setLibelle(saveDroitDTO.getDroit());
                droit.setKey(saveDroitDTO.getKey());
                droit.setIsDefault(saveDroitDTO.getIsDefault());
                droit.setVerbe(saveDroitDTO.getVerbe());
                droit.setModule(module);
                module.getDroits().add(droit);
                droitRepository.save(droit);
                log.info("ici3");

                for (Role role : roleRepository.findAll()){
                    RoleDroit permission = new RoleDroit(role, droit, role.getIsSuper());
                    role.getPermissions().add(permission);
                    roleRepository.save(role);
                    log.info("ici22");
                }
            }else{
                log.info("Le droit avec la clé "+saveDroitDTO.getKey() + " existe déja");
            }
        }catch (Exception e){
            throw new APIException(e.getMessage());
        }
    }

    void refreshPermissions(String key){
        Droit droit = droitRepository.findByKey(key).orElse(null);
        if (droit == null){
            log.info("Le droit avec la clé "+key + " existe déja");
        }
        log.info("ici21");
        List<RoleDroit> permissions = new ArrayList<>();
        for (Role role : roleRepository.findAll()){
            assert droit != null;
            RoleDroit permission = new RoleDroit(role, droit, droit.getIsDefault());
            permissions.add(permission);
            log.info("ici22");
        }
        roleDroitRepository.saveAll(permissions);
        log.info("ici23");
    }
}

