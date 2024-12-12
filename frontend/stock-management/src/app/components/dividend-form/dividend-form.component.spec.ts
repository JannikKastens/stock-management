import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute } from '@angular/router';
import { DividendFormComponent } from './dividend-form.component';
import { DividendService } from '../../services/dividend.service';
import { of } from 'rxjs';

describe('DividendFormComponent', () => {
  let component: DividendFormComponent;
  let fixture: ComponentFixture<DividendFormComponent>;
  let dividendService: jasmine.SpyObj<DividendService>;

  beforeEach(async () => {
    const spy = jasmine.createSpyObj('DividendService', ['createDividend']);
    spy.createDividend.and.returnValue(of({}));

    await TestBed.configureTestingModule({
      imports: [
        DividendFormComponent,
        ReactiveFormsModule,
        RouterTestingModule,
      ],
      providers: [
        { provide: DividendService, useValue: spy },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get: () => '1',
              },
            },
          },
        },
      ],
    }).compileComponents();

    dividendService = TestBed.inject(
      DividendService
    ) as jasmine.SpyObj<DividendService>;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DividendFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with an invalid form', () => {
    expect(component.dividendForm.valid).toBeFalsy();
  });

  it('should validate required fields', () => {
    const form = component.dividendForm;
    expect(form.get('amount')?.errors?.['required']).toBeTruthy();
    expect(form.get('date')?.errors?.['required']).toBeTruthy();
  });

  it('should validate minimum amount', () => {
    const form = component.dividendForm;
    form.get('amount')?.setValue(0);
    expect(form.get('amount')?.errors?.['min']).toBeTruthy();
  });

  it('should initialize currency with EUR', () => {
    expect(component.dividendForm.get('currency')?.value).toBe('EUR');
  });

  it('should submit valid form', () => {
    const form = component.dividendForm;
    form.get('amount')?.setValue(10);
    form.get('date')?.setValue('2024-01-01');
    form.get('currency')?.setValue('EUR');

    component.onSubmit();
    expect(dividendService.createDividend).toHaveBeenCalled();
  });
});
