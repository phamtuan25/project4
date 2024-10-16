package com.example.projectbackend.service.impl;

import com.example.projectbackend.bean.request.BookingRequest;
import com.example.projectbackend.bean.response.BookingResponse;
import com.example.projectbackend.entity.Booking;
import com.example.projectbackend.exception.EmptyListException;
import com.example.projectbackend.exception.NotFoundException;
import com.example.projectbackend.mapper.BookingMapper;
import com.example.projectbackend.repository.BookingRepository;
import com.example.projectbackend.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
    public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    @Override
    public List<BookingResponse> getAllBookings() {
        if(bookingRepository.findAll().isEmpty()) {
            throw new EmptyListException("EmptyBooking","This list Booking is empty");
        }
        return bookingRepository.findAll().stream().map(BookingMapper::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public BookingResponse getDetailBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if(Objects.isNull(booking)){
            throw new NotFoundException("BookingNotFound","Not found Booking with " + bookingId);
        }
        BookingResponse bookResponse = BookingMapper.convertToResponse(booking);
        return bookResponse;
    }

    @Override
    public Booking createBooking(BookingRequest bookingRequest) {
        Booking booking = BookingMapper.convertFromRequest(bookingRequest);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBooking(Long bookingId, Booking booking) {
        Booking bookingUpdate = bookingRepository.findById(bookingId).orElse(null);
        if(bookingUpdate != null){
            setBooking(bookingUpdate, booking);
        }
        return bookingRepository.save(bookingUpdate);
    }

    @Override
    public void deleteBooking(Long bookingId) {
        Optional<Booking> booking = bookingRepository.findById(bookingId);

        if (booking.isEmpty()) {
            throw new NotFoundException("BookingNotFound","Booking with ID " + bookingId + " not found.");
        }
        bookingRepository.deleteById(bookingId);
    }

    public void setBooking(Booking bookingUpdate, Booking bookingInput){
        bookingUpdate.setUser(bookingInput.getUser());
        bookingUpdate.setCreatedAt(bookingInput.getCreatedAt());
        bookingUpdate.setUpdatedAt(bookingInput.getUpdatedAt());
        bookingUpdate.setBookingDetails(bookingInput.getBookingDetails());
    }
}
