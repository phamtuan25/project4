import { Component, OnInit } from '@angular/core';
import { AdminComponent } from '../admin.component';
import $ from 'jquery';
import 'bootstrap';
@Component({
  selector: 'app-user-manager',
  templateUrl: './user-manager.component.html',
  styleUrl: './user-manager.component.css'
})
export class UserManagerComponent implements OnInit {
  constructor(public admin: AdminComponent){}
  ngOnInit(): void {
    this.admin.pageTitle = 'Quản lý Người dùng';
  }
}

interface User {
  id: string; // ID của người dùng
  name: string; // Tên người dùng
  address: string; // Địa chỉ người dùng
  phone: string; // Số điện thoại người dùng
  role: string; // Vai trò của người dùng
  email: string; // Email của người dùng
}

const users: User[] = []; // Giả sử danh sách người dùng đã được định nghĩa

function renderUsers(filteredUsers: User[]): void {
  // Triển khai logic hiển thị danh sách người dùng ở đây
}

function searchUsers(): void {
  const input: string = (document.getElementById('searchUserInput') as HTMLInputElement).value.toLowerCase();
  const filteredUsers: User[] = users.filter(user => {
      return user.name.toLowerCase().includes(input) ||
          user.address.toLowerCase().includes(input) ||
          user.phone.toLowerCase().includes(input) ||
          user.role.toLowerCase().includes(input) ||
          user.email.toLowerCase().includes(input);
  });

  renderUsers(filteredUsers);
}

function submitRole(): void {
  const roleName: string = (document.getElementById('roleName') as HTMLInputElement).value.trim();

  if (!roleName) {
      alert("Vui lòng điền đầy đủ thông tin!");
      return;
  }

  alert(`Đã thêm vai trò: ${roleName}`);
  $('#addRoleModal').modal('hide');
  (document.getElementById('addRoleForm') as HTMLFormElement).reset();
}

function submitUser(): void {
  const userName: string = (document.getElementById('userName') as HTMLInputElement).value.trim();
  const userAddress: string = (document.getElementById('userAddress') as HTMLInputElement).value.trim();
  const userPhone: string = (document.getElementById('userPhone') as HTMLInputElement).value.trim();
  const userRole: string = (document.getElementById('userRole') as HTMLInputElement).value.trim();
  const userEmail: string = (document.getElementById('userEmail') as HTMLInputElement).value.trim();

  if (!userName || !userAddress || !userPhone || !userRole || !userEmail) {
      alert("Vui lòng điền đầy đủ thông tin!");
      return;
  }

  alert(`Đã thêm người dùng: ${userName}, Vai trò: ${userRole}`);
  $('#addUserModal').modal('hide');
  (document.getElementById('addUserForm') as HTMLFormElement).reset();
}

function openEditUserModal(id: string, name: string, address: string, phone: string, role: string, email: string): void {
  (document.getElementById('editUserId') as HTMLInputElement).value = id;
  (document.getElementById('editUserName') as HTMLInputElement).value = name;
  (document.getElementById('editUserAddress') as HTMLInputElement).value = address;
  (document.getElementById('editUserPhone') as HTMLInputElement).value = phone;
  (document.getElementById('editUserRole') as HTMLInputElement).value = role;
  (document.getElementById('editUserEmail') as HTMLInputElement).value = email;
  $('#editUserModal').modal('show');
}

function submitEditUser(): void {
  const id: string = (document.getElementById('editUserId') as HTMLInputElement).value;
  const userName: string = (document.getElementById('editUserName') as HTMLInputElement).value.trim();
  const userAddress: string = (document.getElementById('editUserAddress') as HTMLInputElement).value.trim();
  const userPhone: string = (document.getElementById('editUserPhone') as HTMLInputElement).value.trim();
  const userRole: string = (document.getElementById('editUserRole') as HTMLInputElement).value.trim();
  const userEmail: string = (document.getElementById('editUserEmail') as HTMLInputElement).value.trim();

  if (!userName || !userAddress || !userPhone || !userRole || !userEmail) {
      alert("Vui lòng điền đầy đủ thông tin!");
      return;
  }

  alert(`Đã sửa người dùng: ID: ${id}, Tên: ${userName}, Vai trò: ${userRole}`);
  $('#editUserModal').modal('hide');
  (document.getElementById('editUserForm') as HTMLFormElement).reset();
}

function deleteUser(id: string): void {
  if (confirm(`Bạn có chắc chắn muốn xóa người dùng với ID: ${id}?`)) {
      alert(`Người dùng với ID ${id} đã bị xóa!`);
      // Thêm logic để xóa người dùng từ bảng
  }
}



