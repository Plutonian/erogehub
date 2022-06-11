import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ByDateComponent } from './by-date.component';

describe('ByDateComponent', () => {
  let component: ByDateComponent;
  let fixture: ComponentFixture<ByDateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ByDateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ByDateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
