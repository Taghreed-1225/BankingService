import { HttpClient, HttpHeaders, HTTP_INTERCEPTORS, HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Injectable, Provider } from '@angular/core';
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
          if (res.name) localStorage.setItem('userName', res.name);
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
    }).pipe(
      tap(res => {
        if (res.name) localStorage.setItem('userName', res.name);
      })
    );
  }

  verifyOtp(email: string, otp: string): Observable<any> {
    const cleanEmail = email.trim().toLowerCase();
    const cleanOtp = otp.trim();
    return this.http.post<any>(`${this.apiUrl}/verify?email=${encodeURIComponent(cleanEmail)}&otp=${cleanOtp}`, {});
  }

  logout() {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem('userEmail');
    localStorage.removeItem('userName');
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem(this.tokenKey);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }
}

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('accessToken');
    if (token) {
      const cloned = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
      return next.handle(cloned);
    }
    return next.handle(req);
  }
}

export const authInterceptorProvider: Provider = {
  provide: HTTP_INTERCEPTORS,
  useClass: AuthInterceptor,
  multi: true
};
