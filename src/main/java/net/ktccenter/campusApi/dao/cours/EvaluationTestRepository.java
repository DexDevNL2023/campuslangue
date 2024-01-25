package net.ktccenter.campusApi.dao.cours;

import net.ktccenter.campusApi.entities.cours.EvaluationTest;
import net.ktccenter.campusApi.entities.cours.TestModule;
import net.ktccenter.campusApi.entities.scolarite.ModuleFormation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationTestRepository extends PagingAndSortingRepository<EvaluationTest, Long> {
    @Query("SELECT DISTINCT e FROM EvaluationTest e WHERE e.moduleFormation = :moduleFormation")
    List<EvaluationTest> findAllByModuleFormation(ModuleFormation moduleFormation);

    @Query("SELECT DISTINCT e FROM EvaluationTest e WHERE e.testModule = :testModule")
    List<EvaluationTest> findAllByTestModule(TestModule testModule);

    @Query("SELECT DISTINCT e FROM EvaluationTest e WHERE e.testModule.id = :testModuleId AND e.moduleFormation.id = :moduleId")
    List<EvaluationTest> findAllByTestModuleIdAndModuleId(Long testModuleId, Long moduleId);
}
