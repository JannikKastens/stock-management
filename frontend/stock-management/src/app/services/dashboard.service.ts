import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PortfolioStats {
  totalValue: number;
  totalDividends: number;
  stockCount: number;
  monthlyDividends: { month: string; amount: number }[];
}

@Injectable({
  providedIn: 'root',
})
export class DashboardService {
  private apiUrl = 'http://localhost:8080/api/v1';

  constructor(private http: HttpClient) {}

  getPortfolioStats(): Observable<PortfolioStats> {
    return this.http.get<PortfolioStats>(`${this.apiUrl}/dashboard/stats`);
  }

  getMonthlyDividends(): Observable<{ month: string; amount: number }[]> {
    return this.http.get<{ month: string; amount: number }[]>(
      `${this.apiUrl}/dashboard/monthly-dividends`
    );
  }
}
