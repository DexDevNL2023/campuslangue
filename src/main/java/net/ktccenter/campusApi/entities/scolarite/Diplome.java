package net.ktccenter.campusApi.entities.scolarite;

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
@Table(name = "diplomes")
@EqualsAndHashCode(callSuper = false)
@ToString
@Entity
public class Diplome extends BaseAuditingEntity {

	@Column(nullable = false, unique = true)
	private String code;

	@Column(nullable = false, unique = true)
	private String libelle;
}
