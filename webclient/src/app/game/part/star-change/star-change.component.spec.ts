import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StarChangeComponent } from './star-change.component';

describe('StarChangeComponent', () => {
  let component: StarChangeComponent;
  let fixture: ComponentFixture<StarChangeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StarChangeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StarChangeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
