package com.springboot.blog.controller;

import com.springboot.blog.entity.Tag;
import com.springboot.blog.exception.ResourceInUseException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.TagDTO;
import com.springboot.blog.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tags")
public class TagController {
    private final TagService tagService;

    @GetMapping
    public ResponseEntity<?> getAllTags() {
        List<Tag> tagList = tagService.listAllTag();
        if (tagList.isEmpty()) {
            return new ResponseEntity<>("Tags is empty!", HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(tagList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            TagDTO tag = tagService.findById(id);
            return ResponseEntity.ok(tag);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody @Valid TagDTO tagDTO) {
        try {
            TagDTO tag = tagService.saveTag(tagDTO);
            URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequestUri()
                    .path("/{id}")
                    .buildAndExpand(tag.getId())
                    .toUri();
            return ResponseEntity.created(uri).body(tag);
        } catch (ResourceInUseException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable Long id) {
        try {
            tagService.deleteTag(id);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTag(@PathVariable Long id,@RequestBody TagDTO tagDTO) {
        try {
            TagDTO tag  = tagService.updateTag(id,tagDTO);
        return ResponseEntity.ok(tag);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/count")
    public ResponseEntity<Long> countTag() {
        return ResponseEntity.ok(tagService.countTag());
    }


}
