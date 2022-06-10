import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ByStarComponent } from './by-star.component';

describe('ByStarComponent', () => {
  let component: ByStarComponent;
  let fixture: ComponentFixture<ByStarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ByStarComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ByStarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
