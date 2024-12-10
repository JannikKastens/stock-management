import { Routes } from '@angular/router';
import { StockListComponent } from './components/stock-list/stock-list.component';

export const routes: Routes = [
  { path: '', redirectTo: '/stocks', pathMatch: 'full' },
  { path: 'stocks', component: StockListComponent },
];
