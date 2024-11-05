import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

}
document.addEventListener("DOMContentLoaded", function () {
  const checkinInput = document.getElementById("checkin") as HTMLInputElement;
  const checkoutInput = document.getElementById("checkout") as HTMLInputElement;

  // Thiết lập ngày tối thiểu cho Check In là hôm nay
  const today = new Date();
  const dd: string = String(today.getDate()).padStart(2, '0');
  const mm: string = String(today.getMonth() + 1).padStart(2, '0'); // Tháng bắt đầu từ 0
  const yyyy: string = today.getFullYear().toString();

  // Chuyển đổi ngày hiện tại thành định dạng YYYY-MM-DD
  today.setHours(0, 0, 0, 0); // Đặt giờ, phút, giây về 0
  const minCheckinDate: string = `${yyyy}-${mm}-${dd}`;
  checkinInput.setAttribute("min", minCheckinDate);

  // Cập nhật min cho Check Out khi ngày Check In thay đổi
  checkinInput.addEventListener("change", function () {
      checkoutInput.setAttribute("min", checkinInput.value);
      if (checkoutInput.value && new Date(checkoutInput.value) < new Date(checkinInput.value)) {
          checkoutInput.value = ""; // Reset giá trị
      }
  });

  // Đảm bảo không chọn ngày Check Out không hợp lệ
  checkoutInput.addEventListener("change", function () {
      const checkinDate = new Date(checkinInput.value);
      const checkoutDate = new Date(checkoutInput.value);

      if (checkoutDate <= checkinDate) {
          checkoutInput.value = ""; // Reset giá trị nếu không hợp lệ
      }
  });
});
