package net.ktccenter.campusApi.controller.administration;

import net.ktccenter.campusApi.dto.LitePermissionModuleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.DroitDTO;
import net.ktccenter.campusApi.dto.request.administration.PermissionDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path= "/api/autorisations")
@CrossOrigin("*")
public class AutorisationRestController {
    private final AutorisationService roleService;

    public AutorisationRestController(AutorisationService roleService) {
        this.roleService = roleService;
    }

    @PutMapping(path="/changePermission")
    public void changePersmission(@RequestBody PermissionDTO permissionDTO) {
        roleService.addDroit(new SaveDroitDTO("AUTORISATIONS", "Changer une autorisation", "autorisation-add", "POST", false));
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
        roleService.addDroit(new SaveDroitDTO("AUTORISATIONS", "Définir une permission par défaut", "autorisation-permission-default", "PUT", false));
        if(droitDTO.getId() == null){
            throw new APIException("Id est obligatoire.");
        }
        if(droitDTO.getIsDefault() == null) {
            throw new APIException("IsDefault obligatoire.");
        }
        roleService.changeIsDefaultDroit(droitDTO);
    }

    @GetMapping(path = "/account/role/permission/{roleName}")
    public List<LitePermissionModuleDTO> getRolePersmission(@NotNull String roleName) {
        return roleService.getRolePersmission(roleName);
    }

    @GetMapping(path = "/all/permissions")
    public List<LitePermissionModuleDTO> getAllPermissions() {
        return roleService.getAllPermissions();
    }
}

