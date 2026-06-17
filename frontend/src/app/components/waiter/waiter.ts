import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WaiterService, KitchenOrderDto } from '../../services/waiter.service';
import { interval, Subscription } from 'rxjs';

@Component({
  selector: 'app-waiter',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './waiter.html',
  styleUrl: './waiter.css',
})
export class WaiterComponent implements OnInit, OnDestroy {
  readyOrders: KitchenOrderDto[] = [];

  currentPage: number = 0;
  totalPages: number = 0;
  totalElements: number = 0;
  pageSize: number = 10;

  private updateSubscription!: Subscription;

  constructor(
    private waiterService: WaiterService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.loadReadyOrders(this.currentPage);
    this.updateSubscription = interval(3000).subscribe(() =>
      this.loadReadyOrders(this.currentPage),
    );
  }

  getImageUrl(imageUrl: string | null | undefined): string {
    if (!imageUrl) return '';
    return imageUrl;
  }

  loadReadyOrders(page: number = 0): void {
    this.waiterService.getOrders(page, this.pageSize).subscribe({
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

        this.readyOrders = content.map((order: any) => ({
          ...order,
          imageUrl: this.getImageUrl(order.imageUrl || order.image),
        }));

        this.cdr.detectChanges();
      },
      error: (err: any) => console.error('Error loading ready orders', err),
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
      next: () => this.loadReadyOrders(this.currentPage),
      error: (err: any) => console.error('Error marking as delivered', err),
    });
  }

  ngOnDestroy(): void {
    if (this.updateSubscription) {
      this.updateSubscription.unsubscribe();
    }
  }
}
