import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, ActivatedRoute } from '@angular/router';
import { StockService } from '../../services/stock.service';
import { Stock } from '../../models/stock.interface';
import { Dividend } from '../../models/dividend.interface';
import { switchMap, forkJoin } from 'rxjs';
import { DividendService } from '../../services/dividend.service';

@Component({
  selector: 'app-stock-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="container" *ngIf="stock">
      <h1>{{ stock.name }} ({{ stock.tickerSymbol }})</h1>

      <div class="stock-details">
        <div class="detail-item">
          <span class="label">Kaufdatum:</span>
          <span>{{ stock.kaufDatum | date }}</span>
        </div>
        <div class="detail-item">
          <span class="label">Kaufpreis:</span>
          <span>{{ stock.kaufPreis | currency : 'EUR' }}</span>
        </div>
        <div class="detail-item">
          <span class="label">Anzahl:</span>
          <span>{{ stock.anzahl }}</span>
        </div>
        <div class="detail-item">
          <span class="label">Sektor:</span>
          <span>{{ stock.sektor }}</span>
        </div>
      </div>

      <div class="dividends-section">
        <div class="section-header">
          <h2>Dividenden</h2>
          <button
            [routerLink]="['/stocks', stock.id, 'dividends', 'new']"
            class="add-button"
          >
            Dividende hinzufügen
          </button>
        </div>

        <table class="dividends-table" *ngIf="dividends.length > 0">
          <thead>
            <tr>
              <th>Datum</th>
              <th>Betrag</th>
              <th>Währung</th>
              <th>Aktionen</th>
            </tr>
          </thead>
          <tbody>
            @for (dividend of dividends; track dividend.id) {
            <tr>
              <td>{{ dividend.datum | date }}</td>
              <td>{{ dividend.betrag | number : '1.2-2' }}</td>
              <td>{{ dividend.waehrung }}</td>
              <td>
                <button
                  class="delete-button"
                  (click)="deleteDividend(dividend.id)"
                >
                  Löschen
                </button>
              </td>
            </tr>
            }
          </tbody>
        </table>

        <div class="no-dividends" *ngIf="dividends.length === 0">
          Noch keine Dividenden vorhanden.
        </div>
      </div>

      <div class="actions">
        <button [routerLink]="['/stocks']" class="back-button">
          Zurück zur Übersicht
        </button>
      </div>
    </div>
  `,
  styles: [
    `
      .container {
        padding: 2rem;
        max-width: 1200px;
        margin: 0 auto;
      }

      .stock-details {
        background: #f8f9fa;
        padding: 1.5rem;
        border-radius: 8px;
        margin: 1.5rem 0;
      }

      .detail-item {
        margin-bottom: 1rem;
      }

      .detail-item:last-child {
        margin-bottom: 0;
      }

      .label {
        font-weight: 600;
        margin-right: 0.5rem;
        color: #495057;
      }

      .section-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 1rem;
      }

      .dividends-table {
        width: 100%;
        border-collapse: separate;
        border-spacing: 0;
        margin: 1rem 0;
      }

      .dividends-table th,
      .dividends-table td {
        padding: 1rem;
        text-align: left;
        border-bottom: 1px solid #dee2e6;
      }

      .dividends-table th {
        background-color: #f8f9fa;
        font-weight: 600;
      }

      .no-dividends {
        text-align: center;
        padding: 2rem;
        background: #f8f9fa;
        border-radius: 8px;
        color: #6c757d;
      }

      .add-button {
        padding: 0.75rem 1.5rem;
        background-color: #4caf50;
        color: white;
        border: none;
        border-radius: 6px;
        cursor: pointer;
      }

      .back-button {
        padding: 0.75rem 1.5rem;
        background-color: #6c757d;
        color: white;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        margin-top: 2rem;
      }

      .actions {
        margin-top: 2rem;
      }

      .delete-button {
        padding: 0.5rem 1rem;
        background-color: #dc3545;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
      }

      .delete-button:hover {
        background-color: #c82333;
      }
    `,
  ],
})
export class StockDetailComponent implements OnInit {
  stock?: Stock;
  dividends: Dividend[] = [];

  constructor(
    private route: ActivatedRoute,
    private stockService: StockService,
    private dividendService: DividendService
  ) {}

  ngOnInit() {
    this.route.params
      .pipe(
        switchMap((params) => {
          const stockId = +params['id'];
          return forkJoin({
            stock: this.stockService.getStock(stockId),
            dividends: this.dividendService.getDividendsByStockId(stockId),
          });
        })
      )
      .subscribe({
        next: ({ stock, dividends }) => {
          this.stock = stock;
          this.dividends = dividends;
        },
        error: (error) => console.error('Error loading stock details:', error),
      });
  }

  deleteDividend(id: number) {
    this.dividendService.deleteDividend(id).subscribe({
      next: () => {
        this.dividends = this.dividends.filter((d) => d.id !== id);
      },
      error: (error) => console.error('Error deleting dividend:', error),
    });
  }
}
