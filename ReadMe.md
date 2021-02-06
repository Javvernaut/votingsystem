[![Codacy Badge](https://app.codacy.com/project/badge/Grade/96772101b561468f800e827c8e358334)](https://www.codacy.com/gh/Javvernaut/votingsystem/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Javvernaut/votingsystem&amp;utm_campaign=Badge_Grade) [![Build Status](https://travis-ci.com/Javvernaut/votingsystem.svg?branch=master)](https://travis-ci.com/Javvernaut/votingsystem)

# TopJava Graduation Project
***
Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) without frontend.

The task is:

Build a voting system for deciding where to have lunch.

2 types of users: admin and regular users  
Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)  
Menu changes each day (admins do the updates)  
Users can vote on which restaurant they want to have lunch at  
Only one vote counted per user  
If user votes again the same day:  
If it is before 11:00 we assume that he changed his mind.  
If it is after 11:00 then it is too late, vote can't be changed  
Each restaurant provides a new menu each day.
***
## Spring boot application
Java 15(OpenJDK), Spring boot(Web, Security, Validation, Data JPA), Maven, H2, Lombok, Jackson JSON, Swagger 3
### Install
Entry point
>javvernaut.votingsystem.VotingSystemApplication

Maven
>mvn spring-boot:run
> 
### Available resources
#### Swagger UI
>/swagger-ui

#### Unauthorized users
Method | Resource | Description
-------|----------|------------
GET | /api/restaurants | Get all restaurants, which provided menu for a present day
GET | /api/restaurants/{id} | Get restaurant by id, which provided menu for a present day
GET | /api/restaurants/{id}/menu | Get current menu
GET | /api/restaurants/{id}/menu/items | Get current menu items
POST | /api/profile/register | Register new user

#### Authorized users
Method | Resource | Description
-------|----------|------------
GET | /api/votes | Get all votes for current user
GET | /api/votes/current | Get vote for current user 
POST | /api/votes?restaurantId= | Create new vote
PATCH | /api/votes?restaurant= | Update vote, if possible
DELETE | /api/votes | Delete vote
GET | /api/profile | Get profile for current user
PUT | /api/profile | Update profile for current user
DELETE | /api/profile | Delete profile for current user

#### Admins
Restaurants

Method | Resource | Description
-------|----------|------------
GET | /api/admin/restaurants | Get all restaurants
GET | /api/admin/restaurants/{id} | Get restaurant by id
POST | /api/admin/restaurants | Create new restaurant
PUT | /api/admin/restaurants/{id} | Update existed restaurant
DELETE | /api/admin/restaurants/{id} | Delete existed restaurant

Dishes

Method | Resource | Description
-------|----------|------------
GET | /api/admin/restaurants/{restaurantId}/dishes | Get all dishes by restaurant
GET | /api/admin/restaurants/{restaurantId}/dishes/{id} | Get dish by restaurant
POST | /api/admin/restaurants/{restaurantId}/dishes | Create dish
UPDATE | /api/admin/restaurants/{restaurantId}/dishes/{id} | Update dish
DELETE | /api/admin/restaurants/{restaurantId}/dishes/{id} | Delete dish

Menus

Method | Resource | Description
-------|----------|------------
GET | /api/admin/restaurants/{restaurantId}/menus | Get all menus by restaurant
GET | /api/admin/restaurants/{restaurantId}/menus/{id} | Get menu by restaurant
POST | /api/admin/restaurants/{restaurantId}/menus | Create menu
PUT | /api/admin/restaurants/{restaurantId}/menus/{id} | Update menu
DELETE | /api/admin/restaurants/{restaurantId}/menus/{id} | Delete menu

Items

Method | Resource | Description
-------|----------|------------
GET | /api/admin/restaurants/{restaurantId}/menus/{menuId}/items | Get all menu items by menu
GET | /api/admin/restaurants/{restaurantId}/menus/{menuId}/items/{id} | Get menu item by menu
POST | /api/admin/restaurants/{restaurantId}/menus/{menuId}/items | Create menu item
PATCH | /api/admin/restaurants/{restaurantId}/menus/{menuId}/items/{id} | Update menu item
DELETE | /api/admin/restaurants/{restaurantId}/menus/{menuId}/items/{id} | Delete menu item

## cURL samples

### For unauthorized users
#### Get all restaurants, which provide a menu for present day
    curl -X GET "http://localhost:8080/api/restaurants"
#### Get restaurant, which provide a menu for present day
    curl -X GET "http://localhost:8080/api/restaurants/100004"
#### Get an active menu for restaurant 100004
    curl -X GET "http://localhost:8080/api/restaurants/100004/menu"
#### Get menu items for restaurant 100004
    curl -X GET "http://localhost:8080/api/restaurants/100004/menu/items"
#### Register new user
    curl -X POST "http://localhost:8080/api/profile/register" 
    -H "Content-Type: application/json"
    -d "{
          "id" : null,
          "name" : "Newbie",
          "email" : "vo@vo.vo",
          "password" : "newpass"
        }"
    
***
### For authorized users
#### Get all votes
    curl -X GET  "http://localhost:8080/api/votes" 
    --basic --user al@mail.ru:alien
#### Get current vote
    curl -X GET  "http://localhost:8080/api/votes/current" 
    --basic --user al@mail.ru:alien
#### Create new vote
    curl -X POST --location "http://localhost:8080/api/votes?restaurantId=100005"
    --basic --user admin@gmail.com:admin
#### Change vote
    curl -X PATCH --location "http://localhost:8080/api/votes?restaurantId=100004"
    --basic --user admin@gmail.com:admin
#### Delete vote
    curl -X DELETE --location "http://localhost:8080/api/votes"
    --basic --user admin@gmail.com:admin
***
#### Get profile
    curl -X GET "http://localhost:8080/api/profile"
    --basic --user al@mail.ru:alien
#### Update profile
    curl -X PUT "http://localhost:8080/api/profile"
    -H "Content-Type: application/json"
    -d "{
          "id" : 100002,
          "name" : "Vasilek",
          "email" : "vas@vas.ru",
          "password" : "vasvas"
        }"
    --basic --user al@mail.ru:alien
#### Delete profile
    curl -X DELETE "http://localhost:8080/api/profile"
    --basic --user al@mail.ru:alien
***
### For admins
#### Get all restaurants
    curl -X GET "http://localhost:8080/api/admin/restaurants"
    --basic --user admin@gmail.com:admin
#### Get restaurant
    curl -X GET "http://localhost:8080/api/admin/restaurants/100005"
    --basic --user admin@gmail.com:admin
#### Create restaurant
    curl -X POST --location "http://localhost:8080/api/admin/restaurants"
    -H "Content-Type: application/json"
    -d "{
          "name" : "Mumu"
        }"
    --basic --user admin@gmail.com:admin
#### Update restaurant
    curl -X PUT --location "http://localhost:8080/api/admin/restaurants/100040"
    -H "Content-Type: application/json"
    -d "{
          "name" : "Metropolis"
        }"
    --basic --user admin@gmail.com:admin
#### Delete restaurant
    curl -X DELETE --location "http://localhost:8080/api/admin/restaurants/100040"
    --basic --user admin@gmail.com:admin
***
#### Get dishes
    curl -X GET "http://localhost:8080/api/admin/restaurants/100005/dishes"
    --basic --user admin@gmail.com:admin
#### Get dish
    curl -X GET "http://localhost:8080/api/admin/restaurants/100005/dishes/100019"
    --basic --user admin@gmail.com:admin
#### Create dish
    curl -X POST "http://localhost:8080/api/admin/restaurants/100005/dishes"
    -H "Content-Type: application/json"
    -d "{
          "name":"Ulunka"
        }"
    --basic --user admin@gmail.com:admin
#### Update dish
    curl -X PUT "http://localhost:8080/api/admin/restaurants/100005/dishes/100020"
    -H "Content-Type: application/json"
    -d "{
          "name":"Varenets"
        }"
    --basic --user admin@gmail.com:admin
#### Delete dish
    curl -X DELETE "http://localhost:8080/api/admin/restaurants/100005/dishes/100041"
    --basic --user admin@gmail.com:admin
***
#### Get all menus
    curl -X GET "http://localhost:8080/api/admin/restaurants/100005/menus"
    --basic --user admin@gmail.com:admin
#### Get menu
    curl -X GET "http://localhost:8080/api/admin/restaurants/100005/menus/100028"
    --basic --user admin@gmail.com:admin
#### Create menu
    curl -X POST "http://localhost:8080/api/admin/restaurants/100005/menus"
    -H "Content-Type: application/json"
    -d "{
          "date" : "2020-12-16"
        }"
    --basic --user admin@gmail.com:admin
#### Update menu
    curl -X PUT "http://localhost:8080/api/admin/restaurants/100005/menus/100029"
    -H "Content-Type: application/json"
    -d "{
          "date" : "2020-12-18"
        }"
    --basic --user admin@gmail.com:admin
#### Delete menu
    curl -X DELETE "http://localhost:8080/api/admin/restaurants/100005/menus/100029"
    --basic --user admin@gmail.com:admin
***
#### Get menu items
    curl -X GET "http://localhost:8080/api/admin/restaurants/100005/menus/100028/items"
    --basic --user admin@gmail.com:admin
#### Get menu item
    curl -X GET --location "http://localhost:8080/api/admin/restaurants/100005/menus/100028/items/100020"
    --basic --user admin@gmail.com:admin
#### Create menu item
    curl -X POST --location "http://localhost:8080/api/admin/restaurants/100004/menus/100026/items"
    -H "Content-Type: application/json"
    -d "{
          "dishId" : 100011,
          "price" : 55
        }"
    --basic --user admin@gmail.com:admin
#### Update menu item
    curl -X PATCH --location "http://localhost:8080/api/admin/restaurants/100005/menus/100030/items/100056"
    -H "Content-Type: application/json"
    -d "{
          "id" : 100056,
          "dishId" : 100018,
          "price" : 11
        }"
    --basic --user admin@gmail.com:admin
#### Delete menu item
    curl -X DELETE --location "http://localhost:8080/api/admin/restaurants/100005/menus/100028/items/100020"
    --basic --user admin@gmail.com:admin
***