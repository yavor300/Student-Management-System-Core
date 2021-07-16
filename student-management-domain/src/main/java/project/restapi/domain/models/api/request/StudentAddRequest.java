package project.restapi.domain.models.api.request;

import project.restapi.constants.RegexValidations;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

public class StudentAddRequest {
    private String name;
    private Integer age;
    private String username;
    private String password;
    private String uniqueCitizenNumber;

    public StudentAddRequest() {
    }

    @NotBlank(message = "Name cannot be empty!")
    public String getName() {
        return name;
    }

    @Positive(message = "Age must be positive!")
    public Integer getAge() {
        return age;
    }

    @NotBlank(message = "Unique citizen number cannot be empty!")
    @Pattern(regexp = RegexValidations.UNIQUE_CITIZEN_NUMBER_VALIDATOR)
    public String getUniqueCitizenNumber() {
        return uniqueCitizenNumber;
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

    public void setAge(Integer age) {
        this.age = age;
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
}
