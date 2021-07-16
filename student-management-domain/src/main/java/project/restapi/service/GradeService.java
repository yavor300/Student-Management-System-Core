package project.restapi.service;

import project.restapi.domain.models.api.request.GradeAddRequest;
import project.restapi.domain.models.api.response.GradeAddResponse;

public interface GradeService {
    GradeAddResponse addGrade(GradeAddRequest gradeAddRequest);
}
