package main;

import data.DataLoader;
import data.ProjectRepository;
import model.project.Project;
import model.user.Applicant;
import model.user.HDBOfficer;
import model.user.HDBManager;
import model.user.User;
import ui.MainMenu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    
    /** 
     * @param args
     */
    public static void main(String[] args) {
        List<Applicant> applicants;
        List<HDBOfficer> officers;
        List<Project> projects;
        List<HDBManager> managers;
        List<User> allUsers = new ArrayList<>();

        DataLoader dataLoader = new DataLoader();

        try {
            applicants = dataLoader.loadApplicants("src/data/ApplicantList.csv");
            officers = dataLoader.loadOfficers("src/data/OfficerList.csv");
            managers = dataLoader.loadHDBManagers("src/data/ManagerList.csv");
            projects = DataLoader.loadProjects("src/data/ProjectList.csv", officers, managers);
            ProjectRepository.setAllProjects(projects);
            allUsers.addAll(applicants); 
            allUsers.addAll(officers);
            allUsers.addAll(managers);
            dataLoader.loadApplications("src/data/ApplicationList.csv", allUsers, projects);
            dataLoader.loadEnquiries("src/data/EnquiryList.csv", allUsers, projects);
        } catch (IOException e) {
            System.out.println("Failed to load data files.");
            e.printStackTrace();
            return;
        }

        new MainMenu(allUsers).show();
    }
}
