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
    List<StudentBranchDTO> allStudentByBranch;
    List<StateEtudiantBranchDTO> etudiantEchecs;
    List<StateEtudiantBranchDTO> etudiantEnRattrapages;
    List<StateEtudiantBranchDTO> etudiantAdmis;
    List<StateEtudiantBranchDTO> etudiantScolaritePayee;
    List<StateEtudiantBranchDTO> etudiantScolariteImpayee;
    List<StateEtudiantBranchDTO> etudiantInscription;
    private long nbreTotalEtudiant = 0L;
    private long nbreTotalInscription = 0L;
    private long nbreTotalAdmis = 0L;
    private long nbreTotalEnRattrapages = 0L;
    private long nbreTotalEchecs = 0L;
    private long nbreTotalScolaritePayee = 0L;
    private long nbreTotalScolariteImpayee = 0L;
    private long nbreTotalSessionOuverte = 0L;
    private long nbreTotalSessionSurAnnee = 0L;
    private long nbreTotalCampus = 0L;
}
