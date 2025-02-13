package io.bootify.simple5.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class BallDetailDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String color;

    private Long ballInfo;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public Long getBallInfo() {
        return ballInfo;
    }

    public void setBallInfo(final Long ballInfo) {
        this.ballInfo = ballInfo;
    }

}
