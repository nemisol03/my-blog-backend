package com.springboot.blog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public  abstract class AuditingInformation {
    @CreatedDate
    @Column(nullable = false,
            updatable = false)
    private LocalDateTime createdDate;


    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime modifiedDate;

    @CreatedBy
    @Column(nullable = false,
            updatable = false)
    private Long createdBy;


    @LastModifiedBy
    @Column(insertable = false)
    private Long modifiedBy;
}
