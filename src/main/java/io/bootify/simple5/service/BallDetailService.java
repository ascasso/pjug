package io.bootify.simple5.service;

import io.bootify.simple5.model.BallDetailDTO;
import java.util.List;


public interface BallDetailService {

    List<BallDetailDTO> findAll();

    BallDetailDTO get(Long id);

    Long create(BallDetailDTO ballDetailDTO);

    void update(Long id, BallDetailDTO ballDetailDTO);

    void delete(Long id);

}
