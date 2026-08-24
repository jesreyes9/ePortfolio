import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { BookingDataService } from '../services/booking-data.service';
import { AuthenticationService } from '../services/authentication.service';
import { Booking } from '../models/booking';

@Component({
    selector: 'app-booking-list',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './booking-list.component.html',
    styleUrl: './booking-list.component.css',
    providers: [BookingDataService]
})
export class BookingListComponent implements OnInit {

    bookings!: Booking[];
    message: string = '';

    constructor(
        private bookingDataService: BookingDataService,
        private authenticationService: AuthenticationService,
        private router: Router
    ) {}

    private getStuff(): void {
        this.bookingDataService.getBookings()
            .subscribe({
                next: (value: any) => {
                    this.bookings = value;
                    this.message = value.length > 0
                        ? 'There are ' + value.length + ' bookings on record.'
                        : 'There are no bookings yet.';
                },
                error: (error: any) => {
                    console.log('Error: ' + error);
                }
            });
    }

    ngOnInit(): void {
        this.getStuff();
    }

    public addBooking(): void {
        this.router.navigate(['add-booking']);
    }

    public cancelBooking(booking: Booking): void {
        this.bookingDataService.cancelBooking(booking._id)
            .subscribe({
                next: () => {
                    this.getStuff();
                },
                error: (error: any) => {
                    console.log('Error: ' + error);
                }
            });
    }

    public canManageBookings(): boolean {
        return this.authenticationService.canManageBookings();
    }
}
