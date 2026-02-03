import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchPageSearchOptItemComponent } from './search-page-search-opt-item.component';

describe('TagSearchPageSearchOptComponent', () => {
  let component: SearchPageSearchOptItemComponent;
  let fixture: ComponentFixture<SearchPageSearchOptItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchPageSearchOptItemComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SearchPageSearchOptItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
