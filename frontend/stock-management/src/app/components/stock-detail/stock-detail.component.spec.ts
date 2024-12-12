import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute } from '@angular/router';
import { StockDetailComponent } from './stock-detail.component';
import { StockService } from '../../services/stock.service';
import { DividendService } from '../../services/dividend.service';
import { of } from 'rxjs';

describe('StockDetailComponent', () => {
  let component: StockDetailComponent;
  let fixture: ComponentFixture<StockDetailComponent>;
  let stockService: jasmine.SpyObj<StockService>;
  let dividendService: jasmine.SpyObj<DividendService>;

  beforeEach(async () => {
    const stockSpy = jasmine.createSpyObj('StockService', ['getStock']);
    const dividendSpy = jasmine.createSpyObj('DividendService', [
      'getDividendsByStockId',
      'deleteDividend',
    ]);

    stockSpy.getStock.and.returnValue(
      of({
        id: 1,
        name: 'Test Stock',
        tickerSymbol: 'TEST',
        purchaseDate: new Date(),
        purchasePrice: 100,
        amount: 10,
        sector: 'Technology',
      })
    );

    dividendSpy.getDividendsByStockId.and.returnValue(of([]));
    dividendSpy.deleteDividend.and.returnValue(of({}));

    await TestBed.configureTestingModule({
      imports: [StockDetailComponent, RouterTestingModule],
      providers: [
        { provide: StockService, useValue: stockSpy },
        { provide: DividendService, useValue: dividendSpy },
        {
          provide: ActivatedRoute,
          useValue: {
            params: of({ id: '1' }),
          },
        },
      ],
    }).compileComponents();

    stockService = TestBed.inject(StockService) as jasmine.SpyObj<StockService>;
    dividendService = TestBed.inject(
      DividendService
    ) as jasmine.SpyObj<DividendService>;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StockDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load stock and dividends on init', () => {
    expect(stockService.getStock).toHaveBeenCalledWith(1);
    expect(dividendService.getDividendsByStockId).toHaveBeenCalledWith(1);
  });

  it('should delete dividend', () => {
    const dividendId = 1;
    component.deleteDividend(dividendId);
    expect(dividendService.deleteDividend).toHaveBeenCalledWith(dividendId);
  });
});
