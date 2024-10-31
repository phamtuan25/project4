import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProvisionManagerComponent } from './provision-manager.component';

describe('ProvisionManagerComponent', () => {
  let component: ProvisionManagerComponent;
  let fixture: ComponentFixture<ProvisionManagerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProvisionManagerComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProvisionManagerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
