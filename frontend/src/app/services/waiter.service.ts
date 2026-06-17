import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Page } from './order.service';
import { KitchenOrderDto } from './kitchen.service';
export type { KitchenOrderDto };

@Injectable({
  providedIn: 'root',
})
export class WaiterService {
  private apiUrl = '/api/waiter/orders';

  constructor(private http: HttpClient) {}

  private handleError(error: HttpErrorResponse) {
    console.error('Waiter API Error:', {
      url: error.url,
      status: error.status,
      message: error.message,
      error: error.error,
    });
    return throwError(() => new Error(error.message || 'Server error'));
  }

  getOrders(page: number = 0, size: number = 10): Observable<Page<KitchenOrderDto>> {
    const params = new HttpParams().set('page', page.toString()).set('size', size.toString());

    return this.http
      .get<Page<KitchenOrderDto>>(this.apiUrl, { params })
      .pipe(catchError(this.handleError));
  }

  markAsDelivered(id: number, status: string): Observable<KitchenOrderDto> {
    const params = new HttpParams().set('status', status);
    return this.http
      .put<KitchenOrderDto>(`/api/kitchen/orders/${id}/status`, null, { params })
      .pipe(catchError(this.handleError));
  }
}
