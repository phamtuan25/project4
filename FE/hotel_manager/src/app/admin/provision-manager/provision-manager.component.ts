import { Component, OnInit } from '@angular/core';
import { AdminComponent } from '../admin.component';
import $ from 'jquery';
import 'bootstrap';
import { AdminService } from '../admin.service';
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
  // images: string = "";
  constructor(public admin: AdminComponent, private adminService: AdminService){}
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

  //submit provision đã add
  errors:any=[];
  onSubmitAdd() {
    this.adminService.addProvision(this.provisionName, this.description, this.price, this.status).subscribe(
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
        this.getProvisions();
        this.resetFormData();
      },
      error => {
        console.log("error",error.error)
        this.errors = [];
        error.error.forEach((element:any) => {
          this.errors.push(element);
        });
        
      }
    );
  }
  findErrors(key:string){
    return this.errors.find((error:any)=>error.key==key)?.message;
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
    if(form.valid) {
      this.adminService.eidtProvision(this.provisionId, this.provisionName,this.description, this.price, this.status).subscribe(
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
      this.status = "";
      // this.images = "";
    }

}

export interface Provision {
  provisionId: number;
  provisionName: string;
  description: string;
  price: number; 
  status: string; 
  images: string;
}






