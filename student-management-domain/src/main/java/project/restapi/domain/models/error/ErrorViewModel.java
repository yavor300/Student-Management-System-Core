package project.restapi.domain.models.error;

import java.util.ArrayList;
import java.util.List;

public class ErrorViewModel {
    private final List<String> errors;

    public ErrorViewModel() {
        this.errors = new ArrayList<>();
    }

    public ErrorViewModel(String message) {
        this.errors = new ArrayList<>();
        this.errors.add(message);
    }

    public void add(String message) {
        this.errors.add(message);
    }

    public List<String> getErrors() {
        return errors;
    }
}
