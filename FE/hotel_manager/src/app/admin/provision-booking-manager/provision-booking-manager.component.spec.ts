import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProvisionBookingManagerComponent } from './provision-booking-manager.component';

describe('ProvisionBookingManagerComponent', () => {
  let component: ProvisionBookingManagerComponent;
  let fixture: ComponentFixture<ProvisionBookingManagerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProvisionBookingManagerComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProvisionBookingManagerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
