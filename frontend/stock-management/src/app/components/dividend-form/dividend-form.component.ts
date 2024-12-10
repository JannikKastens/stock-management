import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  Validators,
  FormGroup,
} from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { DividendService } from '../../services/dividend.service';
import { Dividend } from '../../models/dividend.interface';

@Component({
  selector: 'app-dividend-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="container">
      <h1>Neue Dividende hinzufügen</h1>

      <form
        [formGroup]="dividendForm"
        (ngSubmit)="onSubmit()"
        class="dividend-form"
      >
        <div class="form-group">
          <label for="betrag">Betrag*</label>
          <input
            id="betrag"
            type="number"
            step="0.01"
            formControlName="betrag"
          />
          @if (dividendForm.get('betrag')?.errors?.['required'] &&
          dividendForm.get('betrag')?.touched) {
          <div class="error">Betrag ist erforderlich</div>
          } @if (dividendForm.get('betrag')?.errors?.['min']) {
          <div class="error">Betrag muss größer als 0 sein</div>
          }
        </div>

        <div class="form-group">
          <label for="datum">Datum*</label>
          <input id="datum" type="date" formControlName="datum" />
          @if (dividendForm.get('datum')?.errors?.['required'] &&
          dividendForm.get('datum')?.touched) {
          <div class="error">Datum ist erforderlich</div>
          }
        </div>

        <div class="form-group">
          <label for="waehrung">Währung*</label>
          <select id="waehrung" formControlName="waehrung">
            <option value="EUR">EUR</option>
            <option value="USD">USD</option>
            <option value="CHF">CHF</option>
            <option value="GBP">GBP</option>
          </select>
          @if (dividendForm.get('waehrung')?.errors?.['required'] &&
          dividendForm.get('waehrung')?.touched) {
          <div class="error">Währung ist erforderlich</div>
          }
        </div>

        <div class="form-actions">
          <button type="button" class="cancel-button" (click)="onCancel()">
            Abbrechen
          </button>
          <button
            type="submit"
            class="submit-button"
            [disabled]="!dividendForm.valid"
          >
            Speichern
          </button>
        </div>
      </form>
    </div>
  `,
  styles: [
    `
      .container {
        padding: 2rem;
        max-width: 600px;
        margin: 2rem auto;
        background-color: white;
        border-radius: 8px;
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
      }

      .dividend-form {
        display: flex;
        flex-direction: column;
        gap: 1.5rem;
      }

      .form-group {
        display: flex;
        flex-direction: column;
        gap: 0.5rem;
      }

      label {
        font-weight: 500;
        color: #2c3e50;
      }

      input,
      select {
        padding: 0.75rem;
        border: 1px solid #ddd;
        border-radius: 4px;
        font-size: 1rem;
      }

      input:focus,
      select:focus {
        outline: none;
        border-color: #4caf50;
      }

      .error {
        color: #dc3545;
        font-size: 0.875rem;
        margin-top: 0.25rem;
      }

      .form-actions {
        display: flex;
        gap: 1rem;
        justify-content: flex-end;
        margin-top: 1rem;
      }

      button {
        padding: 0.75rem 1.5rem;
        border: none;
        border-radius: 4px;
        font-weight: 500;
        cursor: pointer;
        transition: background-color 0.2s;
      }

      .submit-button {
        background-color: #4caf50;
        color: white;
      }

      .submit-button:hover {
        background-color: #45a049;
      }

      .submit-button:disabled {
        background-color: #cccccc;
        cursor: not-allowed;
      }

      .cancel-button {
        background-color: #f8f9fa;
        color: #2c3e50;
      }

      .cancel-button:hover {
        background-color: #e9ecef;
      }
    `,
  ],
})
export class DividendFormComponent implements OnInit {
  dividendForm: FormGroup = new FormGroup({});

  private stockId?: number;

  constructor(
    private fb: FormBuilder,
    private dividendService: DividendService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.dividendForm = this.fb.group({
      betrag: ['', [Validators.required, Validators.min(0.01)]],
      datum: ['', Validators.required],
      waehrung: ['EUR', Validators.required],
    });
    this.stockId = Number(this.route.snapshot.paramMap.get('stockId'));
  }

  onSubmit() {
    if (this.dividendForm.valid && this.stockId) {
      const formValues = this.dividendForm.value;
      const dividend = {
        betrag: Number(formValues.betrag),
        datum: new Date(formValues.datum as string),
        waehrung: formValues.waehrung as string,
        stockId: this.stockId,
      } as Omit<Dividend, 'id'>;

      this.dividendService.createDividend(this.stockId, dividend).subscribe({
        next: () => {
          this.router.navigate(['/stocks', this.stockId]);
        },
        error: (error) => console.error('Error creating dividend:', error),
      });
    }
  }

  onCancel() {
    if (this.stockId) {
      this.router.navigate(['/stocks', this.stockId]);
    }
  }
}
