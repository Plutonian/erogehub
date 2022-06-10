import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ByPlaystateComponent } from './by-playstate.component';

describe('ByPlaystateComponent', () => {
  let component: ByPlaystateComponent;
  let fixture: ComponentFixture<ByPlaystateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ByPlaystateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ByPlaystateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
