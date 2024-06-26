package net.ktccenter.campusApi.dao.cours;

import net.ktccenter.campusApi.entities.cours.PlageHoraire;
import net.ktccenter.campusApi.enums.Jour;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlageHoraireRepository extends PagingAndSortingRepository<PlageHoraire, Long> {
    @Query("SELECT DISTINCT e FROM PlageHoraire e WHERE e.code = :code")
    Optional<PlageHoraire> findByCode(String code);

    List<PlageHoraire> findByJour(Jour jour);

    List<PlageHoraire> findByStartTimeAndEndTime(LocalTime startTime, LocalTime endTime);
}
