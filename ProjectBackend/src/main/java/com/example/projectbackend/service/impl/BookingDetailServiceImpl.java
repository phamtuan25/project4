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
        // Chuyển đổi từ BookingDetailRequest sang BookingDetail
        BookingDetail bookingDetail = BookingDetailMapper.convertFromRequest(bookingDetailRequest);

        // Lấy Booking từ cơ sở dữ liệu
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("BookingNotFound", "Booking not found with ID: " + bookingId));
        bookingDetail.setBooking(booking);

        // Lấy Room từ cơ sở dữ liệu dựa trên roomId
        Room room = roomRepository.findById(bookingDetailRequest.getRoomId())
                .orElseThrow(() -> new NotFoundException("RoomNotFound", "Room not found with ID: " + bookingDetailRequest.getRoomId()));
        bookingDetail.setRoom(room);

        // Lưu BookingDetail vào cơ sở dữ liệu (createdAt sẽ tự động được thiết lập khi gọi save)
        return bookingDetailRepository.save(bookingDetail);
    }


    @Override
    public BookingDetail updateBookingDetail(Long bookingDetailId, BookingDetail bookingDetail) {
        // Tìm kiếm BookingDetail theo bookingDetailId
        BookingDetail bookingDetailUpdate = bookingDetailRepository.findById(bookingDetailId)
                .orElseThrow(() -> new NotFoundException("BookingDetailNotFound", "Booking Detail not found with ID: " + bookingDetailId));

        // Cập nhật thông tin bookingDetail
        setBookingDetail(bookingDetailUpdate, bookingDetail);

        // Lưu BookingDetail đã cập nhật vào cơ sở dữ liệu
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

    private void setBookingDetail(BookingDetail bookingDetailUpdate, BookingDetail bookingDetail) {
        bookingDetailUpdate.setCheckIn(bookingDetail.getCheckIn());
        bookingDetailUpdate.setCheckOut(bookingDetail.getCheckOut());
        bookingDetailUpdate.setStatus(bookingDetail.getStatus());
        bookingDetailUpdate.setSpecialRequests(bookingDetail.getSpecialRequests());
        bookingDetailUpdate.setPrice(bookingDetail.getPrice());
        bookingDetailUpdate.setUpdatedAt(LocalDateTime.now());
    }
}
