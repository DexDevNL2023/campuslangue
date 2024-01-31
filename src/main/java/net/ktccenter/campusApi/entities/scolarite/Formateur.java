package net.ktccenter.campusApi.entities.scolarite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseEntity;
import net.ktccenter.campusApi.entities.administration.Branche;
import net.ktccenter.campusApi.entities.administration.User;
import net.ktccenter.campusApi.enums.Sexe;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "formateurs")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class Formateur extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String matricule;

  //info personnelles
  @Column(nullable = false, unique = true)
  private String nom;

  private String prenom;

  @Column(length = 256)
  private String imageUrl;

  @Enumerated(EnumType.STRING)
  private Sexe sexe;

  //localisation
  private String adresse;
  private String telephone;
  private String email;
  private Integer experience;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private Diplome diplome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Branche branche;

  @OneToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  private User user;
}
