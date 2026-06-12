import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  title: string = 'Իմ առաջին Kafka Պրոյեկտ';

  showMessage(): void {
    alert('Բարև! Angular-ը աշխատում է կանչվելով!');
  }
}
