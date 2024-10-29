import { Component, OnInit } from '@angular/core';
import { AdminComponent } from '../admin.component';
import $ from 'jquery';
import 'bootstrap';
@Component({
  selector: 'app-booking-manager',
  templateUrl: './booking-manager.component.html',
  styleUrl: './booking-manager.component.css'
})
export class BookingManagerComponent implements OnInit {
  constructor(public admin: AdminComponent) { }
  ngOnInit(): void {
    this.admin.pageTitle = 'Quản lý Đặt phòng';
  }
}

export interface Booking {
  id: string; // ID của đặt phòng
  customerName: string; // Tên khách hàng
  roomBooked: string; // Phòng đã đặt
  checkIn: string; // Ngày nhận phòng
  checkOut: string; // Ngày trả phòng
  numberOfPeople: string; // Số người
  additionalServices: string; // Dịch vụ bổ sung
  totalAmount: string; // Tổng tiền
  bookingStatus: string; // Trạng thái đặt phòng
}

const bookings: Booking[] = []; // Danh sách đặt phòng (giả sử đã định nghĩa)

function renderBookings(filteredBookings: Booking[]): void {
  // Logic để hiển thị danh sách đặt phòng
}

function searchBookings(): void {
  const input: string = (document.getElementById('searchInput') as HTMLInputElement).value.toLowerCase();
  const filteredBookings: Booking[] = bookings.filter(booking => {
      return booking.customerName.toLowerCase().includes(input) ||
          booking.roomBooked.toLowerCase().includes(input) ||
          booking.bookingStatus.toLowerCase().includes(input);
  });

  renderBookings(filteredBookings);
}

function handleKeyPress(event: KeyboardEvent): void {
  if (event.key === 'Enter') {
      searchBookings();
  }
}

function submitBooking(): void {
  const customerName: string = (document.getElementById('customerName') as HTMLInputElement).value.trim();
  const roomBooked: string = (document.getElementById('roomBooked') as HTMLInputElement).value.trim();
  const checkIn: string = (document.getElementById('checkIn') as HTMLInputElement).value;
  const checkOut: string = (document.getElementById('checkOut') as HTMLInputElement).value;
  const numberOfPeople: string = (document.getElementById('numberOfPeople') as HTMLInputElement).value.trim();
  const additionalServices: string = (document.getElementById('additionalServices') as HTMLInputElement).value.trim();
  const totalAmount: string = (document.getElementById('totalAmount') as HTMLInputElement).value.trim();
  const bookingStatus: string = (document.getElementById('bookingStatus') as HTMLInputElement).value;

  if (!customerName || !roomBooked || !checkIn || !checkOut || !numberOfPeople || !additionalServices || !totalAmount) {
      alert("Vui lòng điền đầy đủ thông tin!");
      return;
  }

  alert(`Đã thêm đặt phòng cho khách: ${customerName}, Tổng Tiền: ${totalAmount}, Trạng Thái: ${bookingStatus}`);

  $('#addBookingModal').modal('hide');
  (document.getElementById('addBookingForm') as HTMLFormElement).reset();
}

function openEditBookingModal(
  id: string,
  customerName: string,
  roomBooked: string,
  checkIn: string,
  checkOut: string,
  numberOfPeople: string,
  additionalServices: string,
  totalAmount: string,
  bookingStatus: string
): void {
  (document.getElementById('editBookingId') as HTMLInputElement).value = id;
  (document.getElementById('editCustomerName') as HTMLInputElement).value = customerName;
  (document.getElementById('editRoomBooked') as HTMLInputElement).value = roomBooked;
  (document.getElementById('editCheckIn') as HTMLInputElement).value = checkIn;
  (document.getElementById('editCheckOut') as HTMLInputElement).value = checkOut;
  (document.getElementById('editNumberOfPeople') as HTMLInputElement).value = numberOfPeople;
  (document.getElementById('editAdditionalServices') as HTMLInputElement).value = additionalServices;
  (document.getElementById('editTotalAmount') as HTMLInputElement).value = totalAmount;
  (document.getElementById('editBookingStatus') as HTMLInputElement).value = bookingStatus;
  $('#editBookingModal').modal('show');
}

function submitEditBooking(): void {
  const id: string = (document.getElementById('editBookingId') as HTMLInputElement).value;
  const customerName: string = (document.getElementById('editCustomerName') as HTMLInputElement).value.trim();
  const roomBooked: string = (document.getElementById('editRoomBooked') as HTMLInputElement).value.trim();
  const checkIn: string = (document.getElementById('editCheckIn') as HTMLInputElement).value;
  const checkOut: string = (document.getElementById('editCheckOut') as HTMLInputElement).value;
  const numberOfPeople: string = (document.getElementById('editNumberOfPeople') as HTMLInputElement).value.trim();
  const additionalServices: string = (document.getElementById('editAdditionalServices') as HTMLInputElement).value.trim();
  const totalAmount: string = (document.getElementById('editTotalAmount') as HTMLInputElement).value.trim();
  const bookingStatus: string = (document.getElementById('editBookingStatus') as HTMLInputElement).value;

  if (!customerName || !roomBooked || !checkIn || !checkOut || !numberOfPeople || !additionalServices || !totalAmount) {
      alert("Vui lòng điền đầy đủ thông tin!");
      return;
  }

  alert(`Đã sửa đặt phòng: ID: ${id}, Khách Hàng: ${customerName}, Tổng Tiền: ${totalAmount}, Trạng Thái: ${bookingStatus}`);

  $('#editBookingModal').modal('hide');
  (document.getElementById('editBookingForm') as HTMLFormElement).reset();
}

function deleteBooking(id: string): void {
  if (confirm("Bạn có chắc chắn muốn xóa đặt phòng với ID: " + id + "?")) {
      alert("Đặt phòng với ID " + id + " đã bị xóa!");
      // Thêm logic để xóa đặt phòng từ bảng
  }
}

function signOut(): void {
  if (confirm("Bạn có chắc chắn muốn đăng xuất?")) {
      // Logic để đăng xuất
      alert("Đã đăng xuất!");
      window.location.href = 'login.html'; // Điều hướng đến trang đăng nhập
  }
}



