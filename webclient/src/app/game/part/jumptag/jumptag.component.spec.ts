import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JumptagComponent } from './jumptag.component';

describe('JumptagComponent', () => {
  let component: JumptagComponent;
  let fixture: ComponentFixture<JumptagComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ JumptagComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(JumptagComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
