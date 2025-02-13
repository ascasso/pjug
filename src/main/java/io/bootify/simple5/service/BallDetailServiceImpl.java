package io.bootify.simple5.service;

import io.bootify.simple5.domain.BallDetail;
import io.bootify.simple5.model.BallDetailDTO;
import io.bootify.simple5.repos.BallDetailRepository;
import io.bootify.simple5.repos.BallInfoRepository;
import io.bootify.simple5.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class BallDetailServiceImpl implements BallDetailService {

    private final BallDetailRepository ballDetailRepository;
    private final BallInfoRepository ballInfoRepository;
    private final BallDetailMapper ballDetailMapper;

    public BallDetailServiceImpl(final BallDetailRepository ballDetailRepository,
            final BallInfoRepository ballInfoRepository, final BallDetailMapper ballDetailMapper) {
        this.ballDetailRepository = ballDetailRepository;
        this.ballInfoRepository = ballInfoRepository;
        this.ballDetailMapper = ballDetailMapper;
    }

    @Override
    public List<BallDetailDTO> findAll() {
        final List<BallDetail> ballDetails = ballDetailRepository.findAll(Sort.by("id"));
        return ballDetails.stream()
                .map(ballDetail -> ballDetailMapper.updateBallDetailDTO(ballDetail, new BallDetailDTO()))
                .toList();
    }

    @Override
    public BallDetailDTO get(final Long id) {
        return ballDetailRepository.findById(id)
                .map(ballDetail -> ballDetailMapper.updateBallDetailDTO(ballDetail, new BallDetailDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final BallDetailDTO ballDetailDTO) {
        final BallDetail ballDetail = new BallDetail();
        ballDetailMapper.updateBallDetail(ballDetailDTO, ballDetail, ballInfoRepository);
        return ballDetailRepository.save(ballDetail).getId();
    }

    @Override
    public void update(final Long id, final BallDetailDTO ballDetailDTO) {
        final BallDetail ballDetail = ballDetailRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        ballDetailMapper.updateBallDetail(ballDetailDTO, ballDetail, ballInfoRepository);
        ballDetailRepository.save(ballDetail);
    }

    @Override
    public void delete(final Long id) {
        ballDetailRepository.deleteById(id);
    }

}
