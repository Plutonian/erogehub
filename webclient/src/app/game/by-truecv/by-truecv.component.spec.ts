import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ByTruecvComponent } from './by-truecv.component';

describe('ByTruecvComponent', () => {
  let component: ByTruecvComponent;
  let fixture: ComponentFixture<ByTruecvComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ByTruecvComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ByTruecvComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
