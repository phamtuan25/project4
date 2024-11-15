package com.example.projectbackend.entity;

import com.example.projectbackend.bean.request.BookingDetailRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "bookings")
public class Booking {
    public enum BookingStatus {
        PENDING,
        COMPLETED,
        FAILED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<BookingDetail> bookingDetails;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status;

    @Column(name = "deposit")
    private Double deposit;

    @Column(name = "total_amount")
    private Double totalAmount;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        // Tính toán tổng tiền (totalAmount) khi booking được tạo
        calculateTotalAmount();
    }

    private double calculateRoomPrice(BookingDetail bookingDetail) {
        Room room = bookingDetail.getRoom();
        LocalDateTime checkIn = bookingDetail.getCheckIn();
        LocalDateTime checkOut = bookingDetail.getCheckOut();

        // Kiểm tra giá trị của isHourly, nếu là null thì mặc định là false (tính giá theo ngày)
        Boolean isHourly = room.getIsHourly();
        if (isHourly == null) {
            isHourly = Boolean.FALSE;  // Giá trị mặc định là false (tính giá theo ngày)
        }

        double roomPrice;

        // Nếu phòng tính giá theo giờ
        if (isHourly) {
            // Tính số phút giữa checkIn và checkOut
            long minutes = Duration.between(checkIn, checkOut).toMinutes();

            // Nếu số phút > 0, chúng ta cần làm tròn lên thành giờ tiếp theo
            long hours = (minutes + 59) / 60;  // Làm tròn lên giờ tiếp theo nếu có phút dư

            // Tính giá phòng theo số giờ
            roomPrice = room.getHourPrice() * hours;
        } else {
            // Nếu phòng tính giá theo ngày
            // Tính số ngày giữa checkIn và checkOut
            long days = Duration.between(checkIn, checkOut).toDays();

            // Nếu số ngày < 1, tính 1 ngày
            days = Math.max(days, 1);

            // Tính giá phòng theo số ngày
            roomPrice = room.getDayPrice() * days;
        }

        // Trả về giá phòng tính được
        return roomPrice;
    }

    public void calculateTotalAmount() {
        double totalAmount = 0;

        // Duyệt qua từng BookingDetail để tính tổng giá trị
        for (BookingDetail bookingDetail : bookingDetails) {
            // Tính giá của từng bookingDetail = giá phòng + các provision liên quan
            double roomPrice = calculateRoomPrice(bookingDetail);

            double provisionsPrice = 0;

            // Tính tổng giá trị của provisions liên quan đến bookingDetail
            if (bookingDetail.getRelProvisionBookingDetails() != null) {
                for (RelProvisionBookingDetail rel : bookingDetail.getRelProvisionBookingDetails()) {
                    provisionsPrice += rel.getProvision().getPrice().doubleValue();
                }
            }

            // Cộng giá phòng và giá các provision vào tổng số tiền cho booking
            totalAmount += roomPrice + provisionsPrice;
        }

        // Gán tổng tiền vào thuộc tính totalAmount của Booking
        this.totalAmount = totalAmount;

        // Tính deposit (10% của tổng số tiền)
        this.deposit = totalAmount * 0.10;
    }


}

