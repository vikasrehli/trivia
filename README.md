# trivia
TRIVIA GAME
A REST API with following 2 endpoints: -
POST /trivia/start
PUT /trivia/reply

Above endpoints will do the following:
POST /trivia/start
This endpoint will call following public 3rd party API to get a random trivia question:
GET https://opentdb.com/api.php?amount=1
