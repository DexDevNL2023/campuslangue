package net.ktccenter.campusApi.controller.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportRoleRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteRoleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.RoleDTO;
import net.ktccenter.campusApi.dto.request.administration.RoleRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface RoleController {
    RoleDTO save(@RequestBody RoleRequestDTO role);

    List<LiteRoleDTO> saveAll(@RequestBody List<ImportRoleRequestDTO> dtos);

    RoleDTO findById(@PathVariable("id") Long id);

    void delete(@PathVariable("id") Long id);

    List<LiteRoleDTO> list();

    Page<LiteRoleDTO> pageQuery(Pageable pageable);

    void update(@RequestBody RoleRequestDTO dto, @PathVariable("id") Long id);
}
