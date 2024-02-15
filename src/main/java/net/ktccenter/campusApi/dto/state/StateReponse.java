package net.ktccenter.campusApi.dto.state;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.reponse.branch.CampusBranchDTO;
import net.ktccenter.campusApi.dto.reponse.branch.SessionBranchDTO;
import net.ktccenter.campusApi.dto.reponse.branch.StateEtudiantBranchDTO;
import net.ktccenter.campusApi.dto.reponse.branch.StudentBranchDTO;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class StateReponse {
    List<SessionBranchDTO> allSessionOuverte;
    List<SessionBranchDTO> allSessionSurAnnee;
    List<CampusBranchDTO> allCampusByBranch;
    private Long nbreTotalEtudiant = 0L;
    private Long nbreTotalInscription = 0L;
    private Long nbreTotalAdmis = 0L;
    private Long nbreTotalEnRattrapages = 0L;
    private Long nbreTotalEchecs = 0L;
    private Long nbreTotalScolaritePayee = 0L;
    private Long nbreTotalScolariteImpayee = 0L;
    private Long nbreTotalSessionOuverte = 0L;
    private Long nbreTotalSessionSurAnnee = 0L;
    private Long nbreTotalCampus = 0L;
  List<StudentBranchDTO> allStudentByBranch;
  List<StateEtudiantBranchDTO> etudiantEchecs;
  List<StateEtudiantBranchDTO> etudiantEnRattrapages;
  List<StateEtudiantBranchDTO> etudiantAdmis;
  List<StateEtudiantBranchDTO> etudiantScolaritePayee;
  List<StateEtudiantBranchDTO> etudiantScolariteImpayee;
  List<StateEtudiantBranchDTO> etudiantInscription;
}
