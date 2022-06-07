import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ByPainterComponent } from './by-painter.component';

describe('ByPainterComponent', () => {
  let component: ByPainterComponent;
  let fixture: ComponentFixture<ByPainterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ByPainterComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ByPainterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
