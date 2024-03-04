package net.ktccenter.campusApi.controller.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportEtudiantRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteEtudiantDTO;
import net.ktccenter.campusApi.dto.reponse.branch.EtudiantBranchDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.EtudiantDTO;
import net.ktccenter.campusApi.dto.request.scolarite.EtudiantRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface EtudiantController {
    EtudiantDTO save(@RequestBody EtudiantRequestDTO dto);

    List<LiteEtudiantDTO> saveAll(@RequestBody List<ImportEtudiantRequestDTO> dtos);

    EtudiantDTO findById(@PathVariable("id") Long id);

    void delete(@PathVariable("id") Long id);

    List<EtudiantBranchDTO> list();

    List<EtudiantBranchDTO> getAllBySession(@PathVariable("sessionId") Long sessionId, @PathVariable("salleId") Long salleId, @PathVariable("niveauId") Long niveauId);

    @GetMapping("/is/rattrapage")
    List<EtudiantBranchDTO> getAllIsRattapage();

    List<EtudiantBranchDTO> getAllWithUnpaid();

    Page<LiteEtudiantDTO> pageQuery(Pageable pageable);

    void update(@RequestBody EtudiantRequestDTO dto, @PathVariable("id") Long id);
}
