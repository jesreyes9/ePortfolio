export interface Booking {
  _id: string;
  tripCode: string;
  tripName: string;
  customerName: string;
  customerEmail: string;
  seats: number;
  status: string;
  createdAt?: Date;
}
