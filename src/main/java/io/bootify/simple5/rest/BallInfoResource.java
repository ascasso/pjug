package io.bootify.simple5.rest;

import io.bootify.simple5.model.BallInfoDTO;
import io.bootify.simple5.model.SimpleValue;
import io.bootify.simple5.service.BallInfoService;
import io.bootify.simple5.util.ReferencedException;
import io.bootify.simple5.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/ballInfos", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearer-jwt")
public class BallInfoResource {

    private final BallInfoService ballInfoService;
    private final BallInfoAssembler ballInfoAssembler;
    private final PagedResourcesAssembler<BallInfoDTO> pagedResourcesAssembler;

    public BallInfoResource(final BallInfoService ballInfoService,
            final BallInfoAssembler ballInfoAssembler,
            final PagedResourcesAssembler<BallInfoDTO> pagedResourcesAssembler) {
        this.ballInfoService = ballInfoService;
        this.ballInfoAssembler = ballInfoAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Operation(
            parameters = {
                    @Parameter(
                            name = "page",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "size",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "sort",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = String.class)
                    )
            }
    )
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<BallInfoDTO>>> getAllBallInfos(
            @RequestParam(name = "filter", required = false) final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "ballId") @PageableDefault(size = 20) final Pageable pageable) {
        final Page<BallInfoDTO> ballInfoDTOs = ballInfoService.findAll(filter, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(ballInfoDTOs, ballInfoAssembler));
    }

    @GetMapping("/{ballId}")
    public ResponseEntity<EntityModel<BallInfoDTO>> getBallInfo(
            @PathVariable(name = "ballId") final Long ballId) {
        final BallInfoDTO ballInfoDTO = ballInfoService.get(ballId);
        return ResponseEntity.ok(ballInfoAssembler.toModel(ballInfoDTO));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> createBallInfo(
            @RequestBody @Valid final BallInfoDTO ballInfoDTO) {
        final Long createdBallId = ballInfoService.create(ballInfoDTO);
        return new ResponseEntity<>(ballInfoAssembler.toSimpleModel(createdBallId), HttpStatus.CREATED);
    }

    @PutMapping("/{ballId}")
    public ResponseEntity<EntityModel<SimpleValue<Long>>> updateBallInfo(
            @PathVariable(name = "ballId") final Long ballId,
            @RequestBody @Valid final BallInfoDTO ballInfoDTO) {
        ballInfoService.update(ballId, ballInfoDTO);
        return ResponseEntity.ok(ballInfoAssembler.toSimpleModel(ballId));
    }

    @DeleteMapping("/{ballId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteBallInfo(@PathVariable(name = "ballId") final Long ballId) {
        final ReferencedWarning referencedWarning = ballInfoService.getReferencedWarning(ballId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        ballInfoService.delete(ballId);
        return ResponseEntity.noContent().build();
    }

}
