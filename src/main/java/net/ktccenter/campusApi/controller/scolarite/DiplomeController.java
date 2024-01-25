package net.ktccenter.campusApi.controller.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportDiplomeRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteDiplomeDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.DiplomeDTO;
import net.ktccenter.campusApi.dto.request.scolarite.DiplomeRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface DiplomeController {
    public DiplomeDTO save(@RequestBody DiplomeRequestDTO dto);

    public List<LiteDiplomeDTO> saveAll(@RequestBody List<ImportDiplomeRequestDTO> dtos);

    public DiplomeDTO findById(@PathVariable("id") Long id);

    public void delete(@PathVariable("id") Long id);

    public List<LiteDiplomeDTO> list();

    public Page<LiteDiplomeDTO> pageQuery(Pageable pageable);

    public DiplomeDTO update(@RequestBody DiplomeRequestDTO dto, @PathVariable("id") Long id);
}
