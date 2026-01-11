package Business.models.team;

import Business.Employee.Employee;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MedicalTeamDirectory {
    private List<MedicalTeam> teamList;

    public MedicalTeamDirectory() {
        teamList = new ArrayList<>();
    }

    public List<MedicalTeam> getTeamList() {
        return teamList;
    }

    public MedicalTeam createTeam(String teamName, String specialization) {
        MedicalTeam team = new MedicalTeam(teamName, specialization);
        teamList.add(team);
        return team;
    }

    public MedicalTeam addTeam(String teamName, String specialization) {
        MedicalTeam team = new MedicalTeam(teamName, specialization);
        if (teamList == null) {
            teamList = new ArrayList<>();
        }
        teamList.add(team);
        return team;
    }

    public boolean removeTeam(MedicalTeam team) {
        return teamList.remove(team);
    }

    public MedicalTeam findTeamById(String teamId) {
        if (teamId == null || teamId.trim().isEmpty()) return null;
        for (MedicalTeam team : teamList) {
            if (team.getTeamId() != null && team.getTeamId().equals(teamId)) {
                return team;
            }
        }
        return null;
    }

    public MedicalTeam findTeamByName(String name) {
        if (name == null || name.trim().isEmpty()) return null;
        String searchTerm = name.trim();
        for (MedicalTeam team : teamList) {
            if (team.getTeamName() != null && team.getTeamName().equalsIgnoreCase(searchTerm)) {
                return team;
            }
        }
        return null;
    }

    public List<MedicalTeam> findTeamsManagedBy(Employee manager) {
        List<MedicalTeam> managedTeams = new ArrayList<>();
        if (manager == null) return managedTeams;

        for (MedicalTeam team : teamList) {
            if (manager.equals(team.getTeamManager())) {
                 managedTeams.add(team);
            }
        }
        return managedTeams;
    }

    public List<MedicalTeam> findTeamsBySpecialization(String specialization) {
        if (specialization == null || specialization.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String searchTerm = specialization.trim();
        return teamList.stream()
                       .filter(team -> team.getSpecialization() != null && team.getSpecialization().equalsIgnoreCase(searchTerm))
                       .collect(Collectors.toList());
    }
}