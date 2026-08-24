const jwt = require('jsonwebtoken');

// Verifies the Bearer token on the Authorization header and attaches the
// decoded payload to req.auth. Only calls next() once verification has
// actually completed, and rejects any request that isn't fully verified.
function authenticateJWT(req, res, next) {
    const authHeader = req.headers['authorization'];
    if (!authHeader) {
        return res.status(401).json({ message: 'Authorization header required' });
    }

    const parts = authHeader.split(' ');
    if (parts.length !== 2 || parts[0] !== 'Bearer' || !parts[1]) {
        return res.status(401).json({ message: 'Malformed authorization header' });
    }

    jwt.verify(parts[1], process.env.JWT_SECRET, (err, decoded) => {
        if (err) {
            return res.status(401).json({ message: 'Invalid or expired token' });
        }
        req.auth = decoded;
        next();
    });
}

// Restricts a route to the given roles. Must run after authenticateJWT so
// that req.auth.role is available.
function authorizeRoles(...allowedRoles) {
    return (req, res, next) => {
        if (!req.auth || !allowedRoles.includes(req.auth.role)) {
            return res.status(403).json({ message: 'Insufficient permissions' });
        }
        next();
    };
}

module.exports = { authenticateJWT, authorizeRoles };
