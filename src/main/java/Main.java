import project.restapi.StudentManagerRestApi;

/**
 * In the Main the application starts.
 * The user can choose to run the application in the console using CLI or accessing RESTFul API
 * to manage the student system.
 * For the CLI, an instance of the EngineImpl class should be created and the run method should be called.
 * All of the logic necessary for the CLI commands is inside the 'EngineImpl' class.
 * For the RESTful API, the main method of the StudentManagerRestApi class should be called.
 * Spring project will be loaded and started.
 */
public class Main {
    public static void main(String[] args) {
        StudentManagerRestApi.main(args);
    }
}