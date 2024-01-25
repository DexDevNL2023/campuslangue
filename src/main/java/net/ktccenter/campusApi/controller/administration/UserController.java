package net.ktccenter.campusApi.controller.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportUserRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteUserDTO;
import net.ktccenter.campusApi.dto.reponse.administration.UserDTO;
import net.ktccenter.campusApi.dto.request.administration.UserRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserController {
    public UserDTO save(@RequestBody UserRequestDTO user);

    public List<LiteUserDTO> saveAll(@RequestBody List<ImportUserRequestDTO> dtos);

    public UserDTO findById(@PathVariable("id") Long id);

    public void delete(@PathVariable("id") Long id);

    public List<LiteUserDTO> list();

    public Page<LiteUserDTO> pageQuery(Pageable pageable);

    public UserDTO update(@RequestBody UserRequestDTO dto, @PathVariable("id") Long id);
}
