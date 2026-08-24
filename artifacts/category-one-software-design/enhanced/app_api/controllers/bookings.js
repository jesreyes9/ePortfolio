const Booking = require('../models/booking');
const Trip = require('../models/travlr');

// Confirms the trip exists, the trip hasn't already started, and there is
// enough remaining capacity for the requested number of seats.
async function validateBookingRequest(data) {
    const errors = [];
    if (!data.tripCode) errors.push('tripCode is required');
    if (!data.customerName) errors.push('customerName is required');
    if (!data.customerEmail) errors.push('customerEmail is required');

    const seats = Number(data.seats);
    if (!Number.isInteger(seats) || seats < 1) {
        errors.push('seats must be a positive integer');
    }
    if (errors.length) {
        return { valid: false, errors };
    }

    const trip = await Trip.findOne({ code: data.tripCode }).exec();
    if (!trip) {
        return { valid: false, errors: ['Trip not found'] };
    }
    if (new Date(trip.start) < new Date()) {
        return { valid: false, errors: ['Trip has already started'] };
    }

    const totals = await Booking.aggregate([
        { $match: { tripCode: data.tripCode, status: 'confirmed' } },
        { $group: { _id: null, total: { $sum: '$seats' } } }
    ]);
    const alreadyBooked = totals.length ? totals[0].total : 0;
    const remaining = trip.maxTravelers - alreadyBooked;
    if (seats > remaining) {
        return { valid: false, errors: [`Only ${remaining} seat(s) remaining on this trip`] };
    }

    return { valid: true, trip, seats };
}

// GET: /bookings - lists all bookings, optionally filtered by trip code
const bookingsList = async (req, res) => {
    const filter = req.query.tripCode ? { tripCode: req.query.tripCode } : {};
    const bookings = await Booking.find(filter).sort({ createdAt: -1 }).exec();
    return res.status(200).json(bookings);
};

// GET: /bookings/:bookingId - a single booking
const bookingsFindById = async (req, res) => {
    const booking = await Booking.findById(req.params.bookingId).exec();
    if (!booking) {
        return res.status(404).json({ message: 'Booking not found' });
    }
    return res.status(200).json(booking);
};

// POST: /bookings - creates a booking after validating the trip and capacity
const bookingsAddBooking = async (req, res) => {
    const { valid, errors, trip, seats } = await validateBookingRequest(req.body);
    if (!valid) {
        return res.status(400).json({ message: errors.join(', ') });
    }

    const booking = new Booking({
        tripCode: trip.code,
        tripName: trip.name,
        customerName: req.body.customerName,
        customerEmail: req.body.customerEmail,
        seats,
        createdBy: req.auth._id
    });

    try {
        const saved = await booking.save();
        return res.status(201).json(saved);
    } catch (err) {
        return res.status(400).json({ message: err.message });
    }
};

// PUT: /bookings/:bookingId/cancel - marks a booking cancelled
const bookingsCancelBooking = async (req, res) => {
    const booking = await Booking.findById(req.params.bookingId).exec();
    if (!booking) {
        return res.status(404).json({ message: 'Booking not found' });
    }
    booking.status = 'cancelled';
    const saved = await booking.save();
    return res.status(200).json(saved);
};

module.exports = {
    bookingsList,
    bookingsFindById,
    bookingsAddBooking,
    bookingsCancelBooking
};
