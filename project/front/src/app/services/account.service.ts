import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private apiUrl = 'http://localhost:8081/api';

  constructor(private http: HttpClient) {}

  createAccount(account: { cardNumber: string; name: string; balance: number; userId: number }): Observable<any> {
    return this.http.post(`${this.apiUrl}/accounts`, account);
  }

  getAccount(accountId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/accounts/${accountId}`);
  }

  updateAccount(accountId: number, account: { cardNumber: string; name: string; balance: number }): Observable<any> {
    return this.http.put(`${this.apiUrl}/accounts/${accountId}`, account);
  }

  deposit(accountId: number, amount: number, notes: string = ''): Observable<any> {
    return this.http.post(`${this.apiUrl}/transactions/deposit?accountId=${accountId}&amount=${amount}&notes=${encodeURIComponent(notes)}`, {});
  }

  withdraw(accountId: number, amount: number, notes: string = ''): Observable<any> {
    return this.http.post(`${this.apiUrl}/transactions/withdraw?accountId=${accountId}&amount=${amount}&notes=${encodeURIComponent(notes)}`, {});
  }

  getAccountHistory(accountId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/transactions/history/${accountId}`);
  }

  getAccountByUserId(userId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/accounts/user/${userId}`);
  }
}
