package Business;

import Business.Employee.Employee;
import Business.Enterprise.Enterprise;
import Business.Enterprise.HospitalEnterprise;
import Business.Enterprise.SupplyEnterprise;
import Business.Network.Network;
import Business.Organization.DoctorOrganization;
import Business.Organization.Organization;
import Business.Organization.SupplierOrganization;
import Business.Role.AdminRole;
import Business.Role.DeviceManagerRole;
import Business.Role.DoctorRole;
import Business.Role.LogisticsManagerRole;
import Business.Role.OperationsManagerRole;
import Business.Role.PHSAdminRole;
import Business.Role.SupplierRole;
import Business.Role.MaintainerRole;

import Business.Role.SystemAdminRole;
import Business.UserAccount.UserAccount;
import Business.models.device.Device;
import Business.models.device.DeviceDirectory;
import Business.models.room.Room;
import Business.models.room.RoomDirectory;
import Business.models.team.MedicalTeam;
import Business.models.team.MedicalTeamDirectory;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JOptionPane;

/**
 *
 * @author Di
 */
public class ConfigureASystem {
    
    public static EcoSystem configure(){
        
        EcoSystem system = EcoSystem.getInstance();
        
        //Create a network
        String name = "PHS network";
        Network network = system.createAndAddNetwork();
        network.setName(name);
        

        //create an enterprise 
        Enterprise.EnterpriseType phsType = Enterprise.EnterpriseType.PHS;
        Enterprise.EnterpriseType hospitalType = Enterprise.EnterpriseType.Hospital;
        Enterprise.EnterpriseType supplyType = Enterprise.EnterpriseType.Supply; 
        Enterprise.EnterpriseType logisticsType = Enterprise.EnterpriseType.Logistics;
  
        
        String phsName = "Partners HealthCare System";
        System.out.println("Before phsEnterprise creation: network.getEnterpriseDirectory() = " + network.getEnterpriseDirectory());
        Enterprise phsEnterprise = network.getEnterpriseDirectory().createAndAddEnterprise(phsName, Enterprise.EnterpriseType.PHS);
        System.out.println("After phsEnterprise creation: phsEnterprise = " + phsEnterprise);
        
        String supplyEnterpriseName = "Medical Device Supply";
        Enterprise supplyEnterprise = network.getEnterpriseDirectory().createAndAddEnterprise(supplyEnterpriseName, supplyType);
      
         
        String logisticsEnterpriseName = "Medical Device Logistics";
        Enterprise logisticsEnterprise = network.getEnterpriseDirectory().createAndAddEnterprise(logisticsEnterpriseName, logisticsType);
      
        String primaryHospitalName = "Massachusetts General Hospital";
        Enterprise primaryEnterprise = network.getEnterpriseDirectory().createAndAddEnterprise(primaryHospitalName, hospitalType);

        String secondaryHospitalName = "Western Massachusetts Hospital";
        Enterprise secondaryEnterprise = network.getEnterpriseDirectory().createAndAddEnterprise(secondaryHospitalName, hospitalType);
        
     
        //initialize some organizations
        Organization.Type phsAdminType = Organization.Type.Admin;
        Organization.Type adminType = Organization.Type.Admin;
        Organization.Type doctorType = Organization.Type.Doctor;
        Organization.Type deviceType = Organization.Type.DeviceManagement;
        Organization.Type operationsType = Organization.Type.OperationsManagement;
        Organization.Type supplierType = Organization.Type.Supplier;
        Organization.Type maintenanceType = Organization.Type.Maintenance;
        Organization.Type logisticType = Organization.Type.Logistics;

       
        
        // Create organizations
       
//        Organization primaryDoctorOrganization = primaryEnterprise.getOrganizationDirectory().createOrganization(Organization.Type.Doctor);
//        Organization primaryDeviceOrganization = primaryEnterprise.getOrganizationDirectory().createOrganization(Organization.Type.DeviceManagement);
//        Organization primaryOperationsOrganization = primaryEnterprise.getOrganizationDirectory().createOrganization(Organization.Type.OperationsManagement);
//        
//        Organization secondaryDoctorOrganization = secondaryEnterprise.getOrganizationDirectory().createOrganization(Organization.Type.Doctor);
//        Organization secondaryDeviceOrganization = secondaryEnterprise.getOrganizationDirectory().createOrganization(Organization.Type.DeviceManagement);
//        Organization secondaryOperationsOrganization = secondaryEnterprise.getOrganizationDirectory().createOrganization(Organization.Type.OperationsManagement);
    
        Organization phsAdminOrganization = phsEnterprise.getOrganizationDirectory().createOrganization(Organization.Type.PHSAdmin);
        Organization primaryDoctorOrganization = primaryEnterprise.getOrganizationDirectory().createOrganization(doctorType);
        Organization primaryOperationsOrganization = primaryEnterprise.getOrganizationDirectory().createOrganization(operationsType);
        Organization primaryDeviceOrganization = primaryEnterprise.getOrganizationDirectory().createOrganization(deviceType);
        Organization secondaryDoctorOrganization = secondaryEnterprise.getOrganizationDirectory().createOrganization(doctorType);
        Organization secondaryDeviceOrganization = secondaryEnterprise.getOrganizationDirectory().createOrganization(deviceType);
        Organization secondaryOperationsOrganization = secondaryEnterprise.getOrganizationDirectory().createOrganization(operationsType);
        Organization supplierOrganization1 = supplyEnterprise.getOrganizationDirectory().createOrganization(supplierType);
        Organization supplierOrganization2 = supplyEnterprise.getOrganizationDirectory().createOrganization(supplierType);
        Organization logisticsOrganization1 = logisticsEnterprise.getOrganizationDirectory().createOrganization(logisticType);
        Organization logisticsOrganization2 = logisticsEnterprise.getOrganizationDirectory().createOrganization(logisticType);
        Organization primaryMaintenanceOrganization = primaryEnterprise.getOrganizationDirectory().createOrganization(maintenanceType);

        
        if(primaryMaintenanceOrganization == null) {
            throw new RuntimeException("Failed to create maintenance organization. Check organization type mappings.");
        }
        
        //have some employees
        Employee employee = system.getEmployeeDirectory().createEmployee("sysadmin");
        Employee phsAdmin = phsEnterprise.getEmployeeDirectory().createEmployee("phsadmin");
        Employee employeeAdmin1 = primaryEnterprise.getEmployeeDirectory().createEmployee("admin1");
        Employee employeeAdmin2 = secondaryEnterprise.getEmployeeDirectory().createEmployee("admin2");
        Employee doctor1 = primaryDoctorOrganization.getEmployeeDirectory().createEmployee("doctor1");
        Employee deviceManager1 = primaryDeviceOrganization.getEmployeeDirectory().createEmployee("dm1");
        Employee deviceManager2 = secondaryDeviceOrganization.getEmployeeDirectory().createEmployee("dm2");
        Employee operationsManager1 = primaryOperationsOrganization.getEmployeeDirectory().createEmployee("om1");
        Employee operationsManager2 = secondaryOperationsOrganization.getEmployeeDirectory().createEmployee("om2");
        Employee teamManager1 = primaryDoctorOrganization.getEmployeeDirectory().createEmployee("tm1");
        Employee teamManager2 = primaryDoctorOrganization.getEmployeeDirectory().createEmployee("tm2");
        Employee supplierEmployee1 = supplierOrganization1.getEmployeeDirectory().createEmployee("philips");
        Employee supplierEmployee2 = supplierOrganization2.getEmployeeDirectory().createEmployee("siemens");
        Employee UPSManager = logisticsOrganization1.getEmployeeDirectory().createEmployee("Penny");
        Employee FedexManager = logisticsOrganization2.getEmployeeDirectory().createEmployee("Paul");
        Employee maintaineneceManager = primaryMaintenanceOrganization.getEmployeeDirectory().createEmployee("Tod");
        
        
        primaryDeviceOrganization.setName("MGH Device Management");
        supplierOrganization1.setName("Philips");
        supplierOrganization2.setName("Siemens");
        logisticsOrganization1.setName("UPS HealthCare");
        logisticsOrganization2.setName("FedEx HealthCare Solutions");

        //create user account
        UserAccount ua = system.getUserAccountDirectory().createUserAccount("sysadmin", "sysadmin", employee, new SystemAdminRole());
        UserAccount pua = phsAdminOrganization.getUserAccountDirectory().createUserAccount("phs", "phs", phsAdmin, new PHSAdminRole());
        UserAccount uad1 = primaryEnterprise.getUserAccountDirectory().createUserAccount("admin1", "admin1", employeeAdmin1, new AdminRole());
        UserAccount uad2 = secondaryEnterprise.getUserAccountDirectory().createUserAccount("admin2", "admin2", employeeAdmin2, new AdminRole());
        UserAccount ud = primaryDoctorOrganization.getUserAccountDirectory().createUserAccount("doctor1", "doctor1", doctor1, new DoctorRole());
        UserAccount udv1 = primaryDeviceOrganization.getUserAccountDirectory().createUserAccount("dm1", "dm1", deviceManager1, new DeviceManagerRole());         
        UserAccount uo1 = primaryOperationsOrganization.getUserAccountDirectory().createUserAccount("om1", "om1", operationsManager1, new OperationsManagerRole());
        UserAccount udv2 = secondaryDeviceOrganization.getUserAccountDirectory().createUserAccount("dm2", "dm2", deviceManager2, new DeviceManagerRole());         
        UserAccount uo2 = secondaryOperationsOrganization.getUserAccountDirectory().createUserAccount("om2", "om2", operationsManager2, new OperationsManagerRole());
        UserAccount utm1 = primaryDoctorOrganization.getUserAccountDirectory().createUserAccount("tm1", "tm1", teamManager1, new Business.Role.MedicalTeamManagerRole());
        UserAccount utm2 = primaryDoctorOrganization.getUserAccountDirectory().createUserAccount("tm2", "tm2", teamManager2, new Business.Role.MedicalTeamManagerRole());
        UserAccount ua3 = supplierOrganization1.getUserAccountDirectory().createUserAccount("philips","philips",supplierEmployee1, new SupplierRole());
        // UserAccount ua4 =
        // supplierOrganization.getUserAccountDirectory().createUserAccount("intrinsic
        // surgical",
        // "123", supplierEmployee2, new SupplierRole());
        UserAccount ua5 = supplierOrganization2.getUserAccountDirectory().createUserAccount("siemens","siemens",supplierEmployee2, new SupplierRole());
        UserAccount ua6 = logisticsOrganization1.getUserAccountDirectory().createUserAccount("penny", "penny",UPSManager, new LogisticsManagerRole());
        UserAccount ua7 = logisticsOrganization2.getUserAccountDirectory().createUserAccount("paul", "paul",UPSManager, new LogisticsManagerRole());                        
//        UserAccount ua8 = maintenanceOrganization.getUserAccountDirectory().createUserAccount("priscilla","priscilla", maintenanceManager, new MaintenanceManagerRole());
        UserAccount ut = primaryMaintenanceOrganization.getUserAccountDirectory().createUserAccount("tod", "tod", maintaineneceManager, new  MaintainerRole());
//        
        if (primaryDoctorOrganization instanceof DoctorOrganization) {
            DoctorOrganization doctorOrg = (DoctorOrganization) primaryDoctorOrganization;

            MedicalTeamDirectory teamDir = doctorOrg.getMedicalTeamDirectory();

            MedicalTeam cardioTeam = teamDir.addTeam("Cardiology Team", "Cardiology");
            cardioTeam.setTeamManager(teamManager1);

            MedicalTeam nursingTeam = teamDir.addTeam("Nursing Team", "Nursing Support");
            nursingTeam.setTeamManager(teamManager2);

            System.out.println(">> Created medical teams and assigned to manager: " + teamManager1.getName());
            System.out.println(">> Created medical teams and assigned to manager: " + teamManager2.getName());

            Date now = new Date();

            cardioTeam.addMember(teamManager1);
            nursingTeam.addMember(teamManager2);

            for (int i = 0; i < 30; i++) {
                Calendar startCal = Calendar.getInstance();
                startCal.setTime(now);
                startCal.add(Calendar.DAY_OF_MONTH, i);
                startCal.set(Calendar.HOUR_OF_DAY, 9);
                startCal.set(Calendar.MINUTE, 0);
                startCal.set(Calendar.SECOND, 0);

                Calendar endCal = Calendar.getInstance();
                endCal.setTime(now);
                endCal.add(Calendar.DAY_OF_MONTH, i);
                endCal.set(Calendar.HOUR_OF_DAY, 17);
                endCal.set(Calendar.MINUTE, 0);
                endCal.set(Calendar.SECOND, 0);

                Date dayStart = startCal.getTime();
                Date dayEnd = endCal.getTime();

                for (Employee member : cardioTeam.getTeamMembers()) {
                    cardioTeam.addAvailability(member, dayStart, dayEnd);
                }

                for (Employee member : nursingTeam.getTeamMembers()) {
                    nursingTeam.addAvailability(member, dayStart, dayEnd);
                }
            }

            if (cardioTeam.getTeamMembers().isEmpty()) {
                cardioTeam.addMember(teamManager1);

                Date startTime = new Date();

                Calendar endCal = new GregorianCalendar(2026, Calendar.DECEMBER, 31, 23, 59);
                Date endTime = endCal.getTime();

                cardioTeam.addAvailability(teamManager1, startTime, endTime);
                System.out.println("Added extended availability for cardioTeam from " + startTime + " to " + endTime);
            }

            if (nursingTeam.getTeamMembers().isEmpty()) {
                nursingTeam.addMember(teamManager2);

                Date startTime = new Date();

                Calendar endCal = new GregorianCalendar(2026, Calendar.DECEMBER, 31, 23, 59);
                Date endTime = endCal.getTime();

                nursingTeam.addAvailability(teamManager2, startTime, endTime);
                System.out.println("Added extended availability for nursingTeam from " + startTime + " to " + endTime);
            }

            System.out.println(">> Added availability schedules for all teams");
        }
        
        RoomDirectory primaryRooms = ((HospitalEnterprise) primaryEnterprise).getRoomDirectory();
        
        Room room101 = primaryRooms.addNewRoom();
        room101.setNumber(101);
        room101.setFloor("1st Floor");
        room101.setAvailable(true);
        room101.setRoomType("General OR");
        
        Room room102 = primaryRooms.addNewRoom();
        room102.setNumber(102);
        room102.setFloor("1st Floor");
        room102.setAvailable(true);
        room102.setRoomType("General OR");
        
        Room room103 = primaryRooms.addNewRoom();
        room103.setNumber(103);
        room103.setFloor("1st Floor");
        room103.setAvailable(true);
        room103.setRoomType("General OR");
        
        Room room201 = primaryRooms.addNewRoom();
        room201.setNumber(201);
        room201.setFloor("2nd Floor");
        room201.setAvailable(true);
        room201.setRoomType("Imaging Room");
        
        Room room202 = primaryRooms.addNewRoom();
        room202.setNumber(202);
        room202.setFloor("2nd Floor");
        room202.setAvailable(true);
        room202.setRoomType("Imaging Room");
        
        Room room301 = primaryRooms.addNewRoom();
        room301.setNumber(301);
        room301.setFloor("3rd Floor");
        room301.setAvailable(true);
        room301.setRoomType("Robotic OR");
        
        Room room302 = primaryRooms.addNewRoom();
        room302.setNumber(302);
        room302.setFloor("3rd Floor");
        room302.setAvailable(true);
        room302.setRoomType("Robotic OR");

        Room room401 = primaryRooms.addNewRoom();
        room401.setNumber(401);
        room401.setFloor("4th Floor");
        room401.setAvailable(true);
        room401.setRoomType("General OR");
        
        DeviceDirectory primaryDevices = ((HospitalEnterprise) primaryEnterprise).getDeviceDirectory();
        
        Device xray = primaryDevices.addNewDevice();
        xray.setDeviceID("DEV001");
        xray.setName("X-Ray Machine");
        xray.setType("Imaging");
        xray.setManufacturer("Siemens");
        xray.setAcquisitionDate("2024-01-15");
        xray.setLastMaintenanceDate("2024-03-01");
        xray.setCurrentLocation(room201);
        xray.setPortable(false);
        xray.setQuantity(1);
        xray.setOwningHospital((HospitalEnterprise)primaryEnterprise);
        xray.setDeviceStatus("Available");
        xray.setAvailable(true);
        xray.setYearofManufacturing(2002);
        xray.setPrice(250000.00);

             
        Device xraynew = primaryDevices.addNewDevice();

        xraynew.setName("X-Ray Machine");
        xraynew.setType("Imaging");
        xraynew.setManufacturer("Philips");
        xraynew.setAcquisitionDate("2024-01-15");
        xraynew.setLastMaintenanceDate("2024-03-01");
        xraynew.setCurrentLocation(room201);
        xraynew.setPortable(false);
        xraynew.setAvailable(true);
        xraynew.setYearofManufacturing(2022);
        xraynew.setPrice(300000.00);
        
        
        Device ultrasound = primaryDevices.addNewDevice();
//        ultrasound.setDeviceID("DEV002");
        ultrasound.setName("Ultrasound Scanner");
        ultrasound.setType("Imaging");
        ultrasound.setManufacturer("Philips");
        ultrasound.setAcquisitionDate("2023-11-20");
        ultrasound.setLastMaintenanceDate("2024-02-15");
        ultrasound.setCurrentLocation(room202);
        ultrasound.setPortable(true);
        ultrasound.setQuantity(1);
        ultrasound.setOwningHospital((HospitalEnterprise)primaryEnterprise);
        ultrasound.setDeviceStatus("Available");
        ultrasound.setYearofManufacturing(2002);
        ultrasound.setPrice(100000.00);


         Device newultrasound = primaryDevices.addNewDevice();
//                newultrasound.setDeviceID("DEV002-2");
        newultrasound.setName("Ultrasound Scanner");
        newultrasound.setType("Imaging");
        newultrasound.setManufacturer("Philips");
        newultrasound.setAcquisitionDate("2023-11-20");
        newultrasound.setLastMaintenanceDate("2024-02-15");
        newultrasound.setCurrentLocation(room201);
        newultrasound.setPortable(true);
        newultrasound.setAvailable(true);
        newultrasound.setYearofManufacturing(2022);
        newultrasound.setPrice(2500000.00);
        
                        
        Device MRI = primaryDevices.addNewDevice();
//                MRI.setDeviceID("DEV003");
        MRI.setName("MRI Machine");
        MRI.setType("Imaging");
        MRI.setManufacturer("Philips");
        MRI.setPortable(true);
        MRI.setAvailable(false);
        MRI.setYearofManufacturing(2015);
        MRI.setPrice(500000.00);

        Device newMRI = primaryDevices.addNewDevice();
//                newMRI.setDeviceID("DEV003");
        newMRI.setName("MRI Machine");
        newMRI.setType("Imaging");
        newMRI.setManufacturer("Philips");
        newMRI.setPortable(true);
        newMRI.setAvailable(false);
        newMRI.setYearofManufacturing(2023);
        newMRI.setPrice(600000.00);
        
        
        if (primaryEnterprise instanceof HospitalEnterprise) {
            primaryDevices = ((HospitalEnterprise) primaryEnterprise).getDeviceDirectory();
        }
        DeviceDirectory secondaryDevices = null;
        if (secondaryEnterprise instanceof HospitalEnterprise) {
            secondaryDevices = ((HospitalEnterprise) secondaryEnterprise).getDeviceDirectory();
        }

        if (primaryDevices == null || secondaryDevices == null) {
            System.err.println("Error: Could not get device directories for both hospitals in ConfigureASystem.");
        } else {
            String daVinciParentId = "DAVINCI_SYS_01";

            Device console = primaryDevices.addNewDevice();
            console.setDeviceID("DV-CON-01");
            console.setName("DaVinci Console");
            console.setType("Robotic Component");
            console.setManufacturer("Intuitive Surgical");
            console.setAcquisitionDate("2024-01-10");
            console.setLastMaintenanceDate("2024-03-05");
            console.setCurrentLocation(room301);
            console.setPortable(false);
            console.setQuantity(1);
            console.setOwningHospital((HospitalEnterprise) primaryEnterprise);
            console.setDeviceStatus("Available");
            console.setComponent(true);
            console.setParentDeviceID(daVinciParentId);
            console.setComponentType("Console");
            console.setRentable(false);
            System.out.println(">> ConfigureASystem: Added Device Component: " + console.getName() + " to " + primaryEnterprise.getName());

            Device vision = primaryDevices.addNewDevice();
            vision.setDeviceID("DV-VIS-01");
            vision.setName("DaVinci Vision Cart");
            vision.setType("Robotic Component");
            vision.setManufacturer("Intuitive Surgical");
            vision.setAcquisitionDate("2024-01-10");
            vision.setLastMaintenanceDate("2024-03-05");
            vision.setCurrentLocation(room301);
            vision.setPortable(true);
            vision.setQuantity(1);
            vision.setOwningHospital((HospitalEnterprise) primaryEnterprise);
            vision.setDeviceStatus("Available");
            vision.setComponent(true);
            vision.setParentDeviceID(daVinciParentId);
            vision.setComponentType("VisionCart");
            vision.setRentable(false);
            System.out.println(">> ConfigureASystem: Added Device Component: " + vision.getName() + " to " + primaryEnterprise.getName());

            Device arms = secondaryDevices.addNewDevice();
            arms.setDeviceID("DV-ARM-01");
            arms.setName("DaVinci Arms Set");
            arms.setType("Robotic Component");
            arms.setManufacturer("Intuitive Surgical");
            arms.setAcquisitionDate("2023-12-01");
            arms.setLastMaintenanceDate("2024-02-20");
            arms.setCurrentLocation(null);
            arms.setPortable(true);
            arms.setQuantity(1);
            arms.setOwningHospital((HospitalEnterprise) secondaryEnterprise);
            arms.setDeviceStatus("Available");
            arms.setComponent(true);
            arms.setParentDeviceID(daVinciParentId);
            arms.setComponentType("ArmsSet");
            arms.setRentable(true);
            System.out.println(">> ConfigureASystem: Added Device Component: " + arms.getName() + " to " + secondaryEnterprise.getName());
        
        RoomDirectory secondaryRooms = ((HospitalEnterprise) secondaryEnterprise).getRoomDirectory();
        
        Room room101s = secondaryRooms.addNewRoom();
        room101s.setNumber(101);
        room101s.setFloor("1st Floor");
        room101s.setAvailable(true);
        room101s.setRoomType("General OR");

        Room room201s = secondaryRooms.addNewRoom();
        room201s.setNumber(201);
        room201s.setFloor("2nd Floor");
        room201s.setAvailable(true);
        room201s.setRoomType("Imaging Room");

        Room room301s = secondaryRooms.addNewRoom();
        room301s.setNumber(301);
        room301s.setFloor("3rd Floor");
        room301s.setAvailable(true);
        room301s.setRoomType("Robotic OR");
        
        Room room303 = secondaryRooms.addNewRoom();
        room303.setNumber(303);
        room303.setFloor("3rd Floor");
        room303.setAvailable(false);
        
        Device mri = secondaryDevices.addNewDevice();
        mri.setDeviceID("DEV004");
        mri.setName("MRI Scanner");
        mri.setType("Imaging");
        mri.setManufacturer("Siemens");
        mri.setAcquisitionDate("2023-12-01");
        mri.setLastMaintenanceDate("2024-02-28");
        mri.setCurrentLocation(room303);
        mri.setPortable(false);
        mri.setQuantity(1);
        mri.setOwningHospital((HospitalEnterprise)secondaryEnterprise);
        mri.setDeviceStatus("Available");
        
         SupplierOrganization supplierOrg1 = (SupplierOrganization) supplierOrganization1;
                supplierOrg1.setCertified(true);
                DeviceDirectory directory1 = supplierOrg1.getDeviceDirectory();

                Device philipsxray = directory1.addNewDevice();

//                philipsxray.setDeviceID("PhilipsDEV1");
                philipsxray.setName("X-Ray Machine");
                philipsxray.setType("Imaging");
                philipsxray.setManufacturer("Philips");
                philipsxray.setPortable(true);
                // philipsxray.setAvail(10);
                philipsxray.setPrice(250000.00);


                Device philipsMRI1 = directory1 .addNewDevice();
                
                philipsMRI1.setName("MRI");
                philipsMRI1.setType("Imaging");
                philipsMRI1.setManufacturer("Philips");
                philipsMRI1.setPortable(true);
                philipsMRI1.setPrice(550000.00);


                Device philipsMRI2 = directory1 .addNewDevice();
                philipsMRI2.setName("MRI");
                philipsMRI2.setType("Imaging");
                philipsMRI2.setManufacturer("Philips");
                philipsMRI2.setPortable(true);
                philipsMRI2.setPrice(450000.00);

                SupplierOrganization supplierOrg2 = (SupplierOrganization) supplierOrganization2;
                DeviceDirectory directory2 = supplierOrg2.getDeviceDirectory();
                supplierOrg2.setCertified(true);

                // DeviceDirectory directory = erOrg.getDeviceDirectory();

                Device philipsultrasound = directory2.addNewDevice();

                // philipsultrasound.setDeviceID("philipsDEV002");

                philipsultrasound.setName("Ultrasound Scanner");
                philipsultrasound.setType("Imaging");
                philipsultrasound.setManufacturer("Philips");
                philipsultrasound.setPortable(true);
                philipsultrasound.setPrice(10000.00);

                Device siemensmri = directory2.addNewDevice();

                siemensmri.setName("MRI Scanner");
                siemensmri.setType("Imaging");
                siemensmri.setManufacturer("Siemens");
                siemensmri.setPortable(false);
                siemensmri.setPrice(50000.00);
                 
                newultrasound = directory2.addNewDevice();
                newultrasound.setName("Ultrasound Scanner");
                newultrasound.setType("Imaging");
                newultrasound.setManufacturer("Siemens");
                newultrasound.setPortable(true);
                newultrasound.setAvailable(true);
                newultrasound.setYearofManufacturing(2022);
                newultrasound.setPrice(2500000.00);
                
               
                Device pxray =  directory2.addNewDevice();
                pxray.setName("X-Ray Machine");
                pxray.setType("Imaging");
                pxray.setManufacturer("Siemens");
                pxray.setPortable(false);
                pxray.setAvailable(true);
                pxray.setYearofManufacturing(2022);
                pxray.setPrice(350000.00);
                
        
       
        
      
//        Organization.Type supplierType = Organization.Type.Supplier;
//        Organization.Type maintenanceType = Organization.Type.Maintenance;
////        Organization.Type logisticsType = Organization.Type.Logistics;
//        
//        
//        
//           
//        Organization adminOrganization = supplyEnterprise.getOrganizationDirectory().createOrganization(adminType);
//        Organization supplierOrganization = supplyEnterprise.getOrganizationDirectory().createOrganization(supplierType);
//        Organization maintenanceOrganization = supplyEnterprise.getOrganizationDirectory().createOrganization(maintenanceType);
////        Organization logisticsOrganization = logisticsEnterprise.getOrganizationDirectory().createOrganization(logisticsType);
//        
//    
//         Employee supplyemployeeAdmin = supplyEnterprise.getEmployeeDirectory().createEmployee("admin2");
//         Employee supplychainManager = supplierOrganization.getEmployeeDirectory().createEmployee("Louis");
//         Employee supplier1 = supplierOrganization.getEmployeeDirectory().createEmployee("GE Healthcare");
//         Employee supplier2 = supplierOrganization.getEmployeeDirectory().createEmployee("Intrinsic Surgical");
//         Employee maintenanceManager = maintenanceOrganization.getEmployeeDirectory().createEmployee("Priscilla");
////         Employee logisticsManager = logisticsOrganization.getEmployeeDirectory().createEmployee("Penny");
//         
//        UserAccount ua1 = supplyEnterprise.getUserAccountDirectory().createUserAccount("admin2", "admin2", supplyemployeeAdmin, new AdminRole());
//        UserAccount ua2 = supplierOrganization.getUserAccountDirectory().createUserAccount("Louis", "Louis", supplychainManager, new SupplyChainManagerRole());
//        UserAccount ua3 = supplierOrganization.getUserAccountDirectory().createUserAccount("GE Healthcare", "GE HealthCare",supplier1, new SupplierRole());
//        UserAccount ua4 = supplierOrganization.getUserAccountDirectory().createUserAccount("Intrinsic Surgical", "Intrinsic Surgical",supplier2, new SupplierRole());
//        UserAccount ua5 = logisticsOrganization.getUserAccountDirectory().createUserAccount("james", "james", logisticsManager, new LogisticsManagerRole());         
        
        return system;
        
        
    
    }       
        return null;

    }
}
   
    

