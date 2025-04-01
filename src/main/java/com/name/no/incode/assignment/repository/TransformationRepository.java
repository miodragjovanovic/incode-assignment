package com.name.no.incode.assignment.repository;

import com.name.no.incode.assignment.report.TransformationReport;
import com.name.no.incode.assignment.model.TransformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TransformationRepository extends JpaRepository<TransformationEntity, UUID> {

    List<TransformationEntity> findAllByCreatedAtGreaterThanAndCreatedAtLessThan(LocalDateTime from, LocalDateTime to);

    @Query("""
                SELECT new com.name.no.incode.assignment.report.TransformationReport(
                    t.transformerType,
                    COUNT(t),
                    CAST(DATE(t.createdAt) AS java.sql.Date)
                )
                FROM TransformationEntity t
                GROUP BY t.transformerType, DATE(t.createdAt)
                ORDER BY DATE(t.createdAt) ASC
            """)
    List<TransformationReport> findTransformationsStats();


}
