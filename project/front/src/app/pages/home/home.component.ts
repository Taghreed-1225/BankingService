import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AccountService } from '../../services/account.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-home',
  standalone: true,
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
  imports: [CommonModule, FormsModule]
})
export class HomeComponent {
  accountInfo: any = null;
  error = '';

  constructor(private router: Router, private accountService: AccountService) {
    this.loadAccountInfo();
  }

  loadAccountInfo() {
    // Get account balance
    this.accountService.getAccountBalance().subscribe({
      next: (balanceData) => {
        // Get card number
        this.accountService.getAccountCardNumber().subscribe({
          next: (cardData) => {
            this.accountInfo = {
              balance: balanceData.balance || balanceData,
              cardNumber: cardData.cardNumber || cardData
            };
          },
          error: (err) => {
            this.error = 'Failed to load card number.';
          }
        });
      },
      error: (err) => {
        this.error = 'Failed to load account balance.';
      }
    });
  }

  goToAccount() {
    this.router.navigate(['/accounts']);
  }
}
