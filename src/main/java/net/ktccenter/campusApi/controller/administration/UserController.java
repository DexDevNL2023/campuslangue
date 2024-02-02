package net.ktccenter.campusApi.controller.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportUserRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteUserDTO;
import net.ktccenter.campusApi.dto.reponse.administration.ProfileForCurrentUserDTO;
import net.ktccenter.campusApi.dto.reponse.administration.UserDTO;
import net.ktccenter.campusApi.dto.reponse.branch.UserBranchDTO;
import net.ktccenter.campusApi.dto.request.administration.UpdateUserRequestDTO;
import net.ktccenter.campusApi.dto.request.administration.UserPasswordResetDTO;
import net.ktccenter.campusApi.dto.request.administration.UserRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface UserController {
    UserDTO save(@RequestBody UserRequestDTO user);

    List<LiteUserDTO> saveAll(@RequestBody List<ImportUserRequestDTO> dtos);

    UserDTO findById(@PathVariable("id") Long id);

    void delete(@PathVariable("id") Long id);

    List<UserBranchDTO> list();

    Page<LiteUserDTO> pageQuery(Pageable pageable);

    UserDTO update(@RequestBody UpdateUserRequestDTO dto, @PathVariable("id") Long id);

    ResponseEntity<?> changePassword(@Valid @RequestBody UserPasswordResetDTO userPassword);

    ProfileForCurrentUserDTO profile();
}
