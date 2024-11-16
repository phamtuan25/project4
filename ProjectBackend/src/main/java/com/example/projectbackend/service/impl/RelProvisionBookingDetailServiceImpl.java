package com.example.projectbackend.service.impl;

import com.example.projectbackend.bean.request.RelProvisionBookingDetailRequest;
import com.example.projectbackend.bean.response.BookingResponse;
import com.example.projectbackend.bean.response.RelProvisionBookingDetailResponse;
import com.example.projectbackend.entity.*;
import com.example.projectbackend.exception.EmptyListException;
import com.example.projectbackend.exception.NotFoundException;
import com.example.projectbackend.mapper.BookingMapper;
import com.example.projectbackend.mapper.RelProvisionBookingDetailMapper;
import com.example.projectbackend.repository.BookingDetailRepository;
import com.example.projectbackend.repository.ProvisionRepository;
import com.example.projectbackend.repository.RelProvisionBookingDetailRepository;
import com.example.projectbackend.service.RelProvisionBookingDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelProvisionBookingDetailServiceImpl implements RelProvisionBookingDetailService {
    private final RelProvisionBookingDetailRepository relProvisionBookingDetailRepository;
    private final BookingDetailRepository bookingDetailRepository;
    private final ProvisionRepository provisionRepository;
    @Override
    public Page<RelProvisionBookingDetailResponse> getAllRel(@PageableDefault(size = 10, page = 0) Pageable pageable,
                                                             @RequestParam(required = false) String keyword) {
        Specification<RelProvisionBookingDetail> spec = searchByKeyword(keyword);
        Page<RelProvisionBookingDetail> relProvisionBookingDetailPage = relProvisionBookingDetailRepository.findAll(spec, pageable);
        return relProvisionBookingDetailPage.map(relProvisionBookingDetail -> {
            RelProvisionBookingDetailResponse relProvisionBookingDetailResponse = RelProvisionBookingDetailMapper.convertToResponse(relProvisionBookingDetail);
            return relProvisionBookingDetailResponse;
        });
    }

    public static Specification<RelProvisionBookingDetail> searchByKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + keyword + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("status")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("price").as(String.class)), likePattern)
            );
        };
    }

    @Override
    public RelProvisionBookingDetailResponse getDetailRel(Long relId) {
        RelProvisionBookingDetail relProvisionBookingDetail = relProvisionBookingDetailRepository.findById(relId).orElse(null);
        if(Objects.isNull(relProvisionBookingDetail)){
            throw new NotFoundException("RelProvisionBookingDetailNotFound","Not Found RelProvisionBooking with id " + relId);
        }
        return RelProvisionBookingDetailMapper.convertToResponse(relProvisionBookingDetailRepository.findById(relId).orElse(null));
    }


    @Override
    public RelProvisionBookingDetail createRel(RelProvisionBookingDetailRequest relProvisionBookingDetailRequest) {
        RelProvisionBookingDetail relProvisionBookingDetail = RelProvisionBookingDetailMapper.convertFromRequest(relProvisionBookingDetailRequest);
        relProvisionBookingDetail.setProvision(provisionRepository.findById(relProvisionBookingDetailRequest.getProvisionId()).orElse(null));
        relProvisionBookingDetail.setBookingDetail(bookingDetailRepository.findById(relProvisionBookingDetailRequest.getBookingDetailId()).orElse(null));
        return relProvisionBookingDetailRepository.save(relProvisionBookingDetail);
    }

    @Override
    public RelProvisionBookingDetail updateRel(Long relId, RelProvisionBookingDetail relProvisionBookingDetail) {
        RelProvisionBookingDetail relProvisionBookingDetailUpdate = relProvisionBookingDetailRepository.findById(relId).orElse(null);
        if(relProvisionBookingDetailUpdate != null){
            setRelProvisionBookingDetail(relProvisionBookingDetailUpdate, relProvisionBookingDetail);
        }
        return relProvisionBookingDetailRepository.save(relProvisionBookingDetail);
    }

    @Override
    public void deleteRel(Long relId) {
        Optional<RelProvisionBookingDetail> relProvisionBookingDetail = relProvisionBookingDetailRepository.findById(relId);
        if(relProvisionBookingDetail.isEmpty()){
            throw new NotFoundException("RelProvisionBookingDetailNotFound","Not Found RelProvisionBooking with id " + relId);
        }
        relProvisionBookingDetailRepository.deleteById(relId);
    }

    private void setRelProvisionBookingDetail(RelProvisionBookingDetail relProvisionBookingDetailUpdate, RelProvisionBookingDetail relProvisionBookingDetailInput){
        relProvisionBookingDetailUpdate.setStatus(relProvisionBookingDetailInput.getStatus());
        relProvisionBookingDetailUpdate.setPrice(relProvisionBookingDetailInput.getPrice());
    }
}
