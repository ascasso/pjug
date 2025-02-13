package io.bootify.simple5.rest;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomeResource {

    @GetMapping("/home")
    public RepresentationModel<?> index() {
        return RepresentationModel.of(null)
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BallInfoResource.class).getAllBallInfos(null, null)).withRel("ballInfoes"))
                .add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BallDetailResource.class).getAllBallDetails()).withRel("ballDetails"));
    }

}
