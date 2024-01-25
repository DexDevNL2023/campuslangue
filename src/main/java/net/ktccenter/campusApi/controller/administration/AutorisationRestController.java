package net.ktccenter.campusApi.controller.administration;

import net.ktccenter.campusApi.dto.reponse.administration.DroitDTO;
import net.ktccenter.campusApi.dto.reponse.administration.ModuleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.RoleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.RoleDetailsDTO;
import net.ktccenter.campusApi.dto.request.administration.PermissionDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path= "/api/autorisations")
@CrossOrigin("*")
public class AutorisationRestController {
    private final AutorisationService roleService;

    public AutorisationRestController(AutorisationService roleService) {
        this.roleService = roleService;
    }

    @GetMapping(path = "/roles")
    // @PreAuthorize("hasRole('SUPER')")
    public List<RoleDTO> roles() {
        roleService.addDroit(new SaveDroitDTO("AUTORISATIONS", "Lister les autorisations", "autorisation-list", "GET", false));
        return roleService.findAll();
    }

    @GetMapping(path = "/roles/{id}")
    // @PreAuthorize("hasRole('SUPER')")
    public RoleDetailsDTO getOne(@PathVariable(name = "id") Long id) {
        if(id == null){
            throw new APIException("Id obligatoire.");
        }
        //roleService.addDroit(new SaveDroitDTO("AUTORISATIONS", "Détails d'une autorisation", "autorisation-details", "GET", false));
        return roleService.getOne(id);
    }

    @PostMapping(path="/roles")
    public void save(@RequestBody RoleDTO roleDTO) {
        if(roleDTO.getLibelle() == null || roleDTO.getLibelle().trim().isEmpty()) {
            throw new APIException("Libellé obligatoire.");
        }
        roleService.addDroit(new SaveDroitDTO("AUTORISATIONS", "Ajouter une autorisation", "autorisation-add", "POST", false));
        roleService.save(roleDTO);
    }

    @PutMapping(path="/roles")
    public void update(@RequestBody RoleDTO roleDTO) {
        if(roleDTO.getId() == null){
            throw new APIException("Id obligatoire.");
        }
        if(roleDTO.getLibelle() == null || roleDTO.getLibelle().isEmpty()) {
            throw new APIException("Libellé obligatoire.");
        }
        roleService.addDroit(new SaveDroitDTO("AUTORISATIONS", "Modifier une autorisation", "autorisation-edit", "PUT", false));
        roleService.update(roleDTO);
    }

    @GetMapping(path = "/modules")
    // @PreAuthorize("hasRole('SUPER')")
    public List<ModuleDTO> modules() {
        roleService.addDroit(new SaveDroitDTO("AUTORISATIONS", "Lister les modules", "autorisation-module-list", "GET", false));
        return roleService.allModules();
    }

    @PutMapping(path="/changePermission")
    public void changePersmission(@RequestBody PermissionDTO permissionDTO) {
        if(permissionDTO.getPermissionId() == null){
            throw new APIException("permissionId est obligatoire.");
        }
        if(permissionDTO.getHasPermission() == null) {
            throw new APIException("hasPermission obligatoire.");
        }
        roleService.addDroit(new SaveDroitDTO("AUTORISATIONS", "Change une permission", "autorisation-permission-change", "PUT", false));
        roleService.changeAutorisation(permissionDTO);
    }

    @PutMapping(path="/changeDefaultPermission")
    public void changePersmission(@RequestBody DroitDTO droitDTO) {
        if(droitDTO.getId() == null){
            throw new APIException("Id est obligatoire.");
        }
        if(droitDTO.getIsDefault() == null) {
            throw new APIException("IsDefault obligatoire.");
        }
        roleService.addDroit(new SaveDroitDTO("AUTORISATIONS", "Définir une permission par défaut", "autorisation-permission-default", "PUT", false));
        roleService.changeIsDefaultDroit(droitDTO);
    }
}

