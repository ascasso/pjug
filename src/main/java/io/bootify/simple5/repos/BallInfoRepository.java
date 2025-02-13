package io.bootify.simple5.repos;

import io.bootify.simple5.domain.BallInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BallInfoRepository extends JpaRepository<BallInfo, Long> {

    Page<BallInfo> findAllByBallId(Long ballId, Pageable pageable);

}
