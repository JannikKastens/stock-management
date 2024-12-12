import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  Validators,
  FormGroup,
} from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { DividendService } from '../../services/dividend.service';
import { Dividend } from '../../models/dividend.interface';

@Component({
  selector: 'app-dividend-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './dividend-form.component.html',
  styleUrls: ['./dividend-form.component.scss'],
})
export class DividendFormComponent implements OnInit {
  dividendForm: FormGroup = new FormGroup({});

  private stockId?: number;

  constructor(
    private fb: FormBuilder,
    private dividendService: DividendService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.dividendForm = this.fb.group({
      amount: ['', [Validators.required, Validators.min(0.01)]],
      date: ['', Validators.required],
      currency: ['EUR', Validators.required],
    });
    this.stockId = Number(this.route.snapshot.paramMap.get('stockId'));
  }

  onSubmit() {
    if (this.dividendForm.valid && this.stockId) {
      const formValues = this.dividendForm.value;
      const dividend = {
        amount: Number(formValues.amount),
        date: new Date(formValues.date as string),
        currency: formValues.currency as string,
        stockId: this.stockId,
      } as Omit<Dividend, 'id'>;

      this.dividendService.createDividend(this.stockId, dividend).subscribe({
        next: () => {
          this.router.navigate(['/stocks', this.stockId]);
        },
        error: (error) => console.error('Error creating dividend:', error),
      });
    }
  }

  onCancel() {
    if (this.stockId) {
      this.router.navigate(['/stocks', this.stockId]);
    }
  }
}
