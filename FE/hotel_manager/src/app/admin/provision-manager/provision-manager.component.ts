import { Component, OnInit } from '@angular/core';
import { AdminComponent } from '../admin.component';
import $ from 'jquery';
import 'bootstrap';
import { AdminService, Images } from '../admin.service';
import { NgForm } from '@angular/forms';
@Component({
  selector: 'app-provision-manager',
  templateUrl: './provision-manager.component.html',
  styleUrl: './provision-manager.component.css'
})
export class ProvisionManagerComponent implements OnInit {
  provisions: Provision[] = [];
  filterProvisions: Provision[] = [];
  provisionId: number = 0;
  provisionName: string = "";
  description: string = "";
  price: number = 0;
  status: string = "ACTIVE";
  files: File[] | null = [];
  errors: any[] = [];
  constructor(public admin: AdminComponent, private adminService: AdminService) { }
  ngOnInit(): void {
    this.admin.pageTitle = 'Provision Management';
    this.getProvisions();
  }

  //get list Provision
  getProvisions() {
    this.adminService.getProvisions().subscribe(
      (response: Provision[]) => {
        this.provisions = response;
        this.filterProvisions = response
      }
    )
  }

  //tìm kiếm Provision
  searchProvisions(): void {
    const input: string = (document.getElementById('searchProvisionInput') as HTMLInputElement).value.toLowerCase();
    this.filterProvisions = this.provisions.filter(provision => {
      return provision.provisionName?.toLowerCase().includes(input) ||
        provision.description?.toLowerCase().includes(input) ||
        String(provision.price).toLowerCase().includes(input) ||
        provision.status?.toLowerCase().includes(input)
    });
  }
  handleKeyPress(event: any) {
    if (event.key === 'Enter') {
      this.searchProvisions();
    }
  }

  onFileChange(event: Event) {
    const input = event.target as HTMLInputElement;

    this.errors = [];
    if (input.files && input.files.length > 0) {
      this.files = Array.from(input.files);

      // Kiểm tra định dạng tệp
      const validFormats = ['image/jpeg', 'image/png', 'image/gif'];
      if (!this.files.some(file => validFormats.includes(file.type))) {
        this.errors.push({ key: 'images', message: 'Only JPEG, PNG or GIF format images are accepted.' });
      }
    } else {
      this.files = [];
      this.errors.push({ key: 'images', message: 'Please select an image.' });
    }
  }

  //submit provision đã add

  onSubmitAdd() {
    this.adminService.addProvision(this.provisionName, this.description, this.price, this.status, this.files).subscribe(
      response => {
        alert("Add Success!");
        const modalElement = document.getElementById(`addProvisionModal`);
        if (modalElement) {
          modalElement.style.display = 'none';
          modalElement.classList.remove('show');
        }
        const backdrop = document.querySelector('.modal-backdrop.fade.show');
        if (backdrop) {
          document.body.removeChild(backdrop);
        }
        this.files = [];
        this.getProvisions();
        this.resetFormData();
      },
      error => {
        this.errors = [];
            if (error.error && Array.isArray(error.error)) {
                error.error.forEach((element: any) => {
                    this.errors.push({ key: element.key || 'unknown', message: element.message || 'An error occurred' });
                });
            } else {
                this.errors.push({ key: 'general', message: 'An unknown error has occurred. Please try again!' });
            }
            console.log("error", error.error);
      }
    );
  }
  findErrors(key: string) {
    return this.errors.find((error: any) => error.key == key)?.message;
  }


  // gán giá trị Room edit
  openEditProvision(provision: Provision) {
    this.provisionId = provision.provisionId
    this.provisionName = provision.provisionName
    this.description = provision.description
    this.price = provision.price
    this.status = provision.status
  }
  // submit room đã edit
  onSubmitEdit(form: NgForm) {
    console.log(form.valid)
    if (form.valid) {
      this.adminService.eidtProvision(this.provisionId, this.provisionName, this.description, this.price, this.status).subscribe(
        response => {
          alert("Edit Success!");
          const modalElement = document.getElementById(`editProvisionModal${this.provisionId}`);
          if (modalElement) {
            modalElement.style.display = 'none';
            modalElement.classList.remove('show');
          }
          const backdrop = document.querySelector('.modal-backdrop.fade.show');
          if (backdrop) {
            document.body.removeChild(backdrop);
          }
          this.getProvisions();
          this.resetFormData();
        },
      );
    }
  }
  // reset lại biến
  resetFormData() {
    this.provisionName = "";
    this.description = "";
    this.price = 0;
    this.status = "ACTIVE";
    this.files = null;
  }

}

export interface Provision {
  provisionId: number;
  provisionName: string;
  description: string;
  price: number;
  status: string;
  images: Images[];
}






