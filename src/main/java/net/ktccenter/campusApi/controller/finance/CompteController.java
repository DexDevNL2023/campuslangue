package net.ktccenter.campusApi.controller.finance;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportCompteRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteCompteDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.CompteDTO;
import net.ktccenter.campusApi.dto.request.scolarite.CompteRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CompteController {
    public CompteDTO save(@RequestBody CompteRequestDTO dto);

    public List<LiteCompteDTO> saveAll(@RequestBody List<ImportCompteRequestDTO> dtos);

    public CompteDTO findById(@PathVariable("id") Long id);

    public void delete(@PathVariable("id") Long id);

    public List<LiteCompteDTO> list();

    public Page<LiteCompteDTO> pageQuery(Pageable pageable);

    public CompteDTO update(@RequestBody CompteRequestDTO dto, @PathVariable("id") Long id);
}
