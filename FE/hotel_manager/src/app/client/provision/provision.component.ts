import { Component, OnInit } from '@angular/core';
import { ClientComponent } from '../client.component';
import { ClientService } from '../client.service';
import { ActivatedRoute, Router } from '@angular/router';
declare var $: any
@Component({
  selector: 'app-provision',
  templateUrl: './provision.component.html',
  styleUrl: './provision.component.css'
})
export class ProvisionComponent implements OnInit{
  provisions: Provison[] = [];
  keyword: string = '';
  errors: any[] = []; 
  totalProvisions: number = 0;
  pageSize: number = 4; 
  currentPage: number = 1;
  totalPages: number = 0;

  constructor(
    public client: ClientComponent, 
    private clientService: ClientService, 
    private router: Router,        
    private route: ActivatedRoute  
  ) { }

  ngOnInit(): void {
    this.client.pageTitle = 'Service Page';

    this.route.queryParams.subscribe(params => {
      const page = params['page'];
      if (page) {
        this.currentPage = +page;
      }
      this.getProvisions(this.currentPage, this.pageSize, this.keyword); 
    });
  }

  ngAfterContentInit(): void {
    const myCarousel = document.querySelector('#roomCarousel');
    const carousel = $(myCarousel).carousel();
  }

  getProvisions(page: number, size: number, keyword: string): void {
    this.clientService.getProvision(page - 1, size, keyword, 'ACTIVE').subscribe(
      (response: any) => {
        this.provisions = response.content;
        this.totalProvisions = response.totalElements;
        this.totalPages = Math.ceil(this.totalProvisions / this.pageSize);
      },
      (error) => {
        console.error("Error fetching provisions", error);
        this.provisions = [];
        this.errors.push(error);
      }
    );
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages) return;
    this.currentPage = page;

    this.router.navigate([], {
      queryParams: { page: this.currentPage },
      queryParamsHandling: 'merge',
    });
  }
  }

export interface Provison {
  provisonId: number;
  provisionName: string;
  description: string;
  price: number;
  status: string;
  images: string[];
}

