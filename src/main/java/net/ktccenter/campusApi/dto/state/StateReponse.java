package net.ktccenter.campusApi.dto.state;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.reponse.branch.EtudiantBranchDTO;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class StateReponse {
  Long nbreAnneeAcademique;
  Long nbreCampus;
  Long nbrePersonnel;
  Long nbreSalle;
  Long nbreUtilisateur;
  Long nbreDeliberation;
  Long nbreEmploiDT;
  Float MoyenGeneral;
  Long nbreProgrammation;
  Long nbreUE;
  Long nbreCompteEtudiant;
  Long nbreDepartement;
  Long nbreEtudiant;
  List<EtudiantBranchDTO> etudiantRedoublants;
  List<EtudiantBranchDTO> etudiantAdmis;
  List<EtudiantBranchDTO> etudiantScolaritePayee;
  Long nbreEtudiantScolariteImpayee;
  Long nbreEtudiantScolariteAvancee;
  Long nbreFaculte;
  Long nbreFiliere;
  Long nbreFormateur;
  Long nbreInscription;
  Long nbreModuleUe;
  Long nbrePaiement;
  Long nbreParent;
  Long nbrePreinscription;
  Long nbreReinscription;
  Long nbreSpecialite;
  Long nbreInscriptionValidee;
}
