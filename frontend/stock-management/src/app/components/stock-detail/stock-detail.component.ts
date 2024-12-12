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
  templateUrl: './stock-detail.component.html',
  styleUrls: ['./stock-detail.component.scss'],
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
