package com.example.projectbackend.service.impl;

import com.example.projectbackend.bean.request.BookingDetailRequest;
import com.example.projectbackend.bean.request.BookingRequest;
import com.example.projectbackend.bean.response.BookingResponse;
import com.example.projectbackend.entity.*;
import com.example.projectbackend.exception.NotFoundException;
import com.example.projectbackend.mapper.BookingMapper;
import com.example.projectbackend.repository.*;
import com.example.projectbackend.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
    public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingDetailRepository bookingDetailRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final RelProvisionBookingDetailRepository relProvisionBookingDetailRepository;  // Thêm phụ thuộc vào RelProvisionBookingDetailRepository
    private final ProvisionRepository provisionRepository;

    @Override
    public Page<BookingResponse> getAllBookings(@PageableDefault(size = 10, page = 0) Pageable pageable,
                                                @RequestParam(required = false) String keyword) {
        Specification<Booking> spec = searchByKeyword(keyword);
        Page<Booking> bookingsPage = bookingRepository.findAll(spec, pageable);
        return bookingsPage.map(booking -> {
            BookingResponse bookingResponse = BookingMapper.convertToResponse(booking);
            return bookingResponse;
        });
    }

    @Override
    public BookingResponse getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("BookingNotFound", "Booking not found with ID: " + bookingId));

        return BookingMapper.convertToResponse(booking); // Chuyển đổi Booking thành BookingResponse
    }

    @Override
    public List<BookingResponse> getBookingsByUserId(Long userId) {
        List<Booking> bookings = bookingRepository.findByUser_UserId(userId);

        if (bookings.isEmpty()) {
            throw new NotFoundException("BookingNotFound", "No bookings found for user with ID " + userId);
        }
        List<BookingResponse> bookingResponses = bookings.stream()
                .map(BookingMapper::convertToResponse)
                .collect(Collectors.toList());

        return bookingResponses;
    }

    @Override
    public Booking createBooking(BookingRequest bookingRequest) {
        // Kiểm tra xem booking detail có rỗng không
        BookingDetailRequest bookingDetailRequest = bookingRequest.getBookingDetailRequests();

        // Tìm kiếm người dùng từ cơ sở dữ liệu
        User user = userRepository.findById(bookingRequest.getUser().getUserId())
                .orElseThrow(() -> new NotFoundException("UserNotFound", "User not found with ID: " + bookingRequest.getUser().getUserId()));

        if (bookingDetailRequest == null) {
            throw new NotFoundException("BookingDetailEmpty", "Booking Detail cannot be empty.");
        }

        // Lấy thông tin phòng từ bookingDetailRequest
        Long roomId = bookingDetailRequest.getRoomId();
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("RoomNotFound", "Room not found with ID: " + roomId));

        // Kiểm tra xem người dùng có booking nào chưa hoàn thành (PENDING) không
        Booking existingBooking = bookingRepository.findFirstByUserAndStatus(user, Booking.BookingStatus.PENDING);
        Booking booking;

        if (existingBooking != null) {
            // Nếu đã có booking PENDING, sử dụng booking đó và thêm booking detail mới vào
            booking = existingBooking;

            // Lấy danh sách phòng từ booking hiện tại
            List<Room> currentRooms = booking.getBookingDetails().stream()
                    .map(bookingDetail -> bookingDetail.getRoom())
                    .collect(Collectors.toList());

            // Kiểm tra phòng mới có trùng với phòng cũ không
            if (currentRooms.contains(room)) {
                throw new NotFoundException("RoomAlreadyBooked", "Room with ID " + room.getRoomId() + " has already been booked in this booking.");
            }
        } else {
            // Nếu không có booking PENDING, tạo booking mới
            booking = new Booking();
            booking.setUser(user); // Gán người dùng đã tìm thấy
            booking.setCreatedAt(LocalDateTime.now());
            booking.setStatus(Booking.BookingStatus.PENDING); // Set trạng thái booking là PENDING
            booking.setBookingDetails(new ArrayList<>());

            // Lưu booking để có bookingId
            booking = bookingRepository.save(booking);

        }

        // Kiểm tra trạng thái phòng
        if (room.getStatus() != Room.RoomStatus.AVAILABLE) {
            throw new NotFoundException("RoomNotAvailable", "Room with ID " + room.getRoomId() + " is not available.");
        }

        // Thêm booking detail mới vào booking hiện tại
        BookingDetail bookingDetail = new BookingDetail();
        bookingDetail.setRoom(room);
        bookingDetail.setBooking(booking);
        bookingDetail.setCreatedAt(LocalDateTime.now());
        bookingDetail.setCheckIn(bookingDetailRequest.getCheckIn());
        bookingDetail.setCheckOut(bookingDetailRequest.getCheckOut());
        bookingDetail.setSpecialRequests(bookingDetailRequest.getSpecialRequests());
        bookingDetail.setStatus(BookingDetail.BookingDetailStatus.PENDING);// Set status là PENDING cho booking detail

        bookingDetailRepository.save(bookingDetail);

        if (bookingDetailRequest.getProvisionIds() != null && !bookingDetailRequest.getProvisionIds().isEmpty()) {
            List<Provision> provisions = provisionRepository.findAllByProvisionIdIn(bookingDetailRequest.getProvisionIds());
            for (Provision provision : provisions) {
                RelProvisionBookingDetail relProvisionBookingDetail = new RelProvisionBookingDetail();
                relProvisionBookingDetail.setProvision(provision);
                relProvisionBookingDetail.setBookingDetail(bookingDetail);
                relProvisionBookingDetail.setPrice(provision.getPrice().doubleValue()); // Lưu giá của proviiosn
                relProvisionBookingDetail.setCreatedAt(LocalDateTime.now());  // Gán thời gian hiện tại cho createdAt
                relProvisionBookingDetail.setStatus(RelProvisionBookingDetail.RelProvisionBookingDetailStatus.UNUSED);
                // Gắn các RelProvisionBookingDetail vào bookingDetail
                relProvisionBookingDetailRepository.save(relProvisionBookingDetail);
            }
        }

        // Cập nhật trạng thái phòng thành BOOKED
        room.setStatus(Room.RoomStatus.BOOKED);
        roomRepository.save(room); // Lưu phòng sau khi thay đổi trạng thái

        booking.getBookingDetails().add(bookingDetail);

        // Cập nhật tổng số tiền cho booking
        booking.calculateTotalAmount(); // Tính lại totalAmount và deposit

        // Lưu lại booking sau khi tính toán
        return bookingRepository.save(booking);
    }



    @Override
    public Booking updateBooking(Long bookingId, Booking booking) {
        // Tìm kiếm booking theo bookingId
        Booking bookingUpdate = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("BookingNotFound", "Booking not found with ID: " + bookingId));

        // Kiểm tra nếu trạng thái booking được cập nhật thành FAILED
        if (booking.getStatus() == Booking.BookingStatus.FAILED) {
            for (BookingDetail bookingDetail : bookingUpdate.getBookingDetails()) {
                bookingDetail.setStatus(BookingDetail.BookingDetailStatus.CANCELED);
                bookingDetail.setUpdatedAt(LocalDateTime.now());

                if (bookingDetail.getRelProvisionBookingDetails() != null) {
                    for (RelProvisionBookingDetail relProvisionBookingDetail : bookingDetail.getRelProvisionBookingDetails()) {
                        relProvisionBookingDetail.setStatus(RelProvisionBookingDetail.RelProvisionBookingDetailStatus.UNUSED);
                        relProvisionBookingDetail.setUpdatedAt(LocalDateTime.now());
                    }

                    // Lưu lại các thay đổi của RelProvisionBookingDetail
                    relProvisionBookingDetailRepository.saveAll(bookingDetail.getRelProvisionBookingDetails());
                }

                Room room = bookingDetail.getRoom();
                if (room != null && room.getStatus() != Room.RoomStatus.AVAILABLE) {
                    room.setStatus(Room.RoomStatus.AVAILABLE);
                    roomRepository.save(room); // Lưu lại phòng sau khi cập nhật trạng thái
                }
            }
        }

        // Kiểm tra nếu trạng thái booking được cập nhật thành PAID hoặc DEPOSITED
        if (booking.getStatus() == Booking.BookingStatus.PAID || booking.getStatus() == Booking.BookingStatus.DEPOSITED) {
            for (BookingDetail bookingDetail : bookingUpdate.getBookingDetails()) {
                // Cập nhật trạng thái bookingDetail thành CONFIRMED
                if (bookingDetail.getStatus() != BookingDetail.BookingDetailStatus.CONFIRMED) {
                    bookingDetail.setStatus(BookingDetail.BookingDetailStatus.CONFIRMED);
                    bookingDetail.setUpdatedAt(LocalDateTime.now());
                    bookingDetailRepository.save(bookingDetail);  // Lưu trạng thái đã thay đổi
                }
            }
        }

        // Kiểm tra nếu trạng thái booking được cập nhật thành COMPLETED
        if (booking.getStatus() == Booking.BookingStatus.COMPLETED) {
            for (BookingDetail bookingDetail : bookingUpdate.getBookingDetails()) {
                // Cập nhật trạng thái bookingDetail thành COMPLETED
                if (bookingDetail.getStatus() != BookingDetail.BookingDetailStatus.COMPLETED) {
                    bookingDetail.setStatus(BookingDetail.BookingDetailStatus.COMPLETED);
                    bookingDetail.setUpdatedAt(LocalDateTime.now());
                    bookingDetailRepository.save(bookingDetail);  // Lưu trạng thái đã thay đổi
                }

                Room room = bookingDetail.getRoom();
                if (room != null && room.getStatus() != Room.RoomStatus.AVAILABLE) {
                    room.setStatus(Room.RoomStatus.AVAILABLE);
                    roomRepository.save(room); // Lưu lại phòng sau khi cập nhật trạng thái
                }
            }
        }

        // Cập nhật thông tin booking
        setBooking(bookingUpdate, booking);

        // Tính lại totalAmount và deposit sau khi cập nhật
        bookingUpdate.calculateTotalAmount();

        // Lưu booking đã cập nhật vào cơ sở dữ liệu
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

    private void setBooking(Booking bookingUpdate, Booking booking) {
        bookingUpdate.setStatus(booking.getStatus());
        bookingUpdate.setDeposit(booking.getDeposit());
        bookingUpdate.setTotalAmount(booking.getTotalAmount());
        bookingUpdate.setUpdatedAt(LocalDateTime.now());
    }
    public static Specification<Booking> searchByKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + keyword + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("status").as(String.class)), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("deposit").as(String.class)), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("totalAmount").as(String.class)), likePattern)
            );
        };
    }
}
