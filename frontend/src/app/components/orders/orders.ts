import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { OrderService } from '../../services/order.service';
import { interval, Subscription } from 'rxjs';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './orders.html',
  styleUrls: ['./orders.css'],
})
export class OrdersComponent implements OnInit, OnDestroy {
  orders: any[] = [];
  orderForm: FormGroup;
  statusMessage: string = '';
  isError: boolean = false;
  selectedFile: File | null = null;

  currentPage: number = 0;
  totalPages: number = 0;
  totalElements: number = 0;
  pageSize: number = 10;

  private updateSubscription!: Subscription;

  constructor(
    private orderService: OrderService,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef,
  ) {
    this.orderForm = this.fb.group({
      tableNumber: ['', Validators.required],
      items: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.fetchOrders(this.currentPage);
    this.updateSubscription = interval(3000).subscribe(() => this.fetchOrders(this.currentPage));
  }

  fetchOrders(page: number = 0): void {
    this.orderService.getAllOrders(page, this.pageSize).subscribe({
      next: (data: any) => {
        if (!data) {
          this.orders = [];
          return;
        }

        let content = [];
        if (Array.isArray(data)) {
          content = data;
          this.totalElements = data.length;
          this.totalPages = 1;
        } else {
          content = data.content || [];
          this.totalElements = data.totalElements || 0;
          this.totalPages = data.totalPages || 0;
          this.currentPage = data.number || 0;
        }

        this.orders = content.map((order: any) => ({
          ...order,
          imageUrl: order.imageUrl || 'https://placehold.co/100x100?text=No+Image',
        }));

        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error fetching orders:', err),
    });
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.fetchOrders(this.currentPage);
    }
  }

  getPageNumbers(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i);
  }

  deleteOrder(id: number): void {
    if (confirm('Are you sure you want to delete this order?')) {
      this.orderService.deleteOrder(id).subscribe({
        next: () => {
          this.orders = this.orders.filter((o) => o.id !== id);
          this.statusMessage = 'Order deleted successfully!';
          this.isError = false;
          setTimeout(() => (this.statusMessage = ''), 3000);
        },
        error: (err) => {
          console.error('Delete error:', err);
          this.statusMessage = 'Failed to delete order.';
          this.isError = true;
        },
      });
    }
  }

  onSubmit(): void {
    if (this.orderForm.valid) {
      const orderData = this.orderForm.value;

      this.orderService.createOrder(orderData, this.selectedFile).subscribe({
        next: () => {
          this.statusMessage = 'Order placed successfully!';
          this.isError = false;
          this.orderForm.reset();
          this.selectedFile = null;
          this.fetchOrders(this.currentPage);
        },
        error: (err) => {
          console.error('Submit error:', err);
          this.statusMessage = 'Failed to place order.';
          this.isError = true;
        },
      });
    }
  }

  getStatusClass(status: string): any {
    return {
      'status-pending': status === 'PENDING',
      'status-ready': status === 'READY',
    };
  }

  ngOnDestroy(): void {
    if (this.updateSubscription) {
      this.updateSubscription.unsubscribe();
    }
  }

  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
    }
  }
}
