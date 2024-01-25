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
    public RoleDTO save(@RequestBody RoleRequestDTO role);

    public List<LiteRoleDTO> saveAll(@RequestBody List<ImportRoleRequestDTO> dtos);

    public RoleDTO findById(@PathVariable("id") Long id);

    public void delete(@PathVariable("id") Long id);

    public List<LiteRoleDTO> list();

    public Page<LiteRoleDTO> pageQuery(Pageable pageable);

    public RoleDTO update(@RequestBody RoleRequestDTO dto, @PathVariable("id") Long id);
}
