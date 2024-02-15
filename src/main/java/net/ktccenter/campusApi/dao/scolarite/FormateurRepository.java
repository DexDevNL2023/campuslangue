package net.ktccenter.campusApi.dao.scolarite;

import net.ktccenter.campusApi.entities.administration.Branche;
import net.ktccenter.campusApi.entities.administration.User;
import net.ktccenter.campusApi.entities.scolarite.Diplome;
import net.ktccenter.campusApi.entities.scolarite.Formateur;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormateurRepository extends PagingAndSortingRepository<Formateur, Long> {
  @Query("SELECT DISTINCT e FROM Formateur e WHERE e.telephone = :telephone")
  Optional<Formateur> findByTelephone(String telephone);
  @Query("SELECT DISTINCT e FROM Formateur e WHERE e.email = :email")
  Optional<Formateur> findByEmail(String email);
  @Query("SELECT DISTINCT e FROM Formateur e WHERE e.telephone = :telephone OR e.email = :email")
  Optional<Formateur> findByTelephoneOrEmail(String telephone, String email);
  @Query("SELECT DISTINCT f FROM Formateur f WHERE f.isDefault = :isDefault")
  Optional<Formateur> findByIsDefault(Boolean isDefault);
  @Query("SELECT DISTINCT f FROM Formateur f WHERE f.matricule = :matricule")
  Optional<Formateur> findByMatricule(String matricule);
  @Query("SELECT DISTINCT e FROM Formateur e WHERE e.diplome = :diplome")
  List<Formateur> findAllByDiplome(Diplome diplome);

  @Query("SELECT DISTINCT e FROM Formateur e WHERE e.user = :user")
  Formateur findByUser(User user);

  @Query("SELECT DISTINCT e FROM Formateur e WHERE e.branche = :branche")
  List<Formateur> findAllByBranche(Branche branche);
}
