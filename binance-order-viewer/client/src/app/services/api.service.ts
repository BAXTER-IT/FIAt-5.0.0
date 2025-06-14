import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  getLimit(): Observable<any> {
    return this.http.get(`${this.baseUrl}/limit`);
  }

  updateLimit(limit: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/limit`, { limit });
  }
}
