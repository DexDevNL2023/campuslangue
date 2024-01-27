package net.ktccenter.campusApi.service.scolarite.impl;

import net.ktccenter.campusApi.dao.scolarite.PaiementRepository;
import net.ktccenter.campusApi.dao.scolarite.RubriqueRepository;
import net.ktccenter.campusApi.dto.importation.scolarite.ImportRubriqueRequestDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteModePaiementDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LitePaiementDTO;
import net.ktccenter.campusApi.dto.lite.scolarite.LiteRubriqueDTO;
import net.ktccenter.campusApi.dto.reponse.scolarite.RubriqueDTO;
import net.ktccenter.campusApi.dto.request.scolarite.RubriqueRequestDTO;
import net.ktccenter.campusApi.entities.scolarite.Paiement;
import net.ktccenter.campusApi.entities.scolarite.Rubrique;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.scolarite.RubriqueMapper;
import net.ktccenter.campusApi.service.scolarite.RubriqueService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class RubriqueServiceImpl implements RubriqueService {
	private final RubriqueRepository repository;
	private final RubriqueMapper mapper;
	private final PaiementRepository paiementRepository;

	public RubriqueServiceImpl(RubriqueRepository repository, RubriqueMapper mapper, PaiementRepository paiementRepository) {
		this.repository = repository;
		this.mapper = mapper;
		this.paiementRepository = paiementRepository;
	}

	@Override
	public RubriqueDTO save(RubriqueRequestDTO dto) {
		return buildRubriqueDto(repository.save(mapper.asEntity(dto)));
	}

	private RubriqueDTO buildRubriqueDto(Rubrique rubrique) {
		RubriqueDTO dto = mapper.asDTO(rubrique);
		dto.setPaiements(getAllPaiementsForRubrique(rubrique));
		return dto;
	}

	@Override
	public List<LiteRubriqueDTO> save(List<ImportRubriqueRequestDTO> dtos) {
		return  ((List<Rubrique>) repository.saveAll(mapper.asEntityList(dtos)))
				.stream()
				.map(mapper::asLite)
				.collect(Collectors.toList());
	}

	@Override
	public void deleteById(Long id) {
		Rubrique rubrique = findById(id);
		if (!getAllPaiementsForRubrique(rubrique).isEmpty())
			throw new ResourceNotFoundException("La rubrique avec l'id " + id + " ne peut pas etre supprimé car elle est utilisé par d'autre ressource");
		repository.delete(rubrique);
	}

	private Set<LitePaiementDTO> getAllPaiementsForRubrique(Rubrique rubrique) {
		return paiementRepository.findAllByRubrique(rubrique).stream().map(this::buildPaiementLiteDto).collect(Collectors.toSet());
	}

	private LitePaiementDTO buildPaiementLiteDto(Paiement entity) {
		LitePaiementDTO lite = new LitePaiementDTO();
		lite.setId(entity.getId());
		lite.setRefPaiement(entity.getRefPaiement());
		lite.setMontant(entity.getMontant());
		lite.setDatePaiement(entity.getDatePaiement());
		lite.setModePaiement(new LiteModePaiementDTO(entity.getModePaiement()));
		lite.setRubrique(new LiteRubriqueDTO(entity.getRubrique()));
		return lite;
	}

	@Override
	public Rubrique findById(Long id) {
		return repository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("La rubrique avec l'id " + id + " n'existe pas")
		);
	}

	@Override
	public RubriqueDTO getOne(Long id) {
		return buildRubriqueDto(findById(id));
	}

	@Override
	public List<LiteRubriqueDTO> findAll() {
		return ((List<Rubrique>) repository.findAll()).stream().map(mapper::asLite).collect(Collectors.toList());
	}

	@Override
	public Page<LiteRubriqueDTO> findAll(Pageable pageable) {
		Page<Rubrique> entityPage = repository.findAll(pageable);
		List<Rubrique> entities = entityPage.getContent();
		return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
	}


	@Override
	public void update(RubriqueRequestDTO dto, Long id) {
		Rubrique exist = findById(id);
		dto.setId(exist.getId());
		buildRubriqueDto(repository.save(mapper.asEntity(dto)));
	}


	@Override
	public boolean existByCode(String code) {
		return repository.findByCode(code).isPresent();
	}

	@Override
	public boolean equalsByDto(RubriqueRequestDTO dto, Long id) {
		Rubrique ressource = repository.findByCode(dto.getCode()).orElse(null);
		if (ressource == null) return false;
		return !ressource.getId().equals(id);
	}

	@Override
	public Rubrique findByCode(String code) {
		return repository.findByCode(code).orElseThrow(
				() ->  new ResourceNotFoundException("La rubrique avec le code " + code + " n'existe pas")
		);
	}

	@Override
	public Rubrique findByCodeAndLibelle(String code, String libelle) {
		return repository.findByCodeAndLibelle(code, libelle).orElseThrow(
				() ->  new ResourceNotFoundException("La rubrique avec le code " + code + " et le libelle "+ libelle +" n'existe pas")
		);
	}
}