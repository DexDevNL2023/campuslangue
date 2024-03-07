package net.ktccenter.campusApi.entities.scolarite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseAuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "niveaux_formation")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class Niveau extends BaseAuditingEntity {

	@Column(nullable = false, unique = true)
	private String code;

	@Column(nullable = false, unique = true)
	private String libelle;

	@DecimalMin(value = "0")
	private BigDecimal fraisInscription;

	@DecimalMin(value = "0")
	private BigDecimal fraisPension;

	@DecimalMin(value = "0")
	private BigDecimal fraisRattrapage;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Diplome diplomeRequis;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Diplome diplomeFinFormation;

	private Float dureeSeance;

	public Niveau(String code, String libelle) {
		super();
		this.code = code;
		this.libelle = libelle;
	}
}
