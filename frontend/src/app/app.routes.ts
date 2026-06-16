import { Routes } from '@angular/router';
import { OrdersComponent } from './components/orders/orders';
import { KitchenComponent } from './components/kitchen/kitchen';
import { WaiterComponent } from './components/waiter/waiter';

export const routes: Routes = [
  { path: '', redirectTo: 'orders', pathMatch: 'full' },
  { path: 'orders', component: OrdersComponent },
  { path: 'kitchen', component: KitchenComponent },
  { path: 'waiter', component: WaiterComponent },
];
