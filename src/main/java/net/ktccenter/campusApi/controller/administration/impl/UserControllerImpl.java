package net.ktccenter.campusApi.controller.administration.impl;

import net.ktccenter.campusApi.controller.administration.UserController;
import net.ktccenter.campusApi.dto.importation.administration.ImportUserRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteUserDTO;
import net.ktccenter.campusApi.dto.reponse.administration.UserDTO;
import net.ktccenter.campusApi.dto.reponse.branch.UserBranchDTO;
import net.ktccenter.campusApi.dto.request.administration.UserPasswordResetDTO;
import net.ktccenter.campusApi.dto.request.administration.UserRequestDTO;
import net.ktccenter.campusApi.exceptions.APIException;
import net.ktccenter.campusApi.service.MainService;
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

    public UserControllerImpl(UserService service) {
        this.service = service;
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO save(@Valid @RequestBody UserRequestDTO dto) {
        if (service.existsByNomAndEmail(dto.getNom(), dto.getEmail()))
            throw new RuntimeException("L'utilisateur avec le nom " + dto.getNom() + ", l'email " + dto.getEmail() + " existe déjà");
        return service.save(dto);
    }

    @Override
    @PostMapping("/imports")
    @ResponseStatus(HttpStatus.CREATED)
    public List<LiteUserDTO> saveAll(@Valid @RequestBody List<ImportUserRequestDTO> dtos) {
        for (ImportUserRequestDTO dto : dtos) {
            if (service.existsByNomAndEmail(dto.getNom(), dto.getEmail()))
                throw new RuntimeException("L'utilisateur avec le nom " + dto.getNom() + ", l'email " + dto.getEmail() + " existe déjà");
        }
        return service.save(dtos);
    }

    @Override
    @GetMapping("/{id}")
    public UserDTO findById(@PathVariable("id") Long id) {return service.getOne(id);}


    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        if (service.findById(id) == null) throw new RuntimeException("L'utilisateur avec l'id " + id + " n'existe pas");
        service.deleteById(id);
    }

    @Override
    @GetMapping
    public List<UserBranchDTO> list() {
        return service.findAll();
    }

    @Override
    @GetMapping("/page-query")
    public Page<LiteUserDTO> pageQuery(Pageable pageable) {
        return service.findAll(pageable);
    }

    @Override
    @PutMapping("/{id}")
    public void update(@Valid @RequestBody UserRequestDTO dto, @PathVariable("id") Long id) {
        if (service.findById(id) == null) throw new RuntimeException("L'utilisateur avec l'id " + id + " n'existe pas");
        if (service.equalsByDto(dto, id))
            throw new RuntimeException("L'utilisateur avec les données suivante : " + dto.toString() + " existe déjà");
        service.update(dto, id);
    }

    @GetMapping(path= "/users/profile")
    UserDTO profile(){
        Long id = mainService.getCurrentUser() != null ? mainService.getCurrentUser().getId() : null;
        if (service.findById(id) == null) throw new RuntimeException("L'utilisateur avec l'id " + id + " n'existe pas");
        return service.getOne(id);
    }

    @PostMapping(path = "/users/changePassword")
    ResponseEntity<?> changePassword(@Valid @RequestBody UserPasswordResetDTO userPassword) {
        String response = service.changePassword();
        if(response == "OK"){
            return ResponseEntity.ok().body(response);
        }else{
            throw new APIException(response);
        }
    }
}
