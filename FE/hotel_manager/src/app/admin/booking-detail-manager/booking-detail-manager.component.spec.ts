import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookingDetailManagerComponent } from './booking-detail-manager.component';

describe('BookingDetailManagerComponent', () => {
  let component: BookingDetailManagerComponent;
  let fixture: ComponentFixture<BookingDetailManagerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BookingDetailManagerComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BookingDetailManagerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
