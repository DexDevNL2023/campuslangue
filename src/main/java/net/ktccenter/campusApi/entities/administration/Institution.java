package net.ktccenter.campusApi.entities.administration;

import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseAuditingEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "institutions")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class Institution extends BaseAuditingEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String sigle;

    private Boolean estFonctionnel;
    private Integer anneeOuverture;
    private Boolean estAgree;
    private Boolean estLocataire;
    private Boolean disposeConventionEtat;
    private Integer nombreSitesOccupes;
    private Boolean estTerrainTitre;
    private Double superficie;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String telephone;

    private String site;
    private String telephone2;
    private String adresse;
    private String ville;
    private String bp;
    private Float latitude;
    private Float longitude;
    private String logo;

    private String enteteGauche;
    private String enteteDroite;
    private String piedPage;
}
