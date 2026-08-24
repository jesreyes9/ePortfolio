const mongoose = require('mongoose');

const bookingSchema = new mongoose.Schema({
    tripCode: { type: String, required: true, index: true },
    tripName: { type: String, required: true },
    customerName: { type: String, required: true },
    customerEmail: { type: String, required: true },
    seats: { type: Number, required: true, min: 1 },
    status: { type: String, enum: ['confirmed', 'cancelled'], default: 'confirmed' },
    createdBy: { type: mongoose.Schema.Types.ObjectId, ref: 'users', required: true }
}, { timestamps: true });

const Booking = mongoose.model('bookings', bookingSchema);
module.exports = Booking;
