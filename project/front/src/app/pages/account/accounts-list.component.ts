import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccountService } from '../../services/account.service';

@Component({
  selector: 'app-accounts-list',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="accounts-list-container">
      <button *ngIf="!showAccount" (click)="showAccount = true" class="show-btn">Show My Account</button>
      <ng-container *ngIf="showAccount">
        <h2>My Account</h2>
        
        <div *ngIf="accountInfo" class="account-info">
          <div class="info-row">
            <span class="label">Card Number:</span>
            <span class="value">{{ accountInfo.cardNumber }}</span>
          </div>
          <div class="info-row">
            <span class="label">Balance:</span>
            <span class="value">{{ accountInfo.balance | number:'1.2-2' }} EGP</span>
          </div>
        </div>

        <div *ngIf="transactions && transactions.length > 0" class="transactions-section">
          <h3>Transaction History</h3>
          <table>
            <thead>
              <tr>
                <th>Type</th>
                <th>Amount</th>
                <th>Date</th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let t of transactions">
                <td>{{ t.type || t.transactionType }}</td>
                <td>{{ t.amount | number:'1.2-2' }}</td>
                <td>{{ t.date || t.timestamp || t.createdAt | date:'short' }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <div *ngIf="transactions && transactions.length === 0" class="no-transactions">
          No transactions found.
        </div>

        <div *ngIf="error" class="error">{{ error }}</div>
      </ng-container>
    </div>
  `,
  styles: [`
    .accounts-list-container { max-width: 700px; margin: 40px auto; background: #fff; border-radius: 10px; box-shadow: 0 2px 12px #0001; padding: 32px 24px; }
    h2 { text-align: center; margin-bottom: 24px; }
    .show-btn { display: block; margin: 40px auto 0 auto; font-size: 1.2rem; padding: 12px 32px; background: #1976d2; color: #fff; border: none; border-radius: 8px; cursor: pointer; }
    .show-btn:hover { background: #1256a3; }
    .account-info { background: #f8f9fa; padding: 20px; border-radius: 8px; margin-bottom: 24px; }
    .info-row { display: flex; justify-content: space-between; margin-bottom: 12px; }
    .label { font-weight: bold; color: #555; }
    .value { color: #1976d2; font-weight: 600; }
    table { width: 100%; border-collapse: collapse; margin-top: 16px; }
    th, td { padding: 10px 8px; border-bottom: 1px solid #eee; text-align: left; }
    th { background: #f5f5f5; }
    .no-transactions { color: #888; text-align: center; margin-top: 24px; }
    .error { color: #d32f2f; text-align: center; margin-top: 16px; }
    .transactions-section { margin-top: 24px; }
  `]
})
export class AccountsListComponent {
  accountInfo: any = null;
  transactions: any[] = [];
  error = '';
  private _showAccount = false;

  constructor(private accountService: AccountService) {}

  get showAccount() {
    return this._showAccount;
  }
  set showAccount(val: boolean) {
    if (val && !this.accountInfo) {
      this.fetchAccountInfo();
    }
    this._showAccount = val;
  }

  fetchAccountInfo() {
    // Get balance
    this.accountService.getAccountBalance().subscribe({
      next: (balanceData) => {
        // Get card number
        this.accountService.getAccountCardNumber().subscribe({
          next: (cardData) => {
            this.accountInfo = {
              balance: balanceData.balance || balanceData,
              cardNumber: cardData.cardNumber || cardData
            };
            // Get transactions
            this.fetchTransactions();
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

  fetchTransactions() {
    this.accountService.getAccountTransactions().subscribe({
      next: (data) => {
        this.transactions = Array.isArray(data) ? data : (data?.transactions || []);
      },
      error: (err) => {
        this.transactions = [];
      }
    });
  }
} 