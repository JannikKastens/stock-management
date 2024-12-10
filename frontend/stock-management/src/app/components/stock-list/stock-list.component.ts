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
        padding: 20px;
      }

      .actions {
        margin: 20px 0;
      }

      .add-button {
        padding: 10px 20px;
        background-color: #4caf50;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
      }

      .add-button:hover {
        background-color: #45a049;
      }

      .stock-table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 20px;
      }

      .stock-table th,
      .stock-table td {
        padding: 12px;
        text-align: left;
        border-bottom: 1px solid #ddd;
      }

      .stock-table th {
        background-color: #f5f5f5;
        font-weight: bold;
      }

      .stock-table tr:hover {
        background-color: #f5f5f5;
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
