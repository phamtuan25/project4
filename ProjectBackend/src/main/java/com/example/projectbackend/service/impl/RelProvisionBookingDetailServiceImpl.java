package com.example.projectbackend.service.impl;

import com.example.projectbackend.bean.request.RelProvisionBookingDetailRequest;
import com.example.projectbackend.bean.response.RelProvisionBookingDetailResponse;
import com.example.projectbackend.entity.*;
import com.example.projectbackend.exception.NotFoundException;
import com.example.projectbackend.mapper.RelProvisionBookingDetailMapper;
import com.example.projectbackend.repository.BookingDetailRepository;
import com.example.projectbackend.repository.BookingRepository;
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

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RelProvisionBookingDetailServiceImpl implements RelProvisionBookingDetailService {
    private final RelProvisionBookingDetailRepository relProvisionBookingDetailRepository;
    private final BookingDetailRepository bookingDetailRepository;
    private final ProvisionRepository provisionRepository;
    private final BookingRepository bookingRepository;
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
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("bookingDetail").get("room").get("roomNumber").as(String.class)), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("provision").get("provisionName")), likePattern),
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

        if (relProvisionBookingDetailUpdate == null) {
            throw new NotFoundException("RelProvisionBookingDetailNotFound", "Not Found RelProvisionBookingDetail with id " + relId);
        }

        relProvisionBookingDetailUpdate.setStatus(relProvisionBookingDetail.getStatus());
        relProvisionBookingDetailUpdate.setUpdatedAt(LocalDateTime.now());

        if (relProvisionBookingDetailUpdate.getStatus() == RelProvisionBookingDetail.RelProvisionBookingDetailStatus.USED) {
            Double provisionPrice = relProvisionBookingDetailUpdate.getPrice();

            BookingDetail bookingDetail = relProvisionBookingDetailUpdate.getBookingDetail();
            if (bookingDetail != null) {
                Double currentPrice = bookingDetail.getPrice();
                Double newPrice = currentPrice + provisionPrice;
                bookingDetail.setPrice(newPrice);

                Booking booking = bookingDetail.getBooking();
                if (booking != null) {
                    booking.calculateTotalAmount();

                    bookingDetailRepository.save(bookingDetail);
                    bookingRepository.save(booking);
                }
            }
        }

        return relProvisionBookingDetailRepository.save(relProvisionBookingDetailUpdate);
    }



    @Override
    public void deleteRel(Long relId) {
        Optional<RelProvisionBookingDetail> relProvisionBookingDetail = relProvisionBookingDetailRepository.findById(relId);
        if(relProvisionBookingDetail.isEmpty()){
            throw new NotFoundException("RelProvisionBookingDetailNotFound","Not Found RelProvisionBooking with id " + relId);
        }
        relProvisionBookingDetailRepository.deleteById(relId);
    }


}
