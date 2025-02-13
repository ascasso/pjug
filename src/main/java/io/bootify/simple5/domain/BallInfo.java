package io.bootify.simple5.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
public class BallInfo {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long ballId;

    @Column(nullable = false)
    private String ballName;

    @Column(nullable = false)
    private Integer ballNumber;

    @Column
    private LocalDateTime ballTime;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    public Long getBallId() {
        return ballId;
    }

    public void setBallId(final Long ballId) {
        this.ballId = ballId;
    }

    public String getBallName() {
        return ballName;
    }

    public void setBallName(final String ballName) {
        this.ballName = ballName;
    }

    public Integer getBallNumber() {
        return ballNumber;
    }

    public void setBallNumber(final Integer ballNumber) {
        this.ballNumber = ballNumber;
    }

    public LocalDateTime getBallTime() {
        return ballTime;
    }

    public void setBallTime(final LocalDateTime ballTime) {
        this.ballTime = ballTime;
    }

    public OffsetDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(final OffsetDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public OffsetDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(final OffsetDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
