package io.bootify.simple5.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;


public class BallInfoDTO {

    private Long ballId;

    @NotNull
    @Size(max = 255)
    private String ballName;

    @NotNull
    private Integer ballNumber;

    private LocalDateTime ballTime;

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

}
