import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ByWriterComponent } from './by-writer.component';

describe('ByWriterComponent', () => {
  let component: ByWriterComponent;
  let fixture: ComponentFixture<ByWriterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ByWriterComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ByWriterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
