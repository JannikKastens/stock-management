import { Routes } from '@angular/router';
import { StockListComponent } from './components/stock-list/stock-list.component';
import { StockFormComponent } from './components/stock-form/stock-form.component';

export const routes: Routes = [
  { path: '', redirectTo: '/stocks', pathMatch: 'full' },
  { path: 'stocks', component: StockListComponent },
  { path: 'stocks/new', component: StockFormComponent },
];
