import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JumpcvComponent } from './jumpcv.component';

describe('JumpcvComponent', () => {
  let component: JumpcvComponent;
  let fixture: ComponentFixture<JumpcvComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ JumpcvComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(JumpcvComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
