package project.restapi.domain.models.api.request;

import project.restapi.constants.RegexValidations;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class TeacherAddRequest {
    private String name;
    private String username;
    private String password;
    private String uniqueCitizenNumber;
    private String degree;

    public TeacherAddRequest() {
    }

    @NotBlank(message = "Name cannot be empty!")
    public String getName() {
        return name;
    }
    @NotBlank(message = "Unique citizen number cannot be empty!")
    @Pattern(regexp = RegexValidations.UNIQUE_CITIZEN_NUMBER_VALIDATOR)
    public String getUniqueCitizenNumber() {
        return uniqueCitizenNumber;
    }

    @NotBlank(message = "Degree cannot be empty!")
    public String getDegree() {
        return degree;
    }

    @NotBlank(message = "Username cannot be empty!")
    public String getUsername() {
        return username;
    }

    @NotBlank(message = "Password cannot be empty!")
    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUniqueCitizenNumber(String uniqueCitizenNumber) {
        this.uniqueCitizenNumber = uniqueCitizenNumber;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }
}
