import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { OrderBook } from '../../models/order-book.model';
import { WebsocketService } from '../../services/websocket.service';

@Component({
  selector: 'app-order-book',
  templateUrl: './order-book.component.html',
  styleUrls: ['./order-book.component.scss']
})
export class OrderBookComponent implements OnInit, OnDestroy {
  orderBook: OrderBook | null = null;
  private subscription: Subscription | null = null;

  constructor(private websocketService: WebsocketService) { }

  ngOnInit(): void {
    this.subscription = this.websocketService.orderBook$.subscribe(
      (data) => {
        this.orderBook = data;
      }
    );
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
