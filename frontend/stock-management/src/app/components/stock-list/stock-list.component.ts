import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { StockService } from '../../services/stock.service';
import { Stock } from '../../models/stock.interface';

@Component({
  selector: 'app-stock-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="container">
      <h1>Aktien-Übersicht</h1>

      <div class="actions">
        <button [routerLink]="['/stocks/new']" class="add-button">
          Neue Aktie hinzufügen
        </button>
      </div>

      <table class="stock-table">
        <thead>
          <tr>
            <th>Ticker</th>
            <th>Name</th>
            <th>Kaufdatum</th>
            <th>Kaufpreis</th>
            <th>Anzahl</th>
          </tr>
        </thead>
        <tbody>
          @for (stock of stocks; track stock.id) {
          <tr>
            <td>{{ stock.tickerSymbol }}</td>
            <td>{{ stock.name }}</td>
            <td>{{ stock.kaufDatum | date }}</td>
            <td>{{ stock.kaufPreis | currency : 'EUR' }}</td>
            <td>{{ stock.anzahl }}</td>
          </tr>
          }
        </tbody>
      </table>
    </div>
  `,
  styles: [
    `
      .container {
        padding: 2rem;
        background-color: white;
        border-radius: 8px;
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
        margin: 2rem auto;
        max-width: 1200px;
      }

      h1 {
        margin-bottom: 1.5rem;
        color: #2c3e50;
      }

      .actions {
        margin: 1.5rem 0;
      }

      .add-button {
        padding: 0.75rem 1.5rem;
        background-color: #4caf50;
        color: white;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        font-weight: 500;
        transition: background-color 0.2s ease;
      }

      .add-button:hover {
        background-color: #45a049;
      }

      .stock-table {
        width: 100%;
        border-collapse: separate;
        border-spacing: 0;
        margin-top: 1.5rem;
      }

      .stock-table th,
      .stock-table td {
        padding: 1rem;
        text-align: left;
        border-bottom: 1px solid #eee;
      }

      .stock-table th {
        background-color: #f8f9fa;
        font-weight: 600;
        color: #2c3e50;
        white-space: nowrap;
      }

      .stock-table tr:last-child td {
        border-bottom: none;
      }

      .stock-table tbody tr {
        transition: background-color 0.2s ease;
      }

      .stock-table tbody tr:hover {
        background-color: #f8f9fa;
      }
    `,
  ],
})
export class StockListComponent implements OnInit {
  stocks: Stock[] = [];

  constructor(private stockService: StockService) {}

  ngOnInit() {
    this.stockService.getStocks().subscribe({
      next: (stocks) => (this.stocks = stocks),
      error: (error) => console.error('Error fetching stocks:', error),
    });
  }
}
