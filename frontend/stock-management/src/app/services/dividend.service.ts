import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Dividend } from '../models/dividend.interface';

@Injectable({
  providedIn: 'root',
})
export class DividendService {
  private apiUrl = 'http://localhost:8080/api/v1/dividends';

  constructor(private http: HttpClient) {}

  getDividendsByStockId(stockId: number): Observable<Dividend[]> {
    return this.http.get<Dividend[]>(`${this.apiUrl}/stock/${stockId}`);
  }

  createDividend(
    stockId: number,
    dividend: Omit<Dividend, 'id'>
  ): Observable<Dividend> {
    return this.http.post<Dividend>(
      `${this.apiUrl}/stock/${stockId}`,
      dividend
    );
  }

  updateDividend(
    id: number,
    dividend: Omit<Dividend, 'id'>
  ): Observable<Dividend> {
    return this.http.put<Dividend>(`${this.apiUrl}/${id}`, dividend);
  }

  deleteDividend(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
