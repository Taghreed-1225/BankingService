import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BankingService {
  private apiUrl = 'http://localhost:8081/api';

  constructor(private http: HttpClient) {}

  // Get account balance for current user
  getAccountBalance(): Observable<any> {
    return this.http.get(`${this.apiUrl}/accounts/balance`);
  }

  // Get account transactions for current user
  getAccountTransactions(): Observable<any> {
    return this.http.get(`${this.apiUrl}/accounts/transactions`);
  }

  // Get account card number for current user
  getAccountCardNumber(): Observable<any> {
    return this.http.get(`${this.apiUrl}/accounts/cardNumber`);
  }

  // Deposit money
  depositMoney(cardNumber: string, amount: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/transactions/deposit`, {
      cardNumber: cardNumber,
      amount: amount
    });
  }

  // Withdraw money
  withdrawMoney(cardNumber: string, amount: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/transactions/withdraw`, {
      cardNumber: cardNumber,
      amount: amount
    });
  }

  // Validate token
  validateToken(): Observable<any> {
    return this.http.get(`http://localhost:8081/bank/token/validation`);
  }
}
