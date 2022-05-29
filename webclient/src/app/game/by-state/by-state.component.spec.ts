import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ByStateComponent } from './by-state.component';

describe('ByStateComponent', () => {
  let component: ByStateComponent;
  let fixture: ComponentFixture<ByStateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ByStateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ByStateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
