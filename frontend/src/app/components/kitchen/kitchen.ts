import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { KitchenService, KitchenOrderDto } from '../../services/kitchen.service';

@Component({
  selector: 'app-kitchen',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './kitchen.html',
  styleUrl: './kitchen.css',
})
export class KitchenComponent implements OnInit {
  kitchenOrders: KitchenOrderDto[] = [];

  currentPage: number = 0;
  totalPages: number = 0;
  totalElements: number = 0;
  pageSize: number = 10;

  constructor(
    private kitchenService: KitchenService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.loadKitchenOrders(this.currentPage);
  }

  loadKitchenOrders(page: number = 0): void {
    this.kitchenService.getAllKitchenOrders(page, this.pageSize).subscribe({
      next: (data: any) => {
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

        this.kitchenOrders = content.map((kOrder: any) => {
          const orderId = kOrder.orderId;
          const cachedImage = localStorage.getItem(`order_image_${orderId}`);

          return {
            ...kOrder,
            imageUrl: kOrder.imageUrl || kOrder.image || cachedImage || 'assets/pizza.jpg'
          };
        });

        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error loading kitchen orders', err),
    });
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadKitchenOrders(this.currentPage);
    }
  }

  getPageNumbers(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i);
  }

  updateStatus(id: number, status: string): void {
    this.kitchenService.updateStatus(id, status).subscribe({
      next: () => this.loadKitchenOrders(this.currentPage),
      error: (err) => console.error('Error updating status', err),
    });
  }

  deleteOrder(id: number): void {
    if (confirm('Are you sure you want to delete this kitchen order?')) {
      this.kitchenService.deleteKitchenOrder(id).subscribe({
        next: () => this.loadKitchenOrders(this.currentPage),
        error: (err) => console.error('Error deleting order', err),
      });
    }
  }

  getStatusClass(status: string): string {
    return status.toLowerCase();
  }
}
