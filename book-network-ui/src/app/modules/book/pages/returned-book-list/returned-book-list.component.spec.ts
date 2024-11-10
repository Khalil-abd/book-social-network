import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReturnedBookListComponent } from './returned-book-list.component';

describe('ReturnedBookListComponent', () => {
  let component: ReturnedBookListComponent;
  let fixture: ComponentFixture<ReturnedBookListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReturnedBookListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReturnedBookListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
