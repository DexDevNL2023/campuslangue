package net.ktccenter.campusApi.controller.administration.impl;

import net.ktccenter.campusApi.controller.administration.UserController;
import net.ktccenter.campusApi.dto.importation.administration.ImportUserRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteUserDTO;
import net.ktccenter.campusApi.dto.reponse.administration.ProfileForCurrentUserDTO;
import net.ktccenter.campusApi.dto.reponse.administration.UserDTO;
import net.ktccenter.campusApi.dto.reponse.branch.UserBranchDTO;
import net.ktccenter.campusApi.dto.request.administration.SaveDroitDTO;
import net.ktccenter.campusApi.dto.request.administration.UpdateUserRequestDTO;
import net.ktccenter.campusApi.dto.request.administration.UserPasswordResetDTO;
import net.ktccenter.campusApi.dto.request.administration.UserRequestDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.service.MainService;
import net.ktccenter.campusApi.service.administration.AutorisationService;
import net.ktccenter.campusApi.service.administration.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/administration/users")
@RestController
@CrossOrigin("*")
public class UserControllerImpl implements UserController {

    @Autowired
    private MainService mainService;
    private final UserService service;
    private final AutorisationService autorisationService;

    public UserControllerImpl(UserService service, AutorisationService autorisationService) {
        this.service = service;
        this.autorisationService = autorisationService;
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    //@AuthorizeUser(actionKey = "user-add")
    public UserDTO save(@Valid @RequestBody UserRequestDTO dto) {
        autorisationService.addDroit(new SaveDroitDTO("UTILISATEURS", "Ajouter un utilisateur", "user-add", "POST", false));
        if (service.existsByNomAndEmail(dto.getNom(), dto.getEmail()))
            throw new RuntimeException("L'utilisateur avec le nom " + dto.getNom() + ", l'email " + dto.getEmail() + " existe déjà");
        return service.save(dto);
    }

    @Override
    @PostMapping("/imports")
    @ResponseStatus(HttpStatus.CREATED)
    //@AuthorizeUser(actionKey = "user-import")
    public List<LiteUserDTO> saveAll(@Valid @RequestBody List<ImportUserRequestDTO> dtos) {
        autorisationService.addDroit(new SaveDroitDTO("UTILISATEURS", "Importer des utilisateurs", "user-import", "POST", false));
        for (ImportUserRequestDTO dto : dtos) {
            if (service.existsByNomAndEmail(dto.getNom(), dto.getEmail()))
                throw new RuntimeException("L'utilisateur avec le nom " + dto.getNom() + ", l'email " + dto.getEmail() + " existe déjà");
        }
        return service.save(dtos);
    }

    @Override
    @GetMapping("/{id}")
    //@AuthorizeUser(actionKey = "user-details")
    public UserDTO findById(@PathVariable("id") Long id) {
        autorisationService.addDroit(new SaveDroitDTO("UTILISATEURS", "Détails d'un utilisateur", "user-details", "GET", false));
        return service.getOne(id);
    }


    @Override
    @DeleteMapping("/{id}")
    //@AuthorizeUser(actionKey = "user-delet")
    public void delete(@PathVariable("id") Long id) {
        autorisationService.addDroit(new SaveDroitDTO("UTILISATEURS", "Supprimer un utilisateur", "user-delet", "DELET", false));
        if (service.findById(id) == null) throw new RuntimeException("L'utilisateur avec l'id " + id + " n'existe pas");
        service.deleteById(id);
    }

    @Override
    @GetMapping
    //@AuthorizeUser(actionKey = "user-list")
    public List<UserBranchDTO> list() {
        autorisationService.addDroit(new SaveDroitDTO("UTILISATEURS", "Lister les utilisateurs", "user-list", "GET", false));
        return service.findAll();
    }

    @Override
    @GetMapping("/page-query")
    public Page<LiteUserDTO> pageQuery(Pageable pageable) {
        return service.findAll(pageable);
    }

    @Override
    @PutMapping("/{id}")
    //@AuthorizeUser(actionKey = "user-edit")
    public UserDTO update(@Valid @RequestBody UpdateUserRequestDTO dto, @PathVariable("id") Long id) {
        autorisationService.addDroit(new SaveDroitDTO("UTILISATEURS", "Modifier un utilisateur", "user-edit", "PUT", false));
        if (service.findById(id) == null) throw new RuntimeException("L'utilisateur avec l'id " + id + " n'existe pas");
        /*if (service.equalsByDto(dto, id))
            throw new RuntimeException("L'utilisateur avec les données suivante : " + dto.toString() + " existe déjà");*/
        return service.updateUserFrom(dto, id);
    }

    @Override
    @GetMapping(path= "/users/profile")
    public ProfileForCurrentUserDTO profile() {
        return service.findProfileCurrentUser();
    }

    @Override
    @PostMapping(path = "/users/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody UserPasswordResetDTO userPassword) {
        String response = service.changePassword();
        if(response == "OK"){
            return ResponseEntity.ok().body(response);
        }else{
            throw new APIException(response);
        }
    }
}
