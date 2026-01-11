package Business.models.team;

import Business.Employee.Employee;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class MedicalTeam {
    private final String teamId;
    private String teamName;
    private String specialization;
    private List<Employee> teamMembers;
    private Employee teamManager;
    private Map<Integer, List<TimeSlot>> availabilitySchedule;
    private static int count = 1;

    public MedicalTeam(String teamName, String specialization) {
        this.teamId = "TEAM" + count++;
        this.teamName = teamName;
        this.specialization = specialization;
        this.teamMembers = new ArrayList<>();
        this.availabilitySchedule = new HashMap<>();
    }

    public String getTeamId() { return teamId; }
    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public List<Employee> getTeamMembers() { return teamMembers; }
    public Employee getTeamManager() { return teamManager; }
    public void setTeamManager(Employee teamManager) { this.teamManager = teamManager; }

    public void addMember(Employee member) {
        if (member != null && !teamMembers.contains(member)) {
            teamMembers.add(member);
            availabilitySchedule.putIfAbsent(member.getId(), new ArrayList<>());
        }
    }
    public void removeMember(Employee member) {
        if (member != null) {
            teamMembers.remove(member);
            availabilitySchedule.remove(member.getId());
        }
    }

    public void addAvailability(Employee employee, Date start, Date end) {
        if (employee != null && teamMembers.contains(employee)) {
            try {
                TimeSlot newSlot = new TimeSlot(start, end);
                availabilitySchedule.computeIfAbsent(employee.getId(), k -> new ArrayList<>())
                                    .add(newSlot);
            } catch (IllegalArgumentException e) {
                System.err.println("Error adding availability for " + employee.getName() + ": " + e.getMessage());
            }
        }
    }

    public List<TimeSlot> getAvailability(Employee member) {
        if (member == null) {
            return new ArrayList<>();
        }
        return availabilitySchedule.getOrDefault(member.getId(), new ArrayList<>());
    }

    public boolean isTeamAvailable(Date requiredStart, Date requiredEnd, List<Employee> requiredMembers) {
        // Return false only if times are invalid
        if (requiredStart == null || requiredEnd == null || !requiredStart.before(requiredEnd)) {
             return false;
        }

        // If no specific members are required, check if any team member is available
        if (requiredMembers == null || requiredMembers.isEmpty()) {
            // If the team has no members at all, it's not available
            if (teamMembers.isEmpty()) {
                return false;
            }

            // Check if at least one team member is available
            for (Employee member : teamMembers) {
                if (isMemberAvailable(member, requiredStart, requiredEnd)) {
                    return true;
                }
            }
            return false;
        }

        // If specific members are required, check all of them
        for (Employee member : requiredMembers) {
            if (!isMemberAvailable(member, requiredStart, requiredEnd)) {
                return false;
            }
        }
        return true;
    }

    private boolean isMemberAvailable(Employee member, Date requiredStart, Date requiredEnd) {
        List<TimeSlot> slots = getAvailability(member);
        // Debug output
        System.out.println("Checking availability for " + member.getName());
        System.out.println("Required time: " + requiredStart + " to " + requiredEnd);
        System.out.println("Number of availability slots: " + slots.size());

        // 忽略时间，只比较日期部分
        Calendar reqStartCal = Calendar.getInstance();
        reqStartCal.setTime(requiredStart);
        // 设置为当天开始时间
        reqStartCal.set(Calendar.HOUR_OF_DAY, 0);
        reqStartCal.set(Calendar.MINUTE, 0);
        reqStartCal.set(Calendar.SECOND, 0);
        reqStartCal.set(Calendar.MILLISECOND, 0);
        Date reqStartDate = reqStartCal.getTime();

        Calendar reqEndCal = Calendar.getInstance();
        reqEndCal.setTime(requiredEnd);
        // 设置为当天结束时间
        reqEndCal.set(Calendar.HOUR_OF_DAY, 23);
        reqEndCal.set(Calendar.MINUTE, 59);
        reqEndCal.set(Calendar.SECOND, 59);
        reqEndCal.set(Calendar.MILLISECOND, 999);
        Date reqEndDate = reqEndCal.getTime();

        for (TimeSlot availableSlot : slots) {
            System.out.println("Available slot: " + availableSlot.getStartTime() + " to " + availableSlot.getEndTime());

            // 提取时间槽的日期部分
            Calendar availStartCal = Calendar.getInstance();
            availStartCal.setTime(availableSlot.getStartTime());
            availStartCal.set(Calendar.HOUR_OF_DAY, 0);
            availStartCal.set(Calendar.MINUTE, 0);
            availStartCal.set(Calendar.SECOND, 0);
            availStartCal.set(Calendar.MILLISECOND, 0);
            Date availStartDate = availStartCal.getTime();

            Calendar availEndCal = Calendar.getInstance();
            availEndCal.setTime(availableSlot.getEndTime());
            availEndCal.set(Calendar.HOUR_OF_DAY, 23);
            availEndCal.set(Calendar.MINUTE, 59);
            availEndCal.set(Calendar.SECOND, 59);
            availEndCal.set(Calendar.MILLISECOND, 999);
            Date availEndDate = availEndCal.getTime();

            // 只检查日期范围重叠
            if (!availEndDate.before(reqStartDate) && !availStartDate.after(reqEndDate)) {
                System.out.println("MATCH FOUND - Member is available during this time (date comparison)");
                return true;
            }
        }
        System.out.println("No matching availability slot found");
        return false;
    }

    @Override
    public String toString() {
        return teamName + " (" + specialization + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalTeam that = (MedicalTeam) o;
        return Objects.equals(teamId, that.teamId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamId);
    }

    public class TimeSlot {
        private Date startTime;
        private Date endTime;

        public TimeSlot(Date start, Date end) {
            if (start.after(end)) {
                throw new IllegalArgumentException("Start time must be before end time");
            }
            this.startTime = start;
            this.endTime = end;
        }

        // Getters
        public Date getStartTime() {
            return startTime;
        }

        public Date getEndTime() {
            return endTime;
        }

        // 检查时间段是否重叠
        public boolean overlaps(TimeSlot other) {
            return !(endTime.before(other.startTime) || startTime.after(other.endTime));
        }

        // 检查给定时间点是否在时间段内
        public boolean includes(Date time) {
            return (time.equals(startTime) || time.after(startTime)) && 
                   (time.equals(endTime) || time.before(endTime));
        }
    }
}