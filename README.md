# Pizzeria Toppings Service 
Your task is to build a backend app that can help solve the following
problem: The business has decided that it wants to expand into other industries and to open a
pizzeria. After collaborating with multiple food supplier services in the area, we realized that we
need to figure out which toppings customers would want so that we know what to order.

## Requirements:
1. Create an endpoint that allows for customers to submit their email address along with
   the list of toppings that they would be interested in.
2. Create an endpoint that allows for the front end team to grab the list of toppings currently
   submitted and the number of unique customers that have requested that topping.
3. Only the customer’s latest topping submission will be recorded.
4. Implementation must happen using Kotlin & Spring Boot.

## Bonus points will be awarded for:
- Persisting data between runs
- Creativity for adding additional features
- A special endpoint for listing your personal topping choice!

## How to run

1. Be sure to have the environment variable `DATABASE_FILE` setup in your environment/terminal
2. Have at least Java version 17 installed
3. Run `./gradlew bootRun` on Mac/Linux or `gradlew.exe bootRun` on Windows, which should also run the database migrations and start the application
4. Use your favorite HTTP client to run queries against the endpoints below

### Available Endpoints

By default, all endpoints are accessible via http://localhost:8080/v1

#### Toppings

- POST /toppings
   - Creates toppings and associates them with a user
   - Deletes previously associated toppings if they exist
```json
{
   "email": "user@email.com",
   "toppings": [
      "pepperoni",
      "peppers"
   ],
   "location": "optional field for location data"
}
```

- GET /toppings/popularity
   - Returns the most popular toppings requested, sorted by the most popular

- GET /toppings/popularity/location
   - Returns the most popular toppings requested by location

- GET /toppings/users/{id}
   - Returns the toppings associated with user ID

- GET /toppings/best
   - Returns my personal favorite pizza toppings

#### Users

- GET /users
   - Returns all stored users

- GET /users/{id}
   - Returns user data associated with the ID {id}

- PATCH /users/{id}
   - Allows updating of the user's email associated with the ID {id}
```json
{
   "email": "new@email.com"
}
```