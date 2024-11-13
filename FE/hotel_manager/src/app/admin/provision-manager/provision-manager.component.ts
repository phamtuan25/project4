import { Component, OnInit } from '@angular/core';
import { AdminComponent } from '../admin.component';
import $ from 'jquery';
import 'bootstrap';
import { AdminService, Images } from '../admin.service';
import { NgForm } from '@angular/forms';
import { Observable } from 'rxjs';
import { ChangeDetectorRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import Modal from 'bootstrap/js/dist/modal';
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
  imagePaths: ImagePaths[] = [];
  deleteFiles: string[] = [];
  imageOrigin: string[] = [];
  keyword: string = '';
  totalProvisions: number = 0;
  pageSize: number = 10;
  currentPage: number = 1;
  totalPages: number = 0;
  selectedProvision: any;
  editModal!: Modal;
  createModal!: Modal;
  constructor(public admin: AdminComponent, private adminService: AdminService,private http: HttpClient, private cdr: ChangeDetectorRef) { }
  ngOnInit(): void {
    this.admin.pageTitle = 'Provision Management';
    this.getProvisions(this.currentPage , this.pageSize, this.keyword);
  }

  ngAfterViewInit() {
    this.editModal = new Modal('#editModal', {
      keyboard: false,
      backdrop: 'static'
    });
    this.createModal = new Modal('#createModal', {
      keyboard: false,
      backdrop: 'static'
    });
  }

  //get list Provision   
  getProvisions(page: number, size: number, keyword: string) {
    this.adminService.getProvisions(page - 1, size, keyword).subscribe(
      (response: any) => {
        this.provisions = response.content;
        this.totalProvisions = response.totalElements;
        this.totalPages = Math.ceil(this.totalProvisions / this.pageSize);
      },
      (error) => {
        console.error("Error fetching provision", error);
        this.provisions = [];
        this.errors.push(error);
      }
    );
  }

  goToPage(page: number): void {
    if (page < 1 || page > Math.ceil(this.totalProvisions / this.pageSize)) return;
    this.currentPage = page;
    this.getProvisions(this.currentPage, this.pageSize, this.keyword);
  }


  // tìm kiếm Provision
  searchProvisions(): void {
    const input: string = (document.getElementById('searchProvisionInput') as HTMLInputElement).value.trim();
    this.keyword = input;
    this.currentPage = 1;
    this.getProvisions(this.currentPage, this.pageSize, this.keyword);
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
        console.log('this.files',this.files)
        Array.from(input.files).forEach(file => {
          const reader = new FileReader();
          reader.onload = (e) => {
            if (e.target && e.target.result) {
              this.imagePaths.push({
                name: file.lastModified + file.name,
                path: e.target.result as string
              });
              this.cdr.detectChanges();
            }
          };
          reader.readAsDataURL(file);
        });
        console.log("this.imagePaths",this.imagePaths)
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
        this.files = [];
        this.getProvisions(this.currentPage , this.pageSize, this.keyword);
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

  openAddProvision() {
    this.resetFormData();
    this.createModal.show();
  }
  // gán giá trị Room edit
  openEditProvision(provision: Provision) {
    this.selectedProvision = provision;
    this.provisionId = provision.provisionId
    this.provisionName = provision.provisionName
    this.description = provision.description
    this.price = provision.price
    this.status = provision.status
    this.imageOrigin = provision.images
    provision.images.map(image => {
      this.convertImageToBase64('http://localhost:8080/upload_images/' + image).subscribe(base64 => {
        this.imagePaths.push({
          name: image,
          path: base64
        });
      }, error => {
        console.error('Error converting image:', error);
      });
  })
    this.editModal.show();
  }
  // submit room đã edit
  onSubmitEdit(form: NgForm) {
    console.log(form.valid)
    if (form.valid) {
      this.adminService.eidtProvision(this.provisionId, this.provisionName, this.description, this.price, this.status, this.files, this.deleteFiles).subscribe(
        response => {
          alert("Edit Success!");
          this.getProvisions(this.currentPage , this.pageSize, this.keyword);
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
    this.errors = [];
    this.imagePaths = [];
    this.createModal.hide();
    this.editModal.hide();
  }
  removeImage(image: ImagePaths, index: number) {
    this.imagePaths.splice(index, 1);
    this.files = this.files?.filter(file => file.lastModified + file.name !== image.name) ?? null;
    this.deleteFiles = this.imageOrigin.filter(ori => 
    !this.imagePaths.some(path => path.name === ori)
);
  }
  convertImageToBase64(imageUrl: string): Observable<string> {
    return new Observable(observer => {
      this.http.get(imageUrl, { responseType: 'blob' }).subscribe(blob => {
        const reader = new FileReader();
        reader.onloadend = () => {
          observer.next(reader.result as string);
          observer.complete();
        };
        reader.readAsDataURL(blob);
      }, error => {
        observer.error(error);
      });
    });
  }
}

export interface Provision {
  provisionId: number;
  provisionName: string;
  description: string;
  price: number;
  status: string;
  images: string[];
}

interface ImagePaths {
  name: string,
  path: string;
}




