package net.ktccenter.campusApi.dao.scolarite;

import net.ktccenter.campusApi.entities.administration.Branche;
import net.ktccenter.campusApi.entities.scolarite.Diplome;
import net.ktccenter.campusApi.entities.scolarite.Etudiant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EtudiantRepository extends PagingAndSortingRepository<Etudiant, Long> {
    @Query("SELECT DISTINCT e FROM Etudiant e WHERE e.telephone = :telephone")
    Optional<Etudiant> findByTelephone(String telephone);
    @Query("SELECT DISTINCT e FROM Etudiant e WHERE e.email = :email")
    Optional<Etudiant> findByEmail(String email);
    @Query("SELECT DISTINCT e FROM Etudiant e WHERE e.telephone = :telephone OR e.email = :email")
    Optional<Etudiant> findByTelephoneOrEmail(String telephone, String email);
    @Query("SELECT DISTINCT e FROM Etudiant e WHERE e.matricule = :matricule")
    Optional<Etudiant> findByMatricule(String matricule);
    @Query("SELECT DISTINCT e FROM Etudiant e WHERE e.dernierDiplome = :diplome")
    List<Etudiant> findAllByDiplome(Diplome diplome);

    @Query("SELECT DISTINCT e FROM Etudiant e WHERE e.branche = :branche")
    List<Etudiant> findAllByBranche(Branche branche);
}
