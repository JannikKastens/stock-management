import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { StockFormComponent } from './stock-form.component';
import { StockService } from '../../services/stock.service';
import { of } from 'rxjs';

describe('StockFormComponent', () => {
  let component: StockFormComponent;
  let fixture: ComponentFixture<StockFormComponent>;
  let stockService: jasmine.SpyObj<StockService>;

  beforeEach(async () => {
    const spy = jasmine.createSpyObj('StockService', ['createStock']);
    spy.createStock.and.returnValue(of({}));

    await TestBed.configureTestingModule({
      imports: [StockFormComponent, ReactiveFormsModule, RouterTestingModule],
      providers: [{ provide: StockService, useValue: spy }],
    }).compileComponents();

    stockService = TestBed.inject(StockService) as jasmine.SpyObj<StockService>;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StockFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with an invalid form', () => {
    expect(component.stockForm.valid).toBeFalsy();
  });

  it('should validate required fields', () => {
    const form = component.stockForm;
    expect(form.get('tickerSymbol')?.errors?.['required']).toBeTruthy();
    expect(form.get('name')?.errors?.['required']).toBeTruthy();
    expect(form.get('purchaseDate')?.errors?.['required']).toBeTruthy();
    expect(form.get('purchasePrice')?.errors?.['required']).toBeTruthy();
    expect(form.get('quantity')?.errors?.['required']).toBeTruthy();
    expect(form.get('sector')?.errors?.['required']).toBeTruthy();
  });

  it('should validate minimum values', () => {
    const form = component.stockForm;
    form.get('purchasePrice')?.setValue(0);
    form.get('quantity')?.setValue(0);

    expect(form.get('purchasePrice')?.errors?.['min']).toBeTruthy();
    expect(form.get('quantity')?.errors?.['min']).toBeTruthy();
  });
});
