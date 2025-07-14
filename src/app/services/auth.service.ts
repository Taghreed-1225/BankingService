import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:9994/rest/auth';
  private tokenKey = 'accessToken';

  constructor(private http: HttpClient, private router: Router) { }

  login(email: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, { email, password }).pipe(
      tap(res => {
        if (res.accessToken) {
          localStorage.setItem(this.tokenKey, res.accessToken);
          localStorage.setItem('userEmail', res.email);
        }
      })
    );
  }

  register(name: string, email: string, phone: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/register`, { 
      name, 
      email, 
      phone, 
      password 
    });
  }

  verifyOtp(email: string, otp: string): Observable<any> {
    // Clean email and OTP
    const cleanEmail = email.trim().toLowerCase();
    const cleanOtp = otp.trim();
    
    console.log('Verifying OTP:', { email: cleanEmail, otp: cleanOtp });
    
    return this.http.post<any>(`${this.apiUrl}/verify?email=${encodeURIComponent(cleanEmail)}&otp=${cleanOtp}`, {});
  }

  logout() {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem('userEmail');
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem(this.tokenKey);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }
}
