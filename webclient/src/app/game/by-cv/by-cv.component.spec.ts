import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ByCVComponent } from './by-cv.component';

describe('ByCVComponent', () => {
  let component: ByCVComponent;
  let fixture: ComponentFixture<ByCVComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ByCVComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ByCVComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
