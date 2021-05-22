var request = require("request");

var options = { 
method: 'POST',
  url: 'https://dev-42xjlb9o.us.auth0.com/oauth/token',
  headers: { 'content-type': 'application/json' },
  body: '{"client_id":"18TCfNDHMSjLpbV2rrQNqD9O1Jl0T8pz","client_secret":"U9dF97dhL0iuZj4somb9KulLhVyJUX4HRFljJnvYUSchaSEBYpZT_cLdKFAkZ2EX","audience":"https://dev-42xjlb9o.us.auth0.com/api/v2/","grant_type":"client_credentials"}' };

request(options, function (error, response, body) {
  if (error) throw new Error(error);

  console.log(body);
});

