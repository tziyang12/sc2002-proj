package data;

import model.project.Project;
import model.project.FlatType;
import model.user.Applicant;
import model.user.enums.MaritalStatus;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DataLoader {

    // Load applicants from ApplicantList.csv
    public List<Applicant> loadApplicants(String filePath) {
        List<Applicant> applicants = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip the header
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\t");
                String name = data[0];
                String nric = data[1];
                int age = Integer.parseInt(data[2]);
                MaritalStatus maritalStatus = MaritalStatus.valueOf(data[3].toUpperCase());
                String password = data[4];

                // Create and add the applicant
                applicants.add(new Applicant(nric, password, age, maritalStatus));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return applicants;
    }

    // Load projects from ProjectList.csv
    public static List<Project> loadProjects(String filePath) throws IOException {
        List<Project> projects = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        reader.readLine();  // Skip the header line

        while ((line = reader.readLine()) != null) {
            String[] columns = line.split("\t");

            String projectName = columns[0];
            String neighborhood = columns[1];
            String type1 = columns[2];
            int numUnitsType1 = Integer.parseInt(columns[3]);
            int sellingPriceType1 = Integer.parseInt(columns[4]);
            String type2 = columns[5];
            int numUnitsType2 = Integer.parseInt(columns[6]);
            int sellingPriceType2 = Integer.parseInt(columns[7]);
            LocalDate openingDate = Project.parseDate(columns[8]);
            LocalDate closingDate = Project.parseDate(columns[9]);
            String manager = columns[10];

            Project project = new Project(projectName, neighborhood, openingDate, closingDate, manager);
            project.addFlatUnit(FlatType.valueOf(type1), numUnitsType1);
            project.addFlatUnit(FlatType.valueOf(type2), numUnitsType2);

            projects.add(project);
        }

        reader.close();
        return projects;
    }
}
