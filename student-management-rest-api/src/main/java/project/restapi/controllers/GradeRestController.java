package project.restapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.restapi.constants.ApiPaths;
import project.restapi.domain.models.api.response.GradeAddResponse;
import project.restapi.domain.models.api.request.GradeAddRequest;
import project.restapi.service.GradeService;

@RestController
@RequestMapping(ApiPaths.BASE_GRADE)
public class GradeRestController {
    private final GradeService gradeService;

    @Autowired
    public GradeRestController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping(ApiPaths.GRADE_ADD)
    public ResponseEntity<GradeAddResponse> addGrade(@RequestBody GradeAddRequest addGradeRestModel) {
        return ResponseEntity.ok(gradeService.addGrade(addGradeRestModel));
    }
}