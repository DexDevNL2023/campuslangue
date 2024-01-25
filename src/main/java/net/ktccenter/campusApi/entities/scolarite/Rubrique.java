package net.ktccenter.campusApi.entities.scolarite;

import lombok.*;
import lombok.experimental.Accessors;
import net.ktccenter.campusApi.entities.BaseAuditingEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "rubriques_paiement")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class Rubrique extends BaseAuditingEntity {

	@Column(nullable = false, unique = true)
	private String code;

	@Column(nullable = false, unique = true)
	private String libelle;

	@DecimalMin(value = "0")
	private BigDecimal frais;

	private Double reduction;
}
