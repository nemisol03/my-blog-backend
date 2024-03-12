package com.springboot.blog.controller;

import com.springboot.blog.entity.User;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.*;
import com.springboot.blog.payload.auth.TFARequest;
import com.springboot.blog.service.AuthService;
import com.springboot.blog.service.UserService;
import com.springboot.blog.utils.UploadResource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UploadResource uploadResource;


    @GetMapping("/{id}")
    public ResponseEntity<?> findUser(@PathVariable("id") Long id) {
        try {
            UserProfile user = userService.findById(id);
            return ResponseEntity.ok(user);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping({"", "/", "/manage/all"})
    public ResponseEntity<?> listALlUser(@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                         @RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo,
                                         @RequestParam(value = "sortField", required = false, defaultValue = "id") String sortField,
                                         @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir,
                                         @RequestParam(value = "q", required = false) String keyword) {
        PageResponse<UserDTO> userList = userService.listByPage(pageSize, pageNo, sortField, sortDir,keyword);
        if (userList.getData().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(userList);
    }


    @PutMapping(value = "/me/update",consumes =  MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProfile(@ModelAttribute @Valid UserProfile userProfile,
                                        @RequestParam(value = "image", required = false) MultipartFile multipartFile)
             {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String uploadDir = "users/" + userProfile.getId();
            String url = uploadResource.uploadImage(multipartFile, uploadDir);
            userProfile.setAvatar(url);
        }
        try {
            UserDTO savedUser = userService.updateProfile(userProfile);



            return ResponseEntity.ok(savedUser);
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PatchMapping("/{id}/status/{enabled}")
    public ResponseEntity<?> updateUserStatus(@PathVariable("id") Long id, @PathVariable("enabled") boolean enabled) {
        try {

            userService.updateEnabled(id, enabled);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/trashed/{trashed}")
    public ResponseEntity<?> updateTrashed(@PathVariable("id") Long id, @PathVariable("trashed") boolean trashed) {
        try {
            userService.updateTrashed(id, trashed);
            return ResponseEntity.ok().build();

        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> trashUser(@PathVariable Long id) {
        try {
            userService.trashUser(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}/delete-permanently")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countUser() {
        return ResponseEntity.ok(userService.countUser());
    }

    @GetMapping("/trashed")
    public ResponseEntity<List<UserDTO>> listTrashedUsers() {
        List<UserDTO> userList = userService.listTrashedUsers();
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/me")
    public FullInfoUser fetchMe(@AuthenticationPrincipal User user) throws ResourceNotFoundException {
        return modelMapper.map(user,FullInfoUser.class);
    }

    @PatchMapping("/enable-tfa")
    public void switchTFA(@RequestBody TFARequest tfaRequest) {

        userService.switchTFAstatus(tfaRequest.getEmail(),tfaRequest.isEnabled());

    }
}
