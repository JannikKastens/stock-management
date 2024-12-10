export interface Stock {
  id: number;
  tickerSymbol: string;
  name: string;
  sector: string;
  purchaseDate: Date;
  purchasePrice: number;
  amount: number;
}
