package com.springboot.blog.service.impl;

import com.springboot.blog.entity.User;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.*;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.service.TwoFactorAuthenticationService;
import com.springboot.blog.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepo;
    private final CommentRepository commentRepo;
    private final TwoFactorAuthenticationService tfaService;


    @Override
    public UserProfile findById(Long id) throws ResourceNotFoundException {
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find any user with the given id: " + id));

        UserProfile userProfile = modelMapper.map(user, UserProfile.class);
        userProfile.setNumOfComments(commentRepo.countByUserId(userProfile.getId()));
        userProfile.setNumOfPosts(postRepo.countByUserId(userProfile.getId()));

        return userProfile;
    }

    @Override
    public PageResponse<UserDTO> listByPage(int pageSize, int pageNo, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<User> page;
        if (keyword != null && !keyword.isEmpty()) {
            page = userRepo.search(keyword, pageable);
        } else {
            page = userRepo.findAllUserNotTrashed(pageable);
        }


        List<UserDTO> userDTOS = page.getContent().stream().map(user -> modelMapper.map(user, UserDTO.class)).toList();
        PageResponse pageResponse = PageResponse.<UserDTO>builder()
                .data(userDTOS)
                .totalPage(page.getTotalPages())
                .pageSize(pageSize)
                .pageNo(pageNo)
                .last(page.isLast())
                .totalElements(page.getTotalElements())
                .build();
        return pageResponse;
    }

    @Override
    @Transactional
    public void deleteUser(Long id) throws ResourceNotFoundException {
        userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find any user with the given id: " + id));
        userRepo.deleteById(id);
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {


        return null;
    }

    @Override
    @Transactional
    public void updateEnabled(Long id, boolean enabled) throws ResourceNotFoundException {
        userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find any user with the given id: " + id));
        userRepo.updateEnabled(id, enabled);

    }

    @Override
    public Long countUser() {
        return userRepo.count();
    }

    @Override
    public List<UserDTO> listTrashedUsers() {
        List<User> userList = userRepo.listTrashedUsers();
        return userList.stream().map(user -> modelMapper.map(user, UserDTO.class)).toList();
    }

    @Override
    @Transactional
    public void updateTrashed(Long id, boolean trashed) throws ResourceNotFoundException {
        userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find any user with the given id: " + id));
        userRepo.updateTrashed(id, trashed);
    }

    @Override
    public void trashUser(Long id) throws ResourceNotFoundException {
        userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find any user with the given id: " + id));
        userRepo.updateTrashed(id, true);
    }

    @Override
    public FullInfoUser fetchMe() throws ResourceNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();
        User userFetchMe = userRepo.findById(user.getId()).get();
        return modelMapper.map(userFetchMe, FullInfoUser.class);
    }

    @Override
    public UserDTO updateProfile(UserProfile user) throws ResourceNotFoundException {
        User userFromDB = userRepo.findById(user.getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Could not find any user with the given id: " + user.getId())
                );
        userFromDB.setFirstName(user.getFirstName());
        userFromDB.setLastName(user.getLastName());
        userFromDB.setAddress(user.getAddress());
        userFromDB.setDescription(user.getDescription());
        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            userFromDB.setAvatar(user.getAvatar());
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            userFromDB.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        User savedUser = userRepo.save(userFromDB);

        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    @Transactional
    public void switchTFAstatus(String email, boolean status) {
        Optional<User> user = userRepo.findByEmail(email);
        user.get().setSecret(tfaService.generateNewSecret());
        userRepo.save(user.get());
        userRepo.switchTFAstatus(email, status);
    }

    @Override
    public void confirmEmail(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setConfirmedEmail(true);
        userRepo.save(user);
    }
}
