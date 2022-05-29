import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JumpBrandComponent } from './jump-brand.component';

describe('JumpBrandComponent', () => {
  let component: JumpBrandComponent;
  let fixture: ComponentFixture<JumpBrandComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ JumpBrandComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(JumpBrandComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
