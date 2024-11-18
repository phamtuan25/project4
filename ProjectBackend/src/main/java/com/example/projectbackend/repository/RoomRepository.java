package com.example.projectbackend.repository;

import com.example.projectbackend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;


@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
    Optional<Room> findByRoomNumber(String roomNumber);
    List<Room> findAllByRoomNumberIn(List<String> roomNumbers);
    Optional<Room> findRoomByRoomId(Long roomId);
    List<Room> findByStatus(Room.RoomStatus status);
    List<Room> findByRoomType(Room.RoomType roomType);
}
