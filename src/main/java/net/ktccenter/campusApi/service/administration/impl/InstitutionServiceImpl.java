package net.ktccenter.campusApi.service.administration.impl;

import lombok.extern.slf4j.Slf4j;
import net.ktccenter.campusApi.dao.administration.*;
import net.ktccenter.campusApi.dao.cours.PlageHoraireRepository;
import net.ktccenter.campusApi.dao.scolarite.SessionRepository;
import net.ktccenter.campusApi.dto.importation.administration.ImportInstitutionRequestDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteInstitutionDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteJourOuvrableDTO;
import net.ktccenter.campusApi.dto.lite.administration.LiteParametreInstitutionDTO;
import net.ktccenter.campusApi.dto.reponse.administration.InstitutionDTO;
import net.ktccenter.campusApi.dto.request.administration.InstitutionRequestDTO;
import net.ktccenter.campusApi.dto.request.administration.ParametreInstitutionRequestDTO;
import net.ktccenter.campusApi.entities.administration.*;
import net.ktccenter.campusApi.entities.cours.PlageHoraire;
import net.ktccenter.campusApi.enums.Jour;
import net.ktccenter.campusApi.exceptions.ResourceNotFoundException;
import net.ktccenter.campusApi.mapper.administration.InstitutionMapper;
import net.ktccenter.campusApi.service.administration.InstitutionService;
import net.ktccenter.campusApi.utils.MyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class InstitutionServiceImpl implements InstitutionService {
  private final InstitutionRepository repository;
  private final InstitutionMapper mapper;
    private final ParametreInstitutionRepository parametreRepository;
    private final JourOuvrableRepository jourOuvrablesRepository;
    private final PlageHoraireRepository plageHoraireRepository;
    private final SalleRepository salleRepository;
    private final OccupationSalleRepository occupationSalleRepository;
    private final SessionRepository sessionRepository;

    public InstitutionServiceImpl(InstitutionRepository repository, InstitutionMapper mapper, ParametreInstitutionRepository parametreRepository, JourOuvrableRepository jourOuvrablesRepository, PlageHoraireRepository plageHoraireRepository, SalleRepository salleRepository, OccupationSalleRepository occupationSalleRepository, SessionRepository sessionRepository) {
      this.repository = repository;
      this.mapper = mapper;
        this.parametreRepository = parametreRepository;
        this.jourOuvrablesRepository = jourOuvrablesRepository;
        this.plageHoraireRepository = plageHoraireRepository;
        this.salleRepository = salleRepository;
        this.occupationSalleRepository = occupationSalleRepository;
        this.sessionRepository = sessionRepository;
    }

  @Override
  public InstitutionDTO save(InstitutionRequestDTO dto) {
      if(!MyUtils.isValidEmailAddress(dto.getEmail()))
          throw new ResourceNotFoundException("L'email " + dto.getEmail() + " est invalide.");
      return mapper.asDTO(repository.save(mapper.asEntity(dto)));
  }

  @Override
  public List<LiteInstitutionDTO> save(List<ImportInstitutionRequestDTO> dtos) {
      for (ImportInstitutionRequestDTO dto : dtos) {
          if(!MyUtils.isValidEmailAddress(dto.getEmail()))
              throw new ResourceNotFoundException("L'email " + dto.getEmail() + " est invalide.");
      }
      return  ((List<Institution>) repository.saveAll(mapper.asEntityList(dtos)))
              .stream()
              .map(mapper::asLite)
              .collect(Collectors.toList());
  }

  @Override
  public void deleteById(Long id) {
    //  repository.deleteById(id);
  }

  @Override
  public Institution findById(Long id) {
      return repository.findById(id).orElseThrow(
              () ->  new ResourceNotFoundException("L'institution avec l'id " + id + " n'existe pas")
      );
  }

  @Override
  public InstitutionDTO getOne(Long id) {
      return mapper.asDTO(findById(id));
  }

  @Override
  public List<LiteInstitutionDTO> findAll() {
      return ((List<Institution>) repository.findAll()).stream().map(mapper::asLite).collect(Collectors.toList());
  }

  @Override
  public Page<LiteInstitutionDTO> findAll(Pageable pageable) {
      Page<Institution> entityPage = repository.findAll(pageable);
      List<Institution> entities = entityPage.getContent();
      return new PageImpl<>(mapper.asDTOList(entities), pageable, entityPage.getTotalElements());
  }

  @Override
  public InstitutionDTO update(InstitutionRequestDTO dto, Long id) {
      if(!MyUtils.isValidEmailAddress(dto.getEmail()))
          throw new ResourceNotFoundException("L'email " + dto.getEmail() + " est invalide.");
    Institution exist = findById(id);
    dto.setId(exist.getId());
      return mapper.asDTO(repository.save(mapper.asEntity(dto)));
  }

  @Override
  public InstitutionDTO getCurrentInstitution() {
      Institution institution = repository.findFirstByOrderByName().orElseThrow(
              () ->  new ResourceNotFoundException("Aucune institution n'est enregistrée")
      );
      InstitutionDTO result = mapper.asDTO(institution);
      result.setJourOuvrables(buildJoursOuvrable());
      result.setParametres(buildParametres());
      return result;
  }

    private LiteParametreInstitutionDTO buildParametres() {
        return new LiteParametreInstitutionDTO(parametreRepository.findFirstByOrderById());
    }

    private List<LiteJourOuvrableDTO> buildJoursOuvrable() {
        List<JourOuvrable> jourOuvrables = (List<JourOuvrable>) jourOuvrablesRepository.findAll();
        return jourOuvrables.stream().map(j -> new LiteJourOuvrableDTO(j)).collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return repository.findByName(name).isPresent();
    }

    @Override
    public boolean equalsByDto(InstitutionRequestDTO dto, Long id) {
        Institution ressource = repository.findByName(dto.getName()).orElse(null);
        if (ressource == null) return false;
        return !ressource.getId().equals(id);
    }

    @Override
    public void updateParametres(ParametreInstitutionRequestDTO dto) {
        ParametreInstitution parametres = parametreRepository.findFirstByOrderById();
        log.info("id " + parametres.getId());
        parametres.setBareme(dto.getBareme());
        parametres.setDevise(dto.getDevise());
        parametres.setDureeCours(dto.getDureeCours());
        parametres = parametreRepository.save(parametres);
        updatePlageHoraire(parametres);
    }

    private void updatePlageHoraire(ParametreInstitution parametres) {
        buildPlageHoraires(parametres);
        buildOccupations();
    }

    private void buildOccupations() {
        List<Salle> salles = (List<Salle>) salleRepository.findAll();
        List<PlageHoraire> plages = (List<PlageHoraire>) plageHoraireRepository.findAll();
        for (Salle salle : salles) {
            for (PlageHoraire plage : plages) {
                if (!occupationSalleRepository.findByPlageHoraireAndSalle(plage, salle).isPresent()) {
                    occupationSalleRepository.save(new OccupationSalle(salle.getCode() + "-" + plage.getCode(), false, plage, salle));
                }
            }
        }
    }

    private void buildPlageHoraires(ParametreInstitution parametres) {
        List<PlageHoraire> plages = (List<PlageHoraire>) plageHoraireRepository.findAll();
        for (PlageHoraire plage : plages) {
            List<OccupationSalle> occupations = occupationSalleRepository.findAllByPlageHoraireAndEstOccupee(plage, false);
            if (!occupations.isEmpty()) {
                for (OccupationSalle occupation : occupations) {
                    occupationSalleRepository.delete(occupation);
                }
                plageHoraireRepository.delete(plage);
            } else {
                occupations = occupationSalleRepository.findAllByPlageHoraireAndEstOccupee(plage, true);
                if (occupations.isEmpty()) {
                    plageHoraireRepository.delete(plage);
                }
            }
        }

        // On crée les nouvelles plages horaires
        double dureeCours = parametres.getDureeCours(); // Récupérer la durée du cours en demi-heures
        for (Jour jour : Jour.values()) {
            JourOuvrable ouvrable = jourOuvrablesRepository.findByJour(jour);
            double debutIntervalle = ouvrable.getIntervalle()[0] * 2.0; // Convertir en demi-heures
            double finIntervalle = ouvrable.getIntervalle()[1] * 2.0; // Convertir en demi-heures
            for (double i = debutIntervalle; i < finIntervalle; i += dureeCours * 2) { // Utiliser dureeCours comme pas
                double startTimeValue = i / 2;
                double endTimeValue = (i / 2 + dureeCours);
                String code = jour.getValue().substring(0, 3) + "-" + (int) startTimeValue + "h" + (convertToMinutes(startTimeValue) == 0 ? "" : convertToMinutes(startTimeValue)) + "-" + (int) endTimeValue + "h" + (convertToMinutes(endTimeValue) == 0 ? "" : convertToMinutes(endTimeValue)); // Convertir en heures
                LocalTime startTime = LocalTime.of((int) startTimeValue, convertToMinutes(startTimeValue));
                LocalTime endTime = LocalTime.of((int) endTimeValue, convertToMinutes(endTimeValue));
                Optional<PlageHoraire> result = plageHoraireRepository.findByCode(code);
                if (!result.isPresent()) {
                    PlageHoraire plage = new PlageHoraire();
                    plage.setCode(code);
                    plage.setJour(jour);
                    plage.setStartTime(startTime); // Convertir en heures et minutes
                    plage.setEndTime(endTime); // Convertir en heures et minutes
                    plageHoraireRepository.save(plage);
                }
            }
        }
    }

    private int convertToMinutes(double decimalValue) {
        double minutes = decimalValue % 1 * 60;
        return (int) minutes;
    }
}
