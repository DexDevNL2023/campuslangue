package net.ktccenter.campusApi.controller.administration;

import net.ktccenter.campusApi.dto.importation.administration.ImportModuleRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteModuleDTO;
import net.ktccenter.campusApi.dto.reponse.administration.ModuleDTO;
import net.ktccenter.campusApi.dto.request.administration.ModuleRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ModuleController {
    public ModuleDTO save(@RequestBody ModuleRequestDTO module);

    public List<LiteModuleDTO> saveAll(@RequestBody List<ImportModuleRequestDTO> dtos);

    public ModuleDTO findById(@PathVariable("id") Long id);

    public void delete(@PathVariable("id") Long id);

    public List<LiteModuleDTO> list();

    public Page<LiteModuleDTO> pageQuery(Pageable pageable);

    public ModuleDTO update(@RequestBody ModuleRequestDTO dto, @PathVariable("id") Long id);
}
