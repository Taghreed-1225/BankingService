import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { BankingService } from '../../services/banking.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  accountInfo: any = {
    balance: 0,
    cardNumber: ''
  };
  transactions: any[] = [];
  loading = true;
  error = '';

  constructor(
    private authService: AuthService,
    private bankingService: BankingService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadDashboardData();
  }

  loadDashboardData() {
    this.loading = true;
    this.error = '';

    // Load account balance
    this.bankingService.getAccountBalance().subscribe({
      next: (balanceData) => {
        this.accountInfo.balance = balanceData.balance || balanceData;
        
        // Load card number
        this.bankingService.getAccountCardNumber().subscribe({
          next: (cardData) => {
            this.accountInfo.cardNumber = cardData.cardNumber || cardData;
            
            // Load transactions
            this.loadTransactions();
          },
          error: (err) => {
            this.error = 'Failed to load card number.';
            this.loading = false;
          }
        });
      },
      error: (err) => {
        this.error = 'Failed to load account data.';
        this.loading = false;
      }
    });
  }

  loadTransactions() {
    this.bankingService.getAccountTransactions().subscribe({
      next: (response) => {
        // Try different possible structures
        let transactionsData = response;
        
        if (response && typeof response === 'object') {
          // Check for nested structures
          if (response.transactions) {
            transactionsData = response.transactions;
          } else if (response.data && response.data.transactions) {
            transactionsData = response.data.transactions;
          } else if (response.data && Array.isArray(response.data)) {
            transactionsData = response.data;
          }
        }
        
        // Ensure it's an array and map the fields correctly
        if (Array.isArray(transactionsData)) {
          this.transactions = transactionsData.map(transaction => ({
            id: transaction.id,
            type: transaction.transaction_type, // Map transaction_type to type
            amount: transaction.amount,
            date: transaction.created_at, // Map created_at to date
            paymentMethod: transaction.payment_method,
            // Keep original fields as fallback
            transactionType: transaction.transaction_type,
            timestamp: transaction.created_at,
            createdAt: transaction.created_at
          }));
        } else {
          this.transactions = [];
        }
        
        this.loading = false;
      },
      error: (err) => {
        this.transactions = [];
        this.loading = false;
      }
    });
  }

  logout() {
    this.authService.logout();
  }
}
