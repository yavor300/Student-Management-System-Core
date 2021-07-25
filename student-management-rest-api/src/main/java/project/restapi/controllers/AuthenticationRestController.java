package project.restapi.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import project.restapi.constants.ApiPaths;
import project.restapi.domain.models.api.request.LoggedInUserRequest;
import project.restapi.domain.models.api.request.StudentAddRequest;
import project.restapi.domain.models.api.request.TeacherAddRequest;
import project.restapi.domain.models.api.request.UserLoginRequest;
import project.restapi.domain.models.api.response.AuthenticationResponse;
import project.restapi.domain.models.api.response.StudentAddResponse;
import project.restapi.domain.models.api.response.TeacherAddResponse;
import project.restapi.domain.models.api.response.UserLoggedInResponse;
import project.restapi.filters.JwtUtil;
import project.restapi.service.StudentService;
import project.restapi.service.TeacherService;

import javax.validation.Valid;

@RestController
@RequestMapping(ApiPaths.BASE_PUBLIC)
public class AuthenticationRestController {
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthenticationRestController(StudentService studentService, TeacherService teacherService, AuthenticationManager authenticationManager, @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, JwtUtil jwtUtil, ModelMapper modelMapper) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
    }

    @CrossOrigin(origins = "http://localhost:4200/")
    @PostMapping(ApiPaths.LOGIN)
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginRequest.getUsername(), userLoginRequest.getPassword())
            );

        UserDetails userDetails = userDetailsService
                .loadUserByUsername(userLoginRequest.getUsername());

        String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping(ApiPaths.REGISTER_STUDENT)
    public ResponseEntity<StudentAddResponse> registerStudent(@RequestBody StudentAddRequest studentAddRequest) {
        return ResponseEntity.ok(studentService.add(studentAddRequest));
    }

    @CrossOrigin
    @PostMapping(ApiPaths.REGISTER_TEACHER)
    public ResponseEntity<TeacherAddResponse> registerStudent(@RequestBody TeacherAddRequest teacherAddRequest) {
        return ResponseEntity.ok(teacherService.add(teacherAddRequest));
    }

    @CrossOrigin
    @PostMapping(ApiPaths.LOGGED_ID)
    public ResponseEntity<UserLoggedInResponse> getLoggedUser(@RequestBody LoggedInUserRequest loggedInUserRequest) {
        return ResponseEntity.ok(modelMapper.map(userDetailsService.loadUserByUsername(loggedInUserRequest.getUsername()), UserLoggedInResponse.class));
    }
}