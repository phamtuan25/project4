package com.example.projectbackend.service.impl;

import com.example.projectbackend.bean.request.RoomRequest;
import com.example.projectbackend.bean.response.ImageResponse;
import com.example.projectbackend.bean.response.RoomResponse;
import com.example.projectbackend.entity.BookingDetail;
import com.example.projectbackend.entity.Room;
import com.example.projectbackend.exception.NotFoundException;
import com.example.projectbackend.mapper.ImageMapper;
import com.example.projectbackend.mapper.RoomMapper;
import com.example.projectbackend.repository.BookingDetailRepository;
import com.example.projectbackend.repository.ImageRepository;
import com.example.projectbackend.repository.RoomRepository;
import com.example.projectbackend.service.RoomService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final ImageRepository imageRepository;
    private final BookingDetailRepository bookingDetailRepository;

    @Override
    public Page<RoomResponse> getAllRooms(@PageableDefault(size = 10, page = 0) Pageable pageable,
                                          @RequestParam(required = false) String keyword,
                                          @RequestParam(required = false) String status,
                                          @RequestParam(required = false) String roomType) {

        // Tạo Specification với các tham số lọc (status, roomType, keyword)
        Specification<Room> spec = searchByKeywordStatusAndRoomType(keyword, status, roomType);

        // Lấy danh sách phòng với phân trang
        Page<Room> roomsPage = roomRepository.findAll(spec, pageable);

        // Chuyển đổi danh sách phòng thành danh sách RoomResponse
        return roomsPage.map(room -> {
            RoomResponse roomResponse = RoomMapper.convertToResponse(room);

            // Lấy danh sách hình ảnh cho từng phòng
            List<String> images = imageRepository.findAllByNameAndReferenceId("ROOM", room.getRoomId())
                    .stream()
                    .map(ImageMapper::convertToResponse)
                    .map(ImageResponse::getImageFileName)
                    .collect(Collectors.toList());

            roomResponse.setImages(images);
            return roomResponse;
        });
    }


    @Override
    public RoomResponse getDetailRoom(Long roomId) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if (Objects.isNull(room)) {
            throw new NotFoundException("RoomNotFound", "Not found Room with " + roomId);
        }
        RoomResponse roomResponse = RoomMapper.convertToResponse(room);
        List<String> images = imageRepository.findAllByNameAndReferenceId("ROOM", room.getRoomId())
                .stream()
                .map(ImageMapper::convertToResponse)
                .map(ImageResponse::getImageFileName)
                .collect(Collectors.toList());
        roomResponse.setImages(images);
        return roomResponse;
    }


    @Override
    public Room createRoom(RoomRequest roomRequest) {
        Room room = RoomMapper.convertFromRequest(roomRequest);
        return roomRepository.save(room);
    }

    @Override
    public Room updateRoom(Long roomId, RoomRequest roomRequest) {
        // Find the existing room by ID and update its fields
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        room.setRoomNumber(roomRequest.getRoomNumber());
        room.setRoomType(roomRequest.getRoomType());
        room.setStatus(roomRequest.getStatus());
        // Cập nhật các trường khác...

        // Kiểm tra và cập nhật trạng thái phòng nếu cần thiết
        updateRoomStatusIfNecessary(room, LocalDateTime.now()); // Hoặc dùng thời gian check-in cụ thể nếu cần

        return roomRepository.save(room);
    }


    @Override
    public void deleteRoom(Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);

        if (room.isEmpty()) {
            throw new NotFoundException("RoomNotFound", "Room with ID " + roomId + " not found.");
        }
        roomRepository.deleteById(roomId);
    }



    public void setRoom(Room roomUpdate, RoomRequest roomInput) {
        roomUpdate.setRoomNumber(roomInput.getRoomNumber());
        roomUpdate.setRoomType(roomInput.getRoomType());
        roomUpdate.setStatus(roomInput.getStatus());
        roomUpdate.setHourPrice(roomInput.getHourPrice());
        roomUpdate.setDayPrice(roomInput.getDayPrice());
    }

    public static Specification<Room> searchByKeywordStatusAndRoomType(String keyword, String status, String roomType) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isEmpty()) {
                String likePattern = "%" + keyword.toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("roomNumber")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("roomType")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("status")), likePattern)
                ));
            }

            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("status")), status.toLowerCase()));
            }
            if (roomType != null) {
                predicates.add(criteriaBuilder.equal(root.get("roomType"), roomType));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }



    @Override
    public List<Room> findAvailableRooms(LocalDateTime checkIn, LocalDateTime checkOut, Room.RoomType roomType) {
        // Lấy danh sách phòng theo loại phòng (RoomType)
        List<Room> rooms = roomRepository.findByRoomType(roomType);  // Truyền trực tiếp roomType enum

        // Duyệt qua từng phòng để kiểm tra tính khả dụng
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            // Nếu phòng có trạng thái MAINTENANCE, không hiển thị
            if (room.getStatus() == Room.RoomStatus.MAINTENANCE) {
                continue;  // Bỏ qua phòng này
            }

            // Kiểm tra nếu phòng có trạng thái AVAILABLE
            if (room.getStatus() == Room.RoomStatus.AVAILABLE) {
                availableRooms.add(room); // Nếu trạng thái là AVAILABLE, thêm vào danh sách
                continue; // Tiếp tục vòng lặp
            }

            // Kiểm tra nếu phòng có trạng thái BOOKED
            if (room.getStatus() == Room.RoomStatus.BOOKED) {
                // Lấy danh sách BookingDetail của phòng và sắp xếp theo bookingDetailId giảm dần
                List<BookingDetail> bookingDetails = bookingDetailRepository.findByRoom(room);
                bookingDetails.sort((bd1, bd2) -> bd2.getBookingDetailId().compareTo(bd1.getBookingDetailId())); // Sắp xếp giảm dần theo bookingDetailId

                // Kiểm tra booking mới nhất
                for (BookingDetail bookingDetail : bookingDetails) {
                    if (bookingDetail.getStatus() != BookingDetail.BookingDetailStatus.CANCELED) {
                        // Nếu thời gian checkIn tìm kiếm sau thời gian checkOut của phòng và checkOut đã qua
                        if (checkIn.isAfter(bookingDetail.getCheckOut()) && bookingDetail.getCheckOut().isBefore(LocalDateTime.now())) {
                            // Cập nhật trạng thái phòng thành AVAILABLE nếu checkOut đã qua
                            room.setStatus(Room.RoomStatus.AVAILABLE);
                            roomRepository.save(room); // Cập nhật trong cơ sở dữ liệu
                            availableRooms.add(room);  // Thêm vào danh sách phòng trống
                        } else if (checkIn.isAfter(bookingDetail.getCheckOut()) && bookingDetail.getCheckOut().isAfter(LocalDateTime.now())) {
                            // Nếu checkIn sau checkOut nhưng thời gian checkOut chưa qua, vẫn hiển thị phòng như là BOOKED
                            availableRooms.add(room);  // Thêm vào danh sách dù trạng thái là BOOKED
                        }
                        break;  // Chỉ cần kiểm tra booking đầu tiên (mới nhất)
                    }
                }
            }
        }

        return availableRooms;  // Trả về danh sách phòng trống hoặc phòng đang được đặt nhưng vẫn có thể hiển thị
    }







    public boolean isRoomAvailable(Room room, LocalDateTime checkIn, LocalDateTime checkOut) {
        List<BookingDetail> bookingDetails = bookingDetailRepository.findByRoom(room);

        for (BookingDetail bookingDetail : bookingDetails) {
            if (bookingDetail.getStatus() != BookingDetail.BookingDetailStatus.CANCELED) {
                LocalDateTime bookingCheckIn = bookingDetail.getCheckIn();
                LocalDateTime bookingCheckOut = bookingDetail.getCheckOut();

                if ((checkIn.isBefore(bookingCheckOut) && checkOut.isAfter(bookingCheckIn))) {
                    return false;
                }
            }
        }

        return true;
    }

    public void updateRoomStatusIfNecessary(Room room, LocalDateTime checkIn) {
        // Kiểm tra booking detail xem phòng có booking nào với thời gian checkOut sớm hơn checkIn không
        List<BookingDetail> bookingDetails = bookingDetailRepository.findByRoom(room);
        for (BookingDetail bookingDetail : bookingDetails) {
            if (bookingDetail.getStatus() != BookingDetail.BookingDetailStatus.CANCELED) {
                // Nếu thời gian checkOut của booking trước thời gian checkIn yêu cầu, chúng ta có thể cập nhật trạng thái phòng thành AVAILABLE
                if (bookingDetail.getCheckOut().isBefore(checkIn)) {
                    room.setStatus(Room.RoomStatus.AVAILABLE); // Cập nhật trạng thái bằng RoomStatus enum
                    roomRepository.save(room);
                    break; // Không cần kiểm tra booking tiếp theo nữa
                }
            }
        }
    }





}
