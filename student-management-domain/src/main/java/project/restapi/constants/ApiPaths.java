package project.restapi.constants;

public class ApiPaths {
    public static final String BASE_PUBLIC = "/api/public";

    public static final String LOGIN = "/login";

    public static final String FULL_LOGIN = BASE_PUBLIC + LOGIN;

    public static final String REGISTER_STUDENT = "/register/student";

    public static final String FULL_REGISTER_STUDENT = BASE_PUBLIC + REGISTER_STUDENT;

    public static final String REGISTER_TEACHER = "/register/teacher";

    public static final String FULL_REGISTER_TEACHER = BASE_PUBLIC + REGISTER_TEACHER;

    public static final String BASE_STUDENT = "/api/student";

    public static final String STUDENT_AVERAGE = "/average/{id}";

    public static final String BASE_COURSE = "/api/course";

    public static final String COURSE_ALL = "/all";

    public static final String COURSE_ALL_ORDERED = COURSE_ALL + "/ordered";

    public static final String COURSE_AVERAGE = "/average/{name}";

    public static final String BASE_GRADE = "/api/grade";

    public static final String GRADE_ADD = "/add";

    public static final String STUDENT_ADD_COURSE = "/add/course";

    public static final String COURSE_ADD_TEACHER = "/add/teacher";

    public static final String BASE_ROLE = "/api/role";

    public static final String MAKE_TEACHER_ADMIN = "/admin/teacher";

    public static final String RESTORE_TEACHER = "/restore/teacher";

    public static final String MAKE_STUDENT_TEACHER = "/teacher/student";

    public static final String MAKE_STUDENT_ADMIN = "/admin/student";

    public static final String RESTORE_STUDENT = "/restore/student";

    public static final String STUDENT_AVERAGE_SELF = "/average";

    public static final String BASE_TEACHER = "/api/teacher";

    public static final String COURSES_AVERAGE_SELF = "/courses/all";

    public static final String BASE_ADMIN = "/api/admin";

    public static final String GET_TEACHER = "/teacher/{name}";

    public static final String ALL_STUDENTS = "/students/{name}"
            ;
    public static final String STUDENTS_GRADES = "/students/grades/{name}";

    public static final String COURSE_AVERAGE_GRADE = "/grade/{name}";

    public static final String COURSE_DELETE_TEACHER = "/delete/teacher";

    public static final String STUDENT_PROFILE = "/profile/{name}";
}