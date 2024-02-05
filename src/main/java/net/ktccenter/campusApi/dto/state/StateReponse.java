package net.ktccenter.campusApi.dto.state;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ktccenter.campusApi.dto.reponse.branch.StateEtudiantBranchDTO;
import net.ktccenter.campusApi.dto.reponse.branch.StudentBranchDTO;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class StateReponse {
  List<StudentBranchDTO> allStudentByBranch;
  List<StateEtudiantBranchDTO> etudiantEchecs;
  List<StateEtudiantBranchDTO> etudiantEnRattrapages;
  List<StateEtudiantBranchDTO> etudiantAdmis;
  List<StateEtudiantBranchDTO> etudiantScolaritePayee;
  List<StateEtudiantBranchDTO> etudiantScolariteImpayee;
  List<StateEtudiantBranchDTO> etudiantInscription;
}
