package com.name.no.incode.assignment.model;

import com.name.no.incode.assignment.enums.TransformerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transformation")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransformationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String input;
    @Column(nullable = false)
    private String transformedInput;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransformerType transformerType;
    private String regex;
    private String replacement;
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
