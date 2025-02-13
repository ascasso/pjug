package io.bootify.simple5.service;

import io.bootify.simple5.domain.BallDetail;
import io.bootify.simple5.domain.BallInfo;
import io.bootify.simple5.model.BallInfoDTO;
import io.bootify.simple5.repos.BallDetailRepository;
import io.bootify.simple5.repos.BallInfoRepository;
import io.bootify.simple5.util.NotFoundException;
import io.bootify.simple5.util.ReferencedWarning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class BallInfoServiceImpl implements BallInfoService {

    private final BallInfoRepository ballInfoRepository;
    private final BallInfoMapper ballInfoMapper;
    private final BallDetailRepository ballDetailRepository;

    public BallInfoServiceImpl(final BallInfoRepository ballInfoRepository,
            final BallInfoMapper ballInfoMapper, final BallDetailRepository ballDetailRepository) {
        this.ballInfoRepository = ballInfoRepository;
        this.ballInfoMapper = ballInfoMapper;
        this.ballDetailRepository = ballDetailRepository;
    }

    @Override
    public Page<BallInfoDTO> findAll(final String filter, final Pageable pageable) {
        Page<BallInfo> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = ballInfoRepository.findAllByBallId(longFilter, pageable);
        } else {
            page = ballInfoRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(ballInfo -> ballInfoMapper.updateBallInfoDTO(ballInfo, new BallInfoDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    @Override
    public BallInfoDTO get(final Long ballId) {
        return ballInfoRepository.findById(ballId)
                .map(ballInfo -> ballInfoMapper.updateBallInfoDTO(ballInfo, new BallInfoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final BallInfoDTO ballInfoDTO) {
        final BallInfo ballInfo = new BallInfo();
        ballInfoMapper.updateBallInfo(ballInfoDTO, ballInfo);
        return ballInfoRepository.save(ballInfo).getBallId();
    }

    @Override
    public void update(final Long ballId, final BallInfoDTO ballInfoDTO) {
        final BallInfo ballInfo = ballInfoRepository.findById(ballId)
                .orElseThrow(NotFoundException::new);
        ballInfoMapper.updateBallInfo(ballInfoDTO, ballInfo);
        ballInfoRepository.save(ballInfo);
    }

    @Override
    public void delete(final Long ballId) {
        ballInfoRepository.deleteById(ballId);
    }

    @Override
    public ReferencedWarning getReferencedWarning(final Long ballId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final BallInfo ballInfo = ballInfoRepository.findById(ballId)
                .orElseThrow(NotFoundException::new);
        final BallDetail ballInfoBallDetail = ballDetailRepository.findFirstByBallInfo(ballInfo);
        if (ballInfoBallDetail != null) {
            referencedWarning.setKey("ballInfo.ballDetail.ballInfo.referenced");
            referencedWarning.addParam(ballInfoBallDetail.getId());
            return referencedWarning;
        }
        return null;
    }

}
