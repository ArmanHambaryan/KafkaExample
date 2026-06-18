import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Page } from './order.service';

export interface KitchenOrderDto {
  id?: number;
  orderId: number;
  dishName: string;
  kitchenStatus: string;
  imageUrl?: string;
}

@Injectable({
  providedIn: 'root'
})
export class KitchenService {
  private apiUrl = '/api/kitchen/orders';

  constructor(private http: HttpClient) {}

  private handleError(error: HttpErrorResponse) {
    console.error('Kitchen API Error:', {
      url: error.url,
      status: error.status,
      message: error.message,
      error: error.error
    });
    return throwError(() => new Error(error.message || 'Server error'));
  }

  getAllKitchenOrders(page: number = 0, size: number = 10): Observable<Page<KitchenOrderDto>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Page<KitchenOrderDto>>(this.apiUrl, { params }).pipe(
      catchError(this.handleError)
    );
  }

  updateStatus(id: number, status: string): Observable<KitchenOrderDto> {
    const params = new HttpParams().set('status', status);
    return this.http.put<KitchenOrderDto>(`${this.apiUrl}/${id}/status`, null, { params }).pipe(
      catchError(this.handleError)
    );
  }

  deleteKitchenOrder(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }
}
