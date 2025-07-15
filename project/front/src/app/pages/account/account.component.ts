import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AccountService } from '../../services/account.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './account.component.html',
  styleUrl: './account.component.css'
})
export class AccountComponent {
  accountId: number | null = null;
  showDeposit = false;
  showWithdraw = false;
  editMode = false;
  balance: number = 0;
  name: string = '';
  cardNumber: string = '';
  depositAmount: number = 0;
  depositNotes: string = '';
  withdrawAmount: number = 0;
  withdrawNotes: string = '';
  message: string = '';
  error: string = '';
  transactions: any[] = [];
  userName: string = '';

  // For editing
  editName = '';
  editCardNumber = '';
  editBalance: number = 0;

  openAddAccount = false;
  addCardNumber = '';
  addName = '';
  addBalance: number | null = null;
  addMessage = '';
  addError = '';

  constructor(private route: ActivatedRoute, private accountService: AccountService, private router: Router) {
    this.route.queryParams.subscribe(params => {
      this.accountId = params['id'] ? +params['id'] : null;
      if (this.accountId) {
        this.loadAccount();
        this.loadHistory();
      } else {
        this.userName = localStorage.getItem('userName') || '';
      }
    });
  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.accountId = params['id'] ? +params['id'] : null;
      if (this.accountId) {
        this.loadAccount();
        this.loadHistory();
      } else {
        // جلب userId من التوكن
        const userId = this.getUserIdFromToken();
        if (userId) {
          this.accountService.getAccountByUserId(userId).subscribe({
            next: (res: any) => {
              console.log('getAccountByUserId response:', res);
              let acc = null;
              if (Array.isArray(res)) {
                acc = res[0];
              } else if (res && res.id) {
                acc = res;
              } else if (res && res.data) {
                acc = Array.isArray(res.data) ? res.data[0] : res.data;
              }
              if (acc && acc.id) {
                this.accountId = acc.id;
                this.loadAccount();
                this.loadHistory();
              } else {
                this.name = '';
                this.cardNumber = '';
                this.balance = 0;
                this.transactions = [];
              }
            },
            error: (err) => {
              console.error('getAccountByUserId error:', err);
              this.name = '';
              this.cardNumber = '';
              this.balance = 0;
              this.transactions = [];
            }
          });
        }
      }
    });
  }

  loadAccount() {
    if (!this.accountId) return;
    this.accountService.getAccount(this.accountId).subscribe({
      next: (res: any) => {
        this.name = res.data?.name || '';
        this.cardNumber = res.data?.cardNumber || '';
        this.balance = res.data?.balance || 0;
        // For editing
        this.editName = this.name;
        this.editCardNumber = this.cardNumber;
        this.editBalance = this.balance;
      },
      error: () => {
        this.error = 'Failed to load account data';
      }
    });
  }

  loadHistory() {
    if (!this.accountId) return;
    this.accountService.getAccountHistory(this.accountId).subscribe({
      next: (res: any) => {
        this.transactions = res.data || res || [];
      },
      error: () => {
        this.transactions = [];
      }
    });
  }

  openEdit() {
    this.editMode = true;
    this.message = '';
    this.error = '';
  }

  cancelEdit() {
    this.editMode = false;
    this.editName = this.name;
    this.editCardNumber = this.cardNumber;
    this.editBalance = this.balance;
  }

  saveEdit() {
    if (!this.accountId) return;
    this.accountService.updateAccount(this.accountId, {
      name: this.editName,
      cardNumber: this.editCardNumber,
      balance: this.editBalance
    }).subscribe({
      next: (res: any) => {
        this.message = res.message || 'Account updated successfully';
        this.editMode = false;
        this.loadAccount();
      },
      error: () => {
        this.error = 'Failed to update account';
      }
    });
  }

  openDeposit() {
    this.showDeposit = true;
    this.showWithdraw = false;
    this.message = '';
    this.error = '';
  }

  openWithdraw() {
    this.showWithdraw = true;
    this.showDeposit = false;
    this.message = '';
    this.error = '';
  }

  closeForms() {
    this.showDeposit = false;
    this.showWithdraw = false;
    this.depositAmount = 0;
    this.depositNotes = '';
    this.withdrawAmount = 0;
    this.withdrawNotes = '';
    this.message = '';
    this.error = '';
  }

  submitDeposit() {
    if (!this.accountId || !this.depositAmount) return;
    this.accountService.deposit(this.accountId, this.depositAmount, this.depositNotes).subscribe({
      next: (res: any) => {
        this.message = res.message || 'Deposit successful';
        this.balance = res.data?.balanceAfter || this.balance;
        this.closeForms();
        this.loadAccount();
      },
      error: () => {
        this.error = 'Deposit failed';
      }
    });
  }

  submitWithdraw() {
    if (!this.accountId || !this.withdrawAmount) return;
    this.accountService.withdraw(this.accountId, this.withdrawAmount, this.withdrawNotes).subscribe({
      next: (res: any) => {
        this.message = res.message || 'Withdraw successful';
        this.balance = res.data?.balanceAfter || this.balance;
        this.closeForms();
        this.loadAccount();
      },
      error: () => {
        this.error = 'Withdraw failed';
      }
    });
  }

  goToHome() {
    this.router.navigate(['/home']);
  }

  submitAddAccount() {
    this.addMessage = '';
    this.addError = '';
    const userId = this.getUserIdFromToken();
    if (!this.addCardNumber || !this.addName || !this.addBalance || !userId) {
      this.addError = 'All fields are required';
      return;
    }
    this.accountService.createAccount({
      cardNumber: this.addCardNumber,
      name: this.addName,
      balance: this.addBalance,
      userId
    }).subscribe({
      next: (res: any) => {
        this.addMessage = res.message || 'Account created successfully';
        this.openAddAccount = false;
        this.loadAccount();
        this.loadHistory();
      },
      error: () => {
        this.addError = 'Failed to create account';
      }
    });
  }

  getUserIdFromToken(): number | null {
    const token = localStorage.getItem('accessToken');
    if (!token) return null;
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.userId || payload.userID || payload.userid || payload.id || null;
    } catch {
      return null;
    }
  }
}
