package com.example.projectbackend.repository;

import com.example.projectbackend.entity.Booking;
import com.example.projectbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {
    Booking findFirstByUserAndStatus(User user, Booking.BookingStatus status);
    List<Booking> findByUser_UserId(Long userId);
}
