import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

export interface OrderDto {
  id?: number;
  tableNumber: number;
  items: string;
  status?: string;
  receivedAt?: string;
  imageUrl?: string;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private apiUrl = '/api/orders';

  constructor(private http: HttpClient) {}

  private handleError(error: HttpErrorResponse) {
    console.error('API Error Details:', {
      url: error.url,
      status: error.status,
      statusText: error.statusText,
      message: error.message,
      error: error.error,
    });
    return throwError(() => new Error(error.message || 'Server error'));
  }

  createOrder(order: OrderDto, image: File | null): Observable<OrderDto> {
    const formData = new FormData();
    formData.append('order', new Blob([JSON.stringify(order)], { type: 'application/json' }));
    if (image) {
      formData.append('image', image);
    }
    return this.http.post<OrderDto>(this.apiUrl, formData).pipe(catchError(this.handleError));
  }

  getAllOrders(page: number = 0, size: number = 10): Observable<Page<OrderDto>> {
    const params = new HttpParams().set('page', page.toString()).set('size', size.toString());
    return this.http
      .get<Page<OrderDto>>(this.apiUrl, { params })
      .pipe(catchError(this.handleError));
  }

  getOrderById(id: number): Observable<OrderDto> {
    return this.http.get<OrderDto>(`${this.apiUrl}/${id}`).pipe(catchError(this.handleError));
  }

  deleteOrder(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
