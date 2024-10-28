package com.example.projectbackend.service.impl;

import com.example.projectbackend.bean.request.BookingDetailRequest;
import com.example.projectbackend.bean.response.BookingDetailResponse;
import com.example.projectbackend.entity.Booking;
import com.example.projectbackend.entity.BookingDetail;
import com.example.projectbackend.entity.Room;
import com.example.projectbackend.exception.EmptyListException;
import com.example.projectbackend.exception.NotFoundException;
import com.example.projectbackend.mapper.BookingDetailMapper;
import com.example.projectbackend.mapper.RoomMapper;
import com.example.projectbackend.repository.BookingDetailRepository;
import com.example.projectbackend.repository.BookingRepository;
import com.example.projectbackend.repository.ProvisionRepository;
import com.example.projectbackend.repository.RoomRepository;
import com.example.projectbackend.service.BookingDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingDetailServiceImpl implements BookingDetailService {
    private final BookingDetailRepository bookingDetailRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final ProvisionRepository provisionRepository;

    @Override
    public List<BookingDetailResponse> getAllBookingDetails() {
        if(bookingDetailRepository.findAll().isEmpty()){
            throw new EmptyListException("EmptyBookingDetail","This list BookingDetail empty");
        }
        return bookingDetailRepository.findAll().stream().map(BookingDetailMapper::convertToResponse).collect(Collectors.toList());

    }

    @Override
    public BookingDetailResponse getDetailBookingDetail(Long bookingDetailId) {
        BookingDetail bookingDetail = bookingDetailRepository.findById(bookingDetailId).orElse(null);
        if(Objects.isNull(bookingDetail)){
            throw new NotFoundException("BookingDetailNotFound","Not found BookingDetail with " + bookingDetailId);

        }
        return BookingDetailMapper.convertToResponse(bookingDetailRepository.findById(bookingDetailId).orElse(null));
    }

    @Override
    public BookingDetail createBookingDetail(BookingDetailRequest bookingDetailRequest, Long bookingId) {
        BookingDetail bookingDetail = BookingDetailMapper.convertFromRequest(bookingDetailRequest);
        bookingDetail.setBooking(bookingRepository.findById(bookingId).orElse(null));
        bookingDetail.setRoom(roomRepository.findById(bookingDetailRequest.getRoomId()).orElse(null));
        bookingDetail.setCreatedAt(LocalDateTime.now());
        return bookingDetailRepository.save(bookingDetail);
    }

    @Override
    public BookingDetail updateBookingDetail(Long bookingDetailId, BookingDetail bookingDetail) {
        BookingDetail bookingDetailUpdate = bookingDetailRepository.findById(bookingDetailId).orElse(null);
        if(bookingDetailUpdate != null){
            setBookingDetail(bookingDetailUpdate, bookingDetail);
        }
        return bookingDetailRepository.save(bookingDetailUpdate);
    }

    @Override
    public void deleteBookingDetail(Long bookingDetailId) {
        Optional<BookingDetail> bookingDetail = bookingDetailRepository.findById(bookingDetailId);
        if(bookingDetail.isEmpty()){
            throw new NotFoundException("BookingDetailNotFound","Not found BookingDetail with " + bookingDetailId);
        }
        bookingDetailRepository.deleteById(bookingDetailId);
    }

    public void setBookingDetail(BookingDetail bookingDetailUpdate, BookingDetail bookingDetailInput) {
        bookingDetailUpdate.setCheckIn(bookingDetailInput.getCheckIn());
        bookingDetailUpdate.setCheckOut(bookingDetailInput.getCheckOut());
        bookingDetailUpdate.setUpdatedAt(LocalDateTime.now());
        bookingDetailUpdate.setSpecialRequests(bookingDetailInput.getSpecialRequests());
        bookingDetailUpdate.setPrice(bookingDetailInput.getPrice());
    }


}
