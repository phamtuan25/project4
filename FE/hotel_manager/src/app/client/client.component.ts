import { Component,Renderer2  } from '@angular/core';
import { Router } from '@angular/router';
import { ConfigService } from '../../config/config.service';
import { ClientService } from './client.service';
import { User } from '../admin/user-manager/user-manager.component';
import { GlobalStateService } from '../../config/global.stage.service';

@Component({
  selector: 'app-client',
  templateUrl: './client.component.html',
  styleUrl: './client.component.css'
})
export class ClientComponent {
  title = 'hotel_manager';
  pageTitle = 'Client Page';
  constructor(private router: Router, private config: ConfigService, private clientService: ClientService, private globalStageService: GlobalStateService, private renderer: Renderer2) { }
  userLogin: User | null = null
  lastScrollTop = 0;
  signOut(): void {
    if (confirm("Are you sure you want to log out?")) {
      this.config.setToken('');
      this.config.setEmail('');
      this.userLogin = null;
      alert("Signed out!");
      this.router.navigate(['/login']);
    }
  }
  ngOnInit(): void {
    const email = this.config.getEmail();
    if (email) this.getUserLogin(email);

     window.addEventListener('scroll', this.handleScroll.bind(this));

     this.checkScroll();
  }

  ngOnDestroy(): void {
    window.removeEventListener('scroll', this.handleScroll.bind(this));
  }

  getInitial(fullName: string | null | undefined): string {
    if (!fullName) {
      return 'N/A';
    }
    return fullName.charAt(0).toUpperCase();
  }

  openProfile() {
  }
  getUserLogin(email: string | null) {
    this.clientService.getUserLogin(email).subscribe(
      (response: any) => {
        this.userLogin = response;
        this.globalStageService.setUserStage(response);
      },
      (error) => {
        console.error("Error fetching users", error);
      }
    )
  }

  handleScroll(): void {
    const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
    const navbar = document.getElementById("navbar");
    const scrollToTopBtn = document.getElementById("scrollToTopBtn");

    if (navbar) {
      if (scrollTop > this.lastScrollTop) {
        this.renderer.setStyle(navbar, 'top', '-90px');
      } else {
        this.renderer.setStyle(navbar, 'top', '0');
        navbar.classList.add("navbar-custom");
      }

      if (scrollTop === 0) {
        navbar.classList.remove("navbar-custom");
        this.renderer.setStyle(navbar, 'top', '0');
      }
    }

    if (scrollToTopBtn) {
      if (scrollTop > 100) {
        this.renderer.setStyle(scrollToTopBtn, 'display', 'flex');
      } else {
        this.renderer.setStyle(scrollToTopBtn, 'display', 'none');
      }
    }

    this.lastScrollTop = scrollTop <= 0 ? 0 : scrollTop;
  }

  checkScroll() {
    const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
    const scrollToTopBtn = document.getElementById("scrollToTopBtn");
    if (scrollToTopBtn) {
      if (scrollTop > 100) {
        this.renderer.setStyle(scrollToTopBtn, 'display', 'flex');
      } else {
        this.renderer.setStyle(scrollToTopBtn, 'display', 'none');
      }
    }
  }
  OnScrollTop() {
    window.scrollTo({
      top: 0,
      behavior: 'smooth'
    });
  }
}
