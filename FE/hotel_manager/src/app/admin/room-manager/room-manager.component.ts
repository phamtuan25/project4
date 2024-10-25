import { Component, OnInit } from '@angular/core';
import { AdminComponent } from '../admin.component';
import $ from 'jquery';
import 'bootstrap';
@Component({
  selector: 'app-room-manager',
  templateUrl: './room-manager.component.html',
  styleUrl: './room-manager.component.css'
})
export class RoomManagerComponent implements OnInit {
  constructor(public admin: AdminComponent){}
  ngOnInit(): void {
    this.admin.pageTitle = 'Quản lý Phòng';
  }
}

interface Room {
  id: string; // ID của phòng
  roomName: string; // Tên phòng
  roomImage: string; // Hình ảnh phòng
  roomDescription: string; // Mô tả phòng
  roomStatus: string; // Trạng thái phòng
  roomPrice: string; // Giá phòng
  roomType: string; // Loại phòng
}

const rooms: Room[] = []; // Giả sử danh sách phòng đã được định nghĩa

function renderRooms(filteredRooms: Room[]): void {
  // Triển khai logic hiển thị danh sách phòng ở đây
}

function searchRooms(): void {
  const input: string = (document.getElementById('searchInput') as HTMLInputElement).value.toLowerCase();
  const filteredRooms: Room[] = rooms.filter(room => {
      return room.roomName.toLowerCase().includes(input) ||
             room.roomStatus.toLowerCase().includes(input);
  });

  renderRooms(filteredRooms);
}

function submitRoom(): void {
  const roomName: string = (document.getElementById('roomName') as HTMLInputElement).value.trim();
  const roomImage: string = (document.getElementById('roomImage') as HTMLInputElement).value.trim();
  const roomDescription: string = (document.getElementById('roomDescription') as HTMLInputElement).value.trim();
  const roomStatus: string = (document.getElementById('roomStatus') as HTMLSelectElement).value;
  const roomPrice: string = (document.getElementById('roomPrice') as HTMLInputElement).value.trim();
  const roomType: string = (document.getElementById('roomType') as HTMLInputElement).value.trim();

  if (!roomName || !roomImage || !roomDescription || !roomStatus || !roomPrice || !roomType) {
      alert("Vui lòng điền đầy đủ thông tin!");
      return;
  }

  alert(`Đã thêm phòng: ${roomName}, Giá: ${roomPrice}, Trạng Thái: ${roomStatus}`);
  $('#addRoomModal').modal('hide');
  (document.getElementById('addRoomForm') as HTMLFormElement).reset();
}

function openEditRoomModal(id: string, roomName: string, roomImage: string, roomDescription: string, roomStatus: string, roomPrice: string, roomType: string): void {
  (document.getElementById('editRoomId') as HTMLInputElement).value = id;
  (document.getElementById('editRoomName') as HTMLInputElement).value = roomName;
  (document.getElementById('editRoomImage') as HTMLInputElement).value = roomImage;
  (document.getElementById('editRoomDescription') as HTMLInputElement).value = roomDescription;
  (document.getElementById('editRoomStatus') as HTMLSelectElement).value = roomStatus;
  (document.getElementById('editRoomPrice') as HTMLInputElement).value = roomPrice;
  (document.getElementById('editRoomType') as HTMLInputElement).value = roomType;
  $('#editRoomModal').modal('show');
}

function submitEditRoom(): void {
  const id: string = (document.getElementById('editRoomId') as HTMLInputElement).value;
  const roomName: string = (document.getElementById('editRoomName') as HTMLInputElement).value.trim();
  const roomImage: string = (document.getElementById('editRoomImage') as HTMLInputElement).value.trim();
  const roomDescription: string = (document.getElementById('editRoomDescription') as HTMLInputElement).value.trim();
  const roomStatus: string = (document.getElementById('editRoomStatus') as HTMLSelectElement).value;
  const roomPrice: string = (document.getElementById('editRoomPrice') as HTMLInputElement).value.trim();
  const roomType: string = (document.getElementById('editRoomType') as HTMLInputElement).value.trim();

  if (!roomName || !roomImage || !roomDescription || !roomStatus || !roomPrice || !roomType) {
      alert("Vui lòng điền đầy đủ thông tin!");
      return;
  }

  alert(`Đã sửa phòng: ID: ${id}, Tên: ${roomName}, Giá: ${roomPrice}, Trạng Thái: ${roomStatus}`);
  $('#editRoomModal').modal('hide');
  (document.getElementById('editRoomForm') as HTMLFormElement).reset();
}

function deleteRoom(id: string): void {
  if (confirm(`Bạn có chắc chắn muốn xóa phòng với ID: ${id}?`)) {
      alert(`Phòng với ID ${id} đã bị xóa!`);
      // Thêm logic để xóa phòng từ bảng
  }
}

function signOut(): void {
  if (confirm("Bạn có chắc chắn muốn đăng xuất?")) {
      alert("Đã đăng xuất!");
      window.location.href = 'login.html'; // Điều hướng đến trang đăng nhập
  }
}

