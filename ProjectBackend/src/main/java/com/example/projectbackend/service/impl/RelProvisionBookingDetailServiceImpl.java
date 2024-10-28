package com.example.projectbackend.service.impl;

import com.example.projectbackend.bean.request.RelProvisionBookingDetailRequest;
import com.example.projectbackend.bean.response.RelProvisionBookingDetailResponse;
import com.example.projectbackend.entity.BookingDetail;
import com.example.projectbackend.entity.Provision;
import com.example.projectbackend.entity.RelProvisionBookingDetail;
import com.example.projectbackend.exception.EmptyListException;
import com.example.projectbackend.exception.NotFoundException;
import com.example.projectbackend.mapper.RelProvisionBookingDetailMapper;
import com.example.projectbackend.repository.BookingDetailRepository;
import com.example.projectbackend.repository.ProvisionRepository;
import com.example.projectbackend.repository.RelProvisionBookingDetailRepository;
import com.example.projectbackend.service.RelProvisionBookingDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public List<RelProvisionBookingDetailResponse> getAllRel() {
        if(relProvisionBookingDetailRepository.findAll().isEmpty()) {
            throw new EmptyListException("RelProvisionBookingDetail","This list RelProvisionBookingDetail is empty");
        }
        return relProvisionBookingDetailRepository.findAll().stream().map(RelProvisionBookingDetailMapper::convertToResponse).collect(Collectors.toList());
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
