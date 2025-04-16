package main;

import data.DataLoader;
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
    public static void main(String[] args) {
        List<Applicant> applicants;
        List<HDBOfficer> officers;
        List<Project> projects;
        List<HDBManager> managers;
        List<User> allUsers = new ArrayList<>();

        try {
            applicants = new DataLoader().loadApplicants("src/data/ApplicantList.csv");
            officers = new DataLoader().loadOfficers("src/data/OfficerList.csv");
            managers = new DataLoader().loadHDBManagers("src/data/ManagerList.csv");
            projects = DataLoader.loadProjects("src/data/ProjectList.csv", officers, managers);
            allUsers.addAll(applicants); 
            allUsers.addAll(officers);
            allUsers.addAll(managers);
        } catch (IOException e) {
            System.out.println("Failed to load data files.");
            e.printStackTrace();
            return;
        }

        new MainMenu().show(allUsers, projects);
    }
}
