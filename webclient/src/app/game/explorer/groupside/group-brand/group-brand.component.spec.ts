import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupBrandComponent } from './group-brand.component';

describe('GroupBrandComponent', () => {
  let component: GroupBrandComponent;
  let fixture: ComponentFixture<GroupBrandComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GroupBrandComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GroupBrandComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
