import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { BookingDataService } from '../services/booking-data.service';

@Component({
  selector: 'app-add-booking',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-booking.component.html',
  styleUrl: './add-booking.component.css'
})
export class AddBookingComponent implements OnInit {
  public addForm!: FormGroup;
  submitted = false;
  serverError = '';

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private bookingService: BookingDataService
  ) {}

  ngOnInit() {
    this.addForm = this.formBuilder.group({
      tripCode: ['', Validators.required],
      customerName: ['', Validators.required],
      customerEmail: ['', [Validators.required, Validators.email]],
      seats: [1, [Validators.required, Validators.min(1)]]
    });
  }

  public onSubmit() {
    this.submitted = true;
    this.serverError = '';
    if (this.addForm.valid) {
      this.bookingService.addBooking(this.addForm.value)
        .subscribe({
          next: (data: any) => {
            this.router.navigate(['bookings']);
          },
          error: (error: any) => {
            this.serverError = error?.error?.message || 'Unable to create booking.';
          }
        });
    }
  }

  get f() { return this.addForm.controls; }
}
