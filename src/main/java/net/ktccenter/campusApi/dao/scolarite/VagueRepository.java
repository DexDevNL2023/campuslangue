package net.ktccenter.campusApi.dao.scolarite;

import net.ktccenter.campusApi.entities.scolarite.Vague;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VagueRepository extends PagingAndSortingRepository<Vague, Long> {
  @Query("SELECT DISTINCT e FROM Vague e WHERE e.code = :code")
  Optional<Vague> findByCode(String code);
}
