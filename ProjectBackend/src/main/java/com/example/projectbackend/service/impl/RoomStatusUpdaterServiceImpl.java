package com.example.projectbackend.service.impl;

import com.example.projectbackend.entity.BookingDetail;
import com.example.projectbackend.entity.Room;
import com.example.projectbackend.repository.BookingDetailRepository;
import com.example.projectbackend.repository.RoomRepository;
import com.example.projectbackend.service.RoomStatusUpdaterService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoomStatusUpdaterServiceImpl implements RoomStatusUpdaterService {

    private final RoomRepository roomRepository;
    private final BookingDetailRepository bookingDetailRepository;

    public RoomStatusUpdaterServiceImpl(RoomRepository roomRepository, BookingDetailRepository bookingDetailRepository) {
        this.roomRepository = roomRepository;
        this.bookingDetailRepository = bookingDetailRepository;
    }

    // Phương thức này sẽ tự động được gọi theo lịch trình (ví dụ: mỗi ngày một lần)
    @Scheduled(cron = "0 * * * * ?")
    @Override
    public void updateRoomStatus() {
        LocalDateTime now = LocalDateTime.now();

        // Tìm tất cả các BookingDetail có checkOut < thời gian hiện tại và trạng thái là BOOKED
        List<BookingDetail> bookingDetails = bookingDetailRepository.findByStatusAndCheckOutBefore(BookingDetail.BookingDetailStatus.CONFIRMED, now);

        for (BookingDetail bookingDetail : bookingDetails) {
            // Cập nhật trạng thái phòng từ BOOKED thành AVAILABLE
            Room room = bookingDetail.getRoom();
            if (room.getStatus() == Room.RoomStatus.BOOKED) {
                room.setStatus(Room.RoomStatus.AVAILABLE);
                roomRepository.save(room);  // Lưu lại trạng thái phòng
            }
        }
    }
}
