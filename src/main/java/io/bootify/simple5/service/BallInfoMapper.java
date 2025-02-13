package io.bootify.simple5.service;

import io.bootify.simple5.domain.BallInfo;
import io.bootify.simple5.model.BallInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface BallInfoMapper {

    BallInfoDTO updateBallInfoDTO(BallInfo ballInfo, @MappingTarget BallInfoDTO ballInfoDTO);

    @Mapping(target = "ballId", ignore = true)
    BallInfo updateBallInfo(BallInfoDTO ballInfoDTO, @MappingTarget BallInfo ballInfo);

}
