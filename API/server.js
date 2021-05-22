const express = require('express');
const app = express();
const jwt = require('express-jwt');
const jwksRsa = require('jwks-rsa');
const cors = require('cors');
const bodyParser = require('body-parser');
const jwtAuthz = require('express-jwt-authz');

// Enable CORS
app.use(cors());

// Create middleware for checking the JWT
const checkJwt = jwt({
  // Dynamically provide a signing key based on the kid in the header and the signing keys provided by the JWKS endpoint
  secret: jwksRsa.expressJwtSecret({
    cache: true,
    rateLimit: true,
    jwksRequestsPerMinute: 5,	
    jwksUri: `https://localhost/.well-known/jwks.json`,
  }),

  // Validate the audience and the issuer
  audience: '{https://dev-42xjlb9o.us.auth0.com/api/v2/}', //replace with your API's audience, available at Dashboard > APIs
  issuer: 'http://localhost/',
  algorithms: [ 'RS256' ]
});

// Enable the use of request body parsing middleware
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
  extended: true
}));

// Create timesheets API endpoint
app.get('/timesheets/upload', checkJwt, jwtAuthz(['batch:upload']), function(req, res){
  var timesheet = req.body;


  // Save the timesheet to the database...

  //send the response
  res.status(201).send(timesheet);
});

// Launch the API Server at localhost:8080
app.listen('8080', () => {
	console.log('Server Started')
});