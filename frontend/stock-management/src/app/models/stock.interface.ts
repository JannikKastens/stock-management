export interface Stock {
  id: number;
  tickerSymbol: string;
  name: string;
  sektor: string;
  kaufDatum: Date;
  kaufPreis: number;
  anzahl: number;
}
