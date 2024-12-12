import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { StockListComponent } from './stock-list.component';
import { StockService } from '../../services/stock.service';
import { of } from 'rxjs';

describe('StockListComponent', () => {
  let component: StockListComponent;
  let fixture: ComponentFixture<StockListComponent>;
  let stockService: jasmine.SpyObj<StockService>;

  beforeEach(async () => {
    const spy = jasmine.createSpyObj('StockService', ['getStocks']);
    spy.getStocks.and.returnValue(of([]));

    await TestBed.configureTestingModule({
      imports: [StockListComponent],
      providers: [{ provide: StockService, useValue: spy }, provideRouter([])],
    }).compileComponents();

    stockService = TestBed.inject(StockService) as jasmine.SpyObj<StockService>;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StockListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load stocks on init', () => {
    expect(stockService.getStocks).toHaveBeenCalled();
  });
});
