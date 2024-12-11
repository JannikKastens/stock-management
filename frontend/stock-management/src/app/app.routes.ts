import { Routes } from '@angular/router';
import { StockListComponent } from './components/stock-list/stock-list.component';
import { StockFormComponent } from './components/stock-form/stock-form.component';
import { StockDetailComponent } from './components/stock-detail/stock-detail.component';
import { DividendFormComponent } from './components/dividend-form/dividend-form.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'stocks', component: StockListComponent },
  { path: 'stocks/new', component: StockFormComponent },
  { path: 'stocks/:id', component: StockDetailComponent },
  { path: 'stocks/:stockId/dividends/new', component: DividendFormComponent },
];
