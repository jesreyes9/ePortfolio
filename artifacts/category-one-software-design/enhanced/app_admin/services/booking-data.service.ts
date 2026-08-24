import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Booking } from '../models/booking';

@Injectable({
    providedIn: 'root'
})
export class BookingDataService {

    constructor(
        private http: HttpClient
    ) {}

    url = 'http://localhost:3000/api/bookings';

    getBookings(): Observable<Booking[]> {
        return this.http.get<Booking[]>(this.url);
    }

    addBooking(formData: Booking): Observable<Booking> {
        return this.http.post<Booking>(this.url, formData);
    }

    cancelBooking(bookingId: string): Observable<Booking> {
        return this.http.put<Booking>(this.url + '/' + bookingId + '/cancel', {});
    }
}
