export interface OrderBook {
  lastUpdateId: number;
  bids: PriceQuantityPair[];
  asks: PriceQuantityPair[];
}

export interface PriceQuantityPair {
  price: string;
  quantity: string;
  isManuallyAdded?: boolean;
}
