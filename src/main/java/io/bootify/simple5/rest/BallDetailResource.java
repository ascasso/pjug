package io.bootify.simple5.rest;

import io.bootify.simple5.model.BallDetailDTO;
import io.bootify.simple5.model.SimpleValue;
import io.bootify.simple5.service.BallDetailService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/ballDetails", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearer-jwt")
public class BallDetailResource {

    private final BallDetailService ballDetailService;
    private final BallDetailAssembler ballDetailAssembler;

    public BallDetailResource(final BallDetailService ballDetailService,
            final BallDetailAssembler ballDetailAssembler) {
        this.ballDetailService = ballDetailService;
        this.ballDetailAssembler = ballDetailAssembler;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<BallDetailDTO>>> getAllBallDetails() {
        final List<BallDetailDTO> ballDetailDTOs = ballDetailService.findAll();
        return ResponseEntity.ok(ballDetailAssembler.toCollectionModel(ballDetailDTOs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<BallDetailDTO>> getBallDetail(
            @PathVariable(name = "id") final Long id) {
        final BallDetailDTO ballDetailDTO = ballDetailService.get(id);
        return ResponseEntity.ok(ballDetailAssembler.toModel(ballDetailDTO));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> createBallDetail(
            @RequestBody @Valid final BallDetailDTO ballDetailDTO) {
        final Long createdId = ballDetailService.create(ballDetailDTO);
        return new ResponseEntity<>(ballDetailAssembler.toSimpleModel(createdId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> updateBallDetail(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final BallDetailDTO ballDetailDTO) {
        ballDetailService.update(id, ballDetailDTO);
        return ResponseEntity.ok(ballDetailAssembler.toSimpleModel(id));
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteBallDetail(@PathVariable(name = "id") final Long id) {
        ballDetailService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
