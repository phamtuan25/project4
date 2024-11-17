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
        // Tìm đối tượng RelProvisionBookingDetail hiện tại từ database
        RelProvisionBookingDetail relProvisionBookingDetailUpdate = relProvisionBookingDetailRepository.findById(relId).orElse(null);

        // Kiểm tra nếu không tìm thấy đối tượng
        if (relProvisionBookingDetailUpdate == null) {
            throw new NotFoundException("RelProvisionBookingDetailNotFound", "Không tìm thấy RelProvisionBookingDetail với id " + relId);
        }

        // Cập nhật chỉ trường status
        relProvisionBookingDetailUpdate.setStatus(relProvisionBookingDetail.getStatus());

        // Set the updatedAt field to the current time when updating
        relProvisionBookingDetailUpdate.setUpdatedAt(LocalDateTime.now());  // Manually update the timestamp

        // Nếu status là USED, cập nhật lại giá trị price và tổng amount của BookingDetail
        if (relProvisionBookingDetailUpdate.getStatus() == RelProvisionBookingDetail.RelProvisionBookingDetailStatus.USED) {
            // Lấy giá trị price của RelProvisionBookingDetail (nếu có)
            Double provisionPrice = relProvisionBookingDetailUpdate.getPrice(); // Giá trị đã được gán từ trước

            // Lấy BookingDetail liên quan
            BookingDetail bookingDetail = relProvisionBookingDetailUpdate.getBookingDetail();
            if (bookingDetail != null) {
                // Lấy giá hiện tại của BookingDetail
                Double currentPrice = bookingDetail.getPrice();

                // Cộng giá trị price của RelProvisionBookingDetail vào giá trị price của BookingDetail
                Double newPrice = currentPrice + provisionPrice;
                bookingDetail.setPrice(newPrice);

                // Tính lại totalAmount của Booking liên quan
                Booking booking = bookingDetail.getBooking();
                if (booking != null) {
                    // Tính lại totalAmount và deposit cho Booking
                    booking.calculateTotalAmount();

                    // Lưu lại BookingDetail và Booking sau khi cập nhật
                    bookingDetailRepository.save(bookingDetail);
                    bookingRepository.save(booking);
                }
            }
        }

        // Lưu lại đối tượng RelProvisionBookingDetail đã được cập nhật
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
