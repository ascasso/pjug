package io.bootify.simple5.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.bootify.simple5.model.BallInfoDTO;
import io.bootify.simple5.model.SimpleValue;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class BallInfoAssembler implements SimpleRepresentationModelAssembler<BallInfoDTO> {

    @Override
    public void addLinks(final EntityModel<BallInfoDTO> entityModel) {
        entityModel.add(linkTo(methodOn(BallInfoResource.class).getBallInfo(entityModel.getContent().getBallId())).withSelfRel());
        entityModel.add(linkTo(methodOn(BallInfoResource.class).getAllBallInfos(null, null)).withRel(IanaLinkRelations.COLLECTION));
    }

    @Override
    public void addLinks(final CollectionModel<EntityModel<BallInfoDTO>> collectionModel) {
        collectionModel.add(linkTo(methodOn(BallInfoResource.class).getAllBallInfos(null, null)).withSelfRel());
    }

    public EntityModel<SimpleValue<Long>> toSimpleModel(final Long ballId) {
        final EntityModel<SimpleValue<Long>> simpleModel = SimpleValue.entityModelOf(ballId);
        simpleModel.add(linkTo(methodOn(BallInfoResource.class).getBallInfo(ballId)).withSelfRel());
        return simpleModel;
    }

}
