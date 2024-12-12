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
  templateUrl: './stock-form.component.html',
  styleUrls: ['./stock-form.component.scss'],
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
