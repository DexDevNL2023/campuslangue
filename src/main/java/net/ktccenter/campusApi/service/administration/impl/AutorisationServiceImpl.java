package net.ktccenter.campusApi.service.administration.impl;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.dao.administration.DroitRepository;
import net.ktccenter.campusApi.dao.administration.ModuleRepository;
import net.ktccenter.campusApi.dao.administration.RoleDroitRepository;
import net.ktccenter.campusApi.dao.administration.RoleRepository;
import net.ktccenter.campusApi.dto.LitePermissionModuleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.DroitDTO;
import net.ktccenter.campusApi.dto.request.administration.PermissionDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.entities.administration.Droit;
import net.ktccenter.campusApi.entities.administration.Module;
import net.ktccenter.campusApi.entities.administration.Role;
import net.ktccenter.campusApi.entities.administration.RoleDroit;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class AutorisationServiceImpl implements AutorisationService {
    private final RoleRepository roleRepository;
    private final DroitRepository droitRepository;
    private final ModuleRepository moduleRepository;
    private final RoleDroitRepository roleDroitRepository;

    public AutorisationServiceImpl(RoleRepository roleRepository, DroitRepository droitRepository, ModuleRepository moduleRepository, RoleDroitRepository roleDroitRepository) {
        this.roleRepository = roleRepository;
        this.droitRepository = droitRepository;
        this.moduleRepository = moduleRepository;
        this.roleDroitRepository = roleDroitRepository;
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
    public void addDroit(SaveDroitDTO saveDroitDTO) {
        try{
            Module module = moduleRepository.findByName(saveDroitDTO.getModule()).orElse(null);
            if(module == null){
                module = new Module();
                module.setName(saveDroitDTO.getModule());
                moduleRepository.save(module);
            }
            Optional<Droit> exist = droitRepository.findByKey(saveDroitDTO.getKey());
            if (!exist.isPresent()) {
                Droit droit = new Droit();
                droit.setLibelle(saveDroitDTO.getLibelle());
                droit.setKey(saveDroitDTO.getKey());
                droit.setIsDefault(saveDroitDTO.getIsDefault());
                droit.setVerbe(saveDroitDTO.getVerbe());
                droit.setModule(module);
                droit = droitRepository.save(droit);
                log.info("ici3");

                for (Role role : roleRepository.findAll()){
                    RoleDroit permission = new RoleDroit(role, droit, role.getIsSuper());
                    roleDroitRepository.save(permission);
                    log.info("ici22");
                }
            }else{
                log.info("Le droit avec la clé "+saveDroitDTO.getKey() + " existe déja");
            }
        }catch (Exception e){
            throw new APIException(e.getMessage());
        }
    }

    @Override
    public List<LitePermissionModuleDTO> getRolePersmission(String roleName) {
        Role role = roleRepository.findByRoleName(roleName);
        if (role == null) {
            throw new ResourceNotFoundException("Role introuvable");
        }
        return getAllPermissionsByRole(role);
    }

    private List<LitePermissionModuleDTO> getAllPermissionsByRole(Role role) {
        List<LitePermissionModuleDTO> list = new ArrayList<>();
        List<Module> modules = (List<Module>) moduleRepository.findAll();
        for (Module module : modules) {
            LitePermissionModuleDTO dto = new LitePermissionModuleDTO();
            dto.setModule(module.getName());
            List<String> data = roleDroitRepository.findAllByModuleAndRole(module, role).stream().map(p -> p.getDroit().getKey()).collect(Collectors.toList());
            dto.setPermissions(data);
            list.add(dto);
        }
        return list;
    }

    @Override
    public List<LitePermissionModuleDTO> getAllPermissions() {
        List<LitePermissionModuleDTO> list = new ArrayList<>();
        List<Module> modules = (List<Module>) moduleRepository.findAll();
        for (Module module : modules) {
            LitePermissionModuleDTO dto = new LitePermissionModuleDTO();
            dto.setModule(module.getName());
            List<String> data = roleDroitRepository.findAllByModule(module).stream().map(p -> p.getDroit().getKey()).collect(Collectors.toList());
            dto.setPermissions(data);
            list.add(dto);
        }
        return list;
    }
}

