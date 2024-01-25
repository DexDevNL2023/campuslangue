package net.ktccenter.campusApi.service.scolarite;

import net.ktccenter.campusApi.dto.importation.scolarite.ImportRubriqueRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteRubriqueDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.RubriqueDTO;
import net.ktccenter.campusApi.dto.request.scolarite.RubriqueRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.Rubrique;
import net.ktccenter.campusApi.service.GenericService;

public interface RubriqueService extends GenericService<Rubrique, RubriqueRequestDTO, RubriqueDTO, LiteRubriqueDTO, ImportRubriqueRequestDTO> {

	boolean equalsByDto(RubriqueRequestDTO dto, Long id);

	Rubrique findByCode(String code);

	boolean existByCode(String code);

	Rubrique findByCodeAndLibelle(String code, String libelle);
}
