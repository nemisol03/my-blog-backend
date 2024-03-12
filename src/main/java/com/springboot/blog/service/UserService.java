package com.springboot.blog.service;

import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.*;

import java.util.List;

public interface UserService {
    UserProfile findById(Long id) throws ResourceNotFoundException;

//    PageResponse<UserDTO> listAllEnabledUser();
    PageResponse<UserDTO> listByPage(int pageSize, int pageNo, String sortField, String sortDir, String keyword);

    void deleteUser(Long id) throws ResourceNotFoundException;

    UserDTO createUser(UserDTO userDTO);

    void updateEnabled(Long id,boolean enabled) throws ResourceNotFoundException;

    Long countUser();
    List<UserDTO> listTrashedUsers();

    void updateTrashed(Long id, boolean trashed) throws ResourceNotFoundException;

    void trashUser(Long id) throws ResourceNotFoundException;

    FullInfoUser fetchMe() throws ResourceNotFoundException;

    UserDTO updateProfile(UserProfile userProfile) throws ResourceNotFoundException;

    void switchTFAstatus(String email, boolean status);

    void confirmEmail(String email);
}
