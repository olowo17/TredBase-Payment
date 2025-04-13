package com.tredbase.payment.service;

import com.tredbase.payment.dto.request.AdminRequestDto;
import com.tredbase.payment.dto.request.BaseUserRequestDto;
import com.tredbase.payment.dto.request.ParentRequestDto;
import com.tredbase.payment.dto.request.StudentRequestDto;
import com.tredbase.payment.dto.response.BaseResponse;
import com.tredbase.payment.dto.response.ResponseCodes;
import com.tredbase.payment.dto.response.UserResponse;
import com.tredbase.payment.entities.Admin;
import com.tredbase.payment.entities.BaseUser;
import com.tredbase.payment.entities.Parent;
import com.tredbase.payment.entities.Student;
import com.tredbase.payment.entities.enums.Role;
import com.tredbase.payment.exception.RoleNotFoundException;
import com.tredbase.payment.repository.AdminRepository;
import com.tredbase.payment.repository.ParentRepository;
import com.tredbase.payment.repository.StudentRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Getter
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public BaseResponse<UserResponse> registerUser(BaseUserRequestDto dto) {
        BaseResponse<UserResponse> response = new BaseResponse<>();

        if (dto instanceof StudentRequestDto studentDto) {
            // Create and save student
            Student student = new Student();
            student.setRole(Role.ROLE_STUDENT);
            mapBaseDtoToUser(studentDto, student);
            student.setPaymentType(studentDto.getPaymentType());

            student = studentRepository.save(student);

            // Map to response
            UserResponse userResponse = mapUserToResponse(student);
            response.setData(userResponse);
            response.setCode(ResponseCodes.SUCCESS);
            response.setDescription("Student registered successfully");

        } else if (dto instanceof ParentRequestDto parentDto) {
            // Create and save parent
            Parent parent = new Parent();
            parent.setRole(Role.ROLE_PARENT);
            mapBaseDtoToUser(parentDto, parent);
            parent = parentRepository.save(parent);

            // Map to response
            UserResponse userResponse = mapUserToResponse(parent);
            response.setData(userResponse);
            response.setCode(ResponseCodes.SUCCESS);
            response.setDescription("Parent registered successfully");

        } else if (dto instanceof AdminRequestDto adminRequestDto) {
            // Create and save parent
            Admin admin = new Admin();
            mapBaseDtoToUser(adminRequestDto, admin);
            admin = adminRepository.save(admin);

            // Map to response
            UserResponse userResponse = mapUserToResponse(admin);
            response.setData(userResponse);
            response.setCode(ResponseCodes.SUCCESS);
            response.setDescription("Admin registered successfully");
        }
        else {
            throw new RoleNotFoundException("Could not determine the role");
        }

        return response;
    }

    private void mapBaseDtoToUser(BaseUserRequestDto dto, BaseUser user) {
        user.setFirstname(dto.getFirstName());
        user.setLastname(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
    }

    private UserResponse mapUserToResponse(BaseUser user) {
        UserResponse response = new UserResponse();
        response.setId(String.valueOf(user.getId()));
        response.setFirstName(user.getFirstname());
        response.setLastName(user.getLastname());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        return response;
    }

}
