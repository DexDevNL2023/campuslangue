package net.ktccenter.campusApi.controller.finance;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportCompteRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteCompteDTO;
import net.ktccenter.campusApi.dto.reponse.branch.CompteBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.CompteDTO;
import net.ktccenter.campusApi.dto.request.scolarite.CompteRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CompteController {
    CompteDTO save(@RequestBody CompteRequestDTO dto);

    List<LiteCompteDTO> saveAll(@RequestBody List<ImportCompteRequestDTO> dtos);

    CompteDTO findById(@PathVariable("id") Long id);

    void delete(@PathVariable("id") Long id);

    List<CompteBranchDTO> list();

    Page<LiteCompteDTO> pageQuery(Pageable pageable);

    void update(@RequestBody CompteRequestDTO dto, @PathVariable("id") Long id);
}
