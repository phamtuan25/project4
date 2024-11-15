import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaymentManagerComponent } from './payment-manager.component';

describe('PaymentManagerComponent', () => {
  let component: PaymentManagerComponent;
  let fixture: ComponentFixture<PaymentManagerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PaymentManagerComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PaymentManagerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
