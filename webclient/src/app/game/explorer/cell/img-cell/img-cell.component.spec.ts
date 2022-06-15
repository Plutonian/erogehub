import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImgCellComponent } from './img-cell.component';

describe('ImgCellComponent', () => {
  let component: ImgCellComponent;
  let fixture: ComponentFixture<ImgCellComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ImgCellComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ImgCellComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
