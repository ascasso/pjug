package io.bootify.simple5.service;

import io.bootify.simple5.model.BallInfoDTO;
import io.bootify.simple5.util.ReferencedWarning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BallInfoService {

    Page<BallInfoDTO> findAll(String filter, Pageable pageable);

    BallInfoDTO get(Long ballId);

    Long create(BallInfoDTO ballInfoDTO);

    void update(Long ballId, BallInfoDTO ballInfoDTO);

    void delete(Long ballId);

    ReferencedWarning getReferencedWarning(Long ballId);

}
