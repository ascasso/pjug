package io.bootify.simple5.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import io.bootify.simple5.model.BallDetailDTO;
import io.bootify.simple5.model.SimpleValue;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class BallDetailAssembler implements SimpleRepresentationModelAssembler<BallDetailDTO> {

    @Override
    public void addLinks(final EntityModel<BallDetailDTO> entityModel) {
        entityModel.add(linkTo(methodOn(BallDetailResource.class).getBallDetail(entityModel.getContent().getId())).withSelfRel());
        entityModel.add(linkTo(methodOn(BallDetailResource.class).getAllBallDetails()).withRel(IanaLinkRelations.COLLECTION));
        if (entityModel.getContent().getBallInfo() != null) {
            entityModel.add(linkTo(methodOn(BallInfoResource.class).getBallInfo(entityModel.getContent().getBallInfo())).withRel("ballInfo"));
        }
    }

    @Override
    public void addLinks(final CollectionModel<EntityModel<BallDetailDTO>> collectionModel) {
        collectionModel.add(linkTo(methodOn(BallDetailResource.class).getAllBallDetails()).withSelfRel());
    }

    public EntityModel<SimpleValue<Long>> toSimpleModel(final Long id) {
        final EntityModel<SimpleValue<Long>> simpleModel = SimpleValue.entityModelOf(id);
        simpleModel.add(linkTo(methodOn(BallDetailResource.class).getBallDetail(id)).withSelfRel());
        return simpleModel;
    }

}
