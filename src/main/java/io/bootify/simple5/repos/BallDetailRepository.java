package io.bootify.simple5.repos;

import io.bootify.simple5.domain.BallDetail;
import io.bootify.simple5.domain.BallInfo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BallDetailRepository extends JpaRepository<BallDetail, Long> {

    BallDetail findFirstByBallInfo(BallInfo ballInfo);

}
