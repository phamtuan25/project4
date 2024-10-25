import { Component, OnInit } from '@angular/core';
import { AdminComponent } from '../admin.component';
import $ from 'jquery';
import 'bootstrap';
@Component({
  selector: 'app-service-manager',
  templateUrl: './service-manager.component.html',
  styleUrl: './service-manager.component.css'
})
export class ServiceManagerComponent implements OnInit {
  constructor(public admin: AdminComponent){}
  ngOnInit(): void {
    this.admin.pageTitle = 'Quản lý Dịch Vụ';
  }
}

interface Service {
  id: string; // ID của dịch vụ
  serviceName: string; // Tên dịch vụ
  serviceDetails: string; // Chi tiết dịch vụ
  servicePrice: string; // Giá dịch vụ
  createdDate?: string; // Ngày tạo (tuỳ chọn)
  updatedDate?: string; // Ngày cập nhật (tuỳ chọn)
}

const services: Service[] = []; // Giả sử danh sách dịch vụ đã được định nghĩa

function renderServices(filteredServices: Service[]): void {
  // Triển khai logic hiển thị danh sách dịch vụ ở đây
}

function searchServices(): void {
  const input: string = (document.getElementById('searchServiceInput') as HTMLInputElement).value.toLowerCase();
  const filteredServices: Service[] = services.filter(service => {
      return service.serviceName.toLowerCase().includes(input) ||
          service.serviceDetails.toLowerCase().includes(input);
  });

  renderServices(filteredServices);
}

function submitService(): void {
  const serviceName: string = (document.getElementById('serviceName') as HTMLInputElement).value.trim();
  const serviceDetails: string = (document.getElementById('serviceDetails') as HTMLInputElement).value.trim();
  const servicePrice: string = (document.getElementById('servicePrice') as HTMLInputElement).value.trim();

  if (!serviceName || !serviceDetails || !servicePrice) {
      alert("Vui lòng điền đầy đủ thông tin!");
      return;
  }

  alert(`Đã thêm dịch vụ: ${serviceName}, Giá: ${servicePrice}`);
  $('#addServiceModal').modal('hide');
  (document.getElementById('addServiceForm') as HTMLFormElement).reset();
}

function openEditServiceModal(id: string, serviceName: string, serviceDetails: string, servicePrice: string): void {
  (document.getElementById('editServiceId') as HTMLInputElement).value = id;
  (document.getElementById('editServiceName') as HTMLInputElement).value = serviceName;
  (document.getElementById('editServiceDetails') as HTMLInputElement).value = serviceDetails;
  (document.getElementById('editServicePrice') as HTMLInputElement).value = servicePrice;
  $('#editServiceModal').modal('show');
}

function submitEditService(): void {
  const id: string = (document.getElementById('editServiceId') as HTMLInputElement).value;
  const serviceName: string = (document.getElementById('editServiceName') as HTMLInputElement).value.trim();
  const serviceDetails: string = (document.getElementById('editServiceDetails') as HTMLInputElement).value.trim();
  const servicePrice: string = (document.getElementById('editServicePrice') as HTMLInputElement).value.trim();

  if (!serviceName || !serviceDetails || !servicePrice) {
      alert("Vui lòng điền đầy đủ thông tin!");
      return;
  }

  alert(`Đã sửa dịch vụ: ID: ${id}, Tên: ${serviceName}, Giá: ${servicePrice}`);
  $('#editServiceModal').modal('hide');
  (document.getElementById('editServiceForm') as HTMLFormElement).reset();
}

function deleteService(id: string): void {
  if (confirm(`Bạn có chắc chắn muốn xóa dịch vụ với ID: ${id}?`)) {
      alert(`Dịch vụ với ID ${id} đã bị xóa!`);
      // Thêm logic để xóa dịch vụ từ bảng
  }
}

function signOut(): void {
  if (confirm("Bạn có chắc chắn muốn đăng xuất?")) {
      alert("Đã đăng xuất!");
      window.location.href = 'login.html'; // Điều hướng đến trang đăng nhập
  }
}

