import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormattedDateComponent } from './formatted-date.component';

describe('FormattedDateComponent', () => {
  let component: FormattedDateComponent;
  let fixture: ComponentFixture<FormattedDateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FormattedDateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FormattedDateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
