import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  DashboardService,
  PortfolioStats,
} from '../../services/dashboard.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  stats: PortfolioStats | null = null;
  loading = true;
  error: string | null = null;

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  private loadDashboardData(): void {
    this.dashboardService.getPortfolioStats().subscribe({
      next: (data) => {
        this.stats = data;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load dashboard data';
        this.loading = false;
        console.error('Dashboard error:', error);
      },
    });
  }

  getMaxDividend(): number {
    if (!this.stats?.monthlyDividends?.length) return 0;
    return Math.max(...this.stats.monthlyDividends.map((d) => d.amount));
  }
}
