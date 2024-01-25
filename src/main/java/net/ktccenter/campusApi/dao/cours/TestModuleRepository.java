package net.ktccenter.campusApi.dao.cours;

import net.ktccenter.campusApi.entities.cours.TestModule;
import net.ktccenter.campusApi.entities.scolarite.Inscription;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestModuleRepository extends PagingAndSortingRepository<TestModule, Long> {
  @Query("SELECT DISTINCT e FROM TestModule e WHERE e.code = :code")
  Optional<TestModule> findByCode(String code);

  @Query("SELECT DISTINCT e FROM TestModule e WHERE e.inscription = :inscription")
  TestModule findByInscription(Inscription inscription);

  @Query("SELECT DISTINCT e FROM TestModule e WHERE e.inscription.session.id = :sessionId")
  List<TestModule> findAllBySessionId(Long sessionId);
}
