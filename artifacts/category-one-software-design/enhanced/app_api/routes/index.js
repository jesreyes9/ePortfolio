const express = require('express'); // Express app
const router = express.Router();    // Router logic

// This is where we import the controllers we will route
const tripsController = require('../controllers/trips');
const bookingsController = require('../controllers/bookings');
const authController = require('../controllers/authentication');
const { authenticateJWT, authorizeRoles } = require('../middleware/auth');

// define route for login endpoint
router
    .route('/login')
    .post(authController.login);

// define route for registration endpoint
router
    .route('/register')
    .post(authController.register);

// define route for our trips endpoint
router
    .route('/trips')
    .get(tripsController.tripsList)               // GET Method routes tripList
    .post(authenticateJWT, authorizeRoles('admin', 'agent'), tripsController.tripsAddTrip); // POST Method Adds a Trip

// GET Method routes tripsFindByCode - requires parameter
// PUT Method routes tripsUpdateTrip - requires parameter
router
    .route('/trips/:tripCode')
    .get(tripsController.tripsFindByCode)
    .put(authenticateJWT, authorizeRoles('admin', 'agent'), tripsController.tripsUpdateTrip);

// define routes for our bookings endpoint
router
    .route('/bookings')
    .get(authenticateJWT, bookingsController.bookingsList)
    .post(authenticateJWT, authorizeRoles('admin', 'agent'), bookingsController.bookingsAddBooking);

router
    .route('/bookings/:bookingId')
    .get(authenticateJWT, bookingsController.bookingsFindById);

router
    .route('/bookings/:bookingId/cancel')
    .put(authenticateJWT, authorizeRoles('admin', 'agent'), bookingsController.bookingsCancelBooking);

module.exports = router;
