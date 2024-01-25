package net.ktccenter.campusApi.entities.scolarite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseEntity;
import net.ktccenter.campusApi.entities.administration.User;
import net.ktccenter.campusApi.enums.Sexe;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "apprenants")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class Etudiant extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String matricule;

  //info personnelles
  @Column(nullable = false)
  private String nom;

  private String prenom;

  @Column(length = 256)
  private String imageUrl;

  private String nationalite;

  @Enumerated(EnumType.STRING)
  private Sexe sexe;

  //localisation
  private String adresse;
  private String telephone;
  private String email;

  @Column(nullable = false)
  private String tuteur;

  @Column(nullable = false)
  private String contactTuteur;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Diplome dernierDiplome;

  @OneToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private User user;

}
