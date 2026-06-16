import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WaiterService } from '../../services/waiter.service';
import { KitchenOrderDto } from '../../services/kitchen.service';

@Component({
  selector: 'app-waiter',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './waiter.html',
  styleUrl: './waiter.css',
})
export class WaiterComponent implements OnInit {
  readyOrders: KitchenOrderDto[] = [];
  isLoading: boolean = false;

  currentPage: number = 0;
  totalPages: number = 0;
  totalElements: number = 0;
  pageSize: number = 10;

  constructor(
    private waiterService: WaiterService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.loadReadyOrders(this.currentPage);
  }

  loadReadyOrders(page: number = 0): void {
    this.isLoading = true;
    this.waiterService.getOrders(page, this.pageSize).subscribe({
      next: (data: any) => {
        this.isLoading = false;
        let content = data && data.content ? data.content : Array.isArray(data) ? data : [];
        this.totalElements = data.totalElements || 0;
        this.totalPages = data.totalPages || 0;
        this.currentPage = data.number || 0;

        // Enrich waiter orders with cached images from localStorage
        this.readyOrders = content.map((order: any) => {
          const orderId = order.orderId;
          const cachedImage = localStorage.getItem(`order_image_${orderId}`);
          
          return { 
            ...order, 
            imageUrl: order.imageUrl || order.image || cachedImage || 'assets/pizza.jpg'
          };
        });

        this.cdr.detectChanges();
      },
      error: (err) => {
        this.isLoading = false;
        console.error('Error loading ready orders:', err);
      },
    });
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadReadyOrders(this.currentPage);
    }
  }

  getPageNumbers(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i);
  }

  markAsDelivered(id: number): void {
    this.waiterService.markAsDelivered(id, 'DELIVERED').subscribe({
      next: () => {
        this.readyOrders = this.readyOrders.filter((o) => o.id !== id);
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error marking as delivered:', err),
    });
  }
}
