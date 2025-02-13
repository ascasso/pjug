package io.bootify.simple5.service;

import io.bootify.simple5.domain.BallDetail;
import io.bootify.simple5.domain.BallInfo;
import io.bootify.simple5.model.BallDetailDTO;
import io.bootify.simple5.repos.BallInfoRepository;
import io.bootify.simple5.util.NotFoundException;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface BallDetailMapper {

    @Mapping(target = "ballInfo", ignore = true)
    BallDetailDTO updateBallDetailDTO(BallDetail ballDetail,
            @MappingTarget BallDetailDTO ballDetailDTO);

    @AfterMapping
    default void afterUpdateBallDetailDTO(BallDetail ballDetail,
            @MappingTarget BallDetailDTO ballDetailDTO) {
        ballDetailDTO.setBallInfo(ballDetail.getBallInfo() == null ? null : ballDetail.getBallInfo().getBallId());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ballInfo", ignore = true)
    BallDetail updateBallDetail(BallDetailDTO ballDetailDTO, @MappingTarget BallDetail ballDetail,
            @Context BallInfoRepository ballInfoRepository);

    @AfterMapping
    default void afterUpdateBallDetail(BallDetailDTO ballDetailDTO,
            @MappingTarget BallDetail ballDetail, @Context BallInfoRepository ballInfoRepository) {
        final BallInfo ballInfo = ballDetailDTO.getBallInfo() == null ? null : ballInfoRepository.findById(ballDetailDTO.getBallInfo())
                .orElseThrow(() -> new NotFoundException("ballInfo not found"));
        ballDetail.setBallInfo(ballInfo);
    }

}
