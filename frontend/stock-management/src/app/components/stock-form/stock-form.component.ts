import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { StockService } from '../../services/stock.service';

@Component({
  selector: 'app-stock-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="container">
      <h1>Neue Aktie hinzufügen</h1>

      <form [formGroup]="stockForm" (ngSubmit)="onSubmit()" class="stock-form">
        <div class="form-group">
          <label for="tickerSymbol">Ticker Symbol*</label>
          <input id="tickerSymbol" type="text" formControlName="tickerSymbol" />
          @if (stockForm.get('tickerSymbol')?.errors?.['required'] &&
          stockForm.get('tickerSymbol')?.touched) {
          <div class="error">Ticker Symbol ist erforderlich</div>
          }
        </div>

        <div class="form-group">
          <label for="name">Name*</label>
          <input id="name" type="text" formControlName="name" />
          @if (stockForm.get('name')?.errors?.['required'] &&
          stockForm.get('name')?.touched) {
          <div class="error">Name ist erforderlich</div>
          }
        </div>

        <div class="form-group">
          <label for="purchaseDate">Kaufdatum*</label>
          <input id="purchaseDate" type="date" formControlName="purchaseDate" />
          @if (stockForm.get('purchaseDate')?.errors?.['required'] &&
          stockForm.get('purchaseDate')?.touched) {
          <div class="error">Kaufdatum ist erforderlich</div>
          }
        </div>

        <div class="form-group">
          <label for="purchasePrice">Kaufpreis*</label>
          <input
            id="purchasePrice"
            type="number"
            step="0.01"
            formControlName="purchasePrice"
          />
          @if (stockForm.get('purchasePrice')?.errors?.['required'] &&
          stockForm.get('purchasePrice')?.touched) {
          <div class="error">Kaufpreis ist erforderlich</div>
          } @if (stockForm.get('purchasePrice')?.errors?.['min']) {
          <div class="error">Kaufpreis muss größer als 0 sein</div>
          }
        </div>

        <div class="form-group">
          <label for="quantity">Anzahl*</label>
          <input id="quantity" type="number" formControlName="quantity" />
          @if (stockForm.get('quantity')?.errors?.['required'] &&
          stockForm.get('quantity')?.touched) {
          <div class="error">Anzahl ist erforderlich</div>
          } @if (stockForm.get('quantity')?.errors?.['min']) {
          <div class="error">Anzahl muss größer als 0 sein</div>
          }
        </div>

        <div class="form-group">
          <label for="sector">Sektor*</label>
          <input id="sector" type="text" formControlName="sector" />
          @if (stockForm.get('sector')?.errors?.['required'] &&
          stockForm.get('sector')?.touched) {
          <div class="error">Sektor ist erforderlich</div>
          }
        </div>

        <div class="form-actions">
          <button type="button" class="cancel-button" (click)="onCancel()">
            Abbrechen
          </button>
          <button
            type="submit"
            class="submit-button"
            [disabled]="!stockForm.valid"
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

      .stock-form {
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

      input {
        padding: 0.75rem;
        border: 1px solid #ddd;
        border-radius: 4px;
        font-size: 1rem;
      }

      input:focus {
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
export class StockFormComponent {
  stockForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private stockService: StockService,
    private router: Router
  ) {
    this.stockForm = this.fb.group({
      tickerSymbol: ['', Validators.required],
      name: ['', Validators.required],
      purchaseDate: ['', Validators.required],
      purchasePrice: ['', [Validators.required, Validators.min(0.01)]],
      quantity: ['', [Validators.required, Validators.min(1)]],
      sector: ['', Validators.required],
    });
  }

  onSubmit() {
    if (this.stockForm.valid) {
      this.stockService.createStock(this.stockForm.value).subscribe({
        next: () => {
          this.router.navigate(['/stocks']);
        },
        error: (error) => {
          console.error('Error creating stock:', error);
          // Hier könnte man noch eine Fehlermeldung für den Benutzer anzeigen
        },
      });
    }
  }

  onCancel() {
    this.router.navigate(['/stocks']);
  }
}
