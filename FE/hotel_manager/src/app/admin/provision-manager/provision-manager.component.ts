import { Component, OnInit } from '@angular/core';
import { AdminComponent } from '../admin.component';
import $ from 'jquery';
import 'bootstrap';
import { AdminService, Images } from '../admin.service';
import { NgForm } from '@angular/forms';
import { Observable } from 'rxjs';
import { ChangeDetectorRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
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
  isShowAddPopup: Boolean = false;
  isShowEditPopup: Boolean = false;
  deleteFiles: string[] = [];
  imageOrigin: string[] = [];
  constructor(public admin: AdminComponent, private adminService: AdminService,private http: HttpClient, private cdr: ChangeDetectorRef) { }
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

  openAddProvision() {
    this.resetFormData();
    this.isShowAddPopup = true;
    this.openPopup();
  }
  // gán giá trị Room edit
  openEditProvision(provision: Provision) {
    this.provisionId = provision.provisionId
    this.provisionName = provision.provisionName
    this.description = provision.description
    this.price = provision.price
    this.status = provision.status
    this.imageOrigin = provision.images
    provision.images.map(image => {
      this.convertImageToBase64('/upload_images/' + image).subscribe(base64 => {
        this.imagePaths.push({
          name: image,
          path: base64
        });
      }, error => {
        console.error('Error converting image:', error);
      });
  })
    this.isShowEditPopup = true;
    this.openPopup();
  }
  // submit room đã edit
  onSubmitEdit(form: NgForm) {
    console.log(form.valid)
    if (form.valid) {
      this.adminService.eidtProvision(this.provisionId, this.provisionName, this.description, this.price, this.status, this.files, this.deleteFiles).subscribe(
        response => {
          alert("Edit Success!");
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
    this.errors = [];
    this.imagePaths = [];
    this.isShowAddPopup = false;
    this.isShowEditPopup = false;
    this.closePopup();
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
  openPopup() {
    document.body.style.paddingRight = '17px'
    document.body.classList.add('modal-open')
      
  }
  closePopup() {
    document.body.style.paddingRight = '';
    document.body.classList.remove('modal-open')
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




