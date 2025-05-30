import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import { BehaviorSubject } from 'rxjs';
import { OrderBook } from '../models/order-book.model';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  private client: Client;
  private orderBookSubject = new BehaviorSubject<OrderBook | null>(null);
  
  orderBook$ = this.orderBookSubject.asObservable();

  constructor() {
    this.initializeWebSocketConnection();
  }

  private initializeWebSocketConnection() {
    this.client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      debug: (msg: string) => {
        console.log(msg);
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000
    });

    this.client.onConnect = () => {
      console.log('Connected to WebSocket');
      
      this.client.subscribe('/topic/orderbook', (message) => {
        if (message.body) {
          const orderBookData: OrderBook = JSON.parse(message.body);
          this.orderBookSubject.next(orderBookData);
        }
      });
    };

    this.client.onStompError = (frame) => {
      console.error('STOMP error', frame);
    };

    this.client.activate();
  }

  disconnect() {
    if (this.client) {
      this.client.deactivate();
    }
  }
}
