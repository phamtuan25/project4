package com.example.projectbackend.repository;

import com.example.projectbackend.entity.BookingDetail;
import com.example.projectbackend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingDetailRepository extends JpaRepository<BookingDetail, Long>, JpaSpecificationExecutor<BookingDetail> {
    List<BookingDetail> findByStatusAndCheckOutBefore(BookingDetail.BookingDetailStatus status, LocalDateTime checkOut);
    @Query("SELECT bd FROM BookingDetail bd WHERE bd.room.roomId = :roomId AND (" +
            "(bd.checkIn < :checkOut AND bd.checkOut > :checkIn))")
    List<BookingDetail> findOverlappingBookings(@Param("roomId") Long roomId,
                                                @Param("checkIn") LocalDateTime checkIn,
                                                @Param("checkOut") LocalDateTime checkOut);
    List<BookingDetail> findByRoom(Room room);
}

