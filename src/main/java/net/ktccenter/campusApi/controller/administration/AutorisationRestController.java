package net.ktccenter.campusApi.controller.administration;

import net.ktccenter.campusApi.dto.lite.administration.LitePermissionModuleAccesStatusDTO;
import net.ktccenter.campusApi.dto.lite.administration.LitePermissionModuleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.DroitDTO;
import net.ktccenter.campusApi.dto.request.administration.PermissionDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.administration.ModuleService;
import net.ktccenter.campusApi.service.administration.RoleDroitService;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path= "/api/autorisations")
@CrossOrigin("*")
public class AutorisationRestController {
    private final AutorisationService roleService;
    private final ModuleService moduleService;
    private final RoleDroitService roleDroitService;

    public AutorisationRestController(AutorisationService roleService, ModuleService moduleService, RoleDroitService roleDroitService) {
        this.roleService = roleService;
        this.moduleService = moduleService;
        this.roleDroitService = roleDroitService;
    }

    @PutMapping(path="/changePermission")
    public void changePersmission(@RequestBody PermissionDTO permissionDTO) {
        roleService.addDroit(new SaveDroitDTO("Administration", "Changer une autorisation", "autorisation-add", "POST", false));
        if(permissionDTO.getPermissionId() == null){
            throw new APIException("permissionId est obligatoire.");
        }
        if(permissionDTO.getHasPermission() == null) {
            throw new APIException("hasPermission obligatoire.");
        }
        roleService.changeAutorisation(permissionDTO);
    }

    @PutMapping(path="/changeDefaultPermission")
    public void changePersmission(@RequestBody DroitDTO droitDTO) {
        roleService.addDroit(new SaveDroitDTO("Administration", "Définir une permission par défaut", "autorisation-permission-default", "PUT", false));
        if(droitDTO.getId() == null){
            throw new APIException("Id est obligatoire.");
        }
        if(droitDTO.getIsDefault() == null) {
            throw new APIException("IsDefault obligatoire.");
        }
        roleService.changeIsDefaultDroit(droitDTO);
    }

    @GetMapping(path = "/account/role/permission/{roleName}")
    public List<LitePermissionModuleAccesStatusDTO> getRolePersmission(@NotNull @PathVariable("roleName") String roleName) {
        return roleService.getRolePersmission(roleName);
    }

    @GetMapping(path = "/all/permissions")
    public List<LitePermissionModuleDTO> getAllPermissions() {
        return roleService.getAllPermissions();
    }

    @GetMapping(path = "/change/module/status/{moduleId}")
    public void changeModuleStatus(@PathVariable("moduleId") Long moduleId) {
        moduleService.changeModuleStatus(moduleId);
    }

    @GetMapping(path = "/change/permission/status/{permissionId}")
    public void changePermissionStatus(@PathVariable("permissionId") Long permissionId) {
        roleDroitService.changePermissionStatus(permissionId);
    }
}

