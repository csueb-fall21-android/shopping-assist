===

# Shopping Assistant

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description

Take a picture and search for details about the item in the picture. Details like, price, product details, where you can purchase it, and items that might be similar to it.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:**
    - Would fall under shopping. 
- **Mobile:**
    - User will be able to use their phone and take pictures of items to display advanced product description. 
    - In addition, a user will be able to scan an item from anywhere like home and get a list of nearby stores with      the item in stock.
- **Story:**
    - When looking to purchase an item in store, you can use this app to get additional information that might not be displayed on the in person description/display. You can also get price/detail comparisons for the product at nearby stores or whether you can purchase it online.
- **Market:**
    - Market would be anyone who wants to buy something but doesn't have enough details about the item they are looking for.
- **Habit:**
    - Would be used by any potential shopper that is looking to buy a product, but is unsure what features an item may provide or how it works. 
- **Scope:**
    - For full implementation, might need some very in depth work on computer vision/machine learning, etc, a lot of api's integrated to be used.
    - A stripped down version might still be interesting to make, but it might not be as useful depending on which features will be ultimately implemented.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

- User can take a picture of an item and the app will "search" for the item on the internet (image processing + search for details)
- User can save the item to a persisted list, including details of the price and location that the item was found (if it's in a shop; automatically populate with image processing + google maps)
- User can see a list of previously saved items
- User can see a detailed view of the item, with included saved information that the app retrieved from the search (product details)
- User can send the detailed view of item to another user to view

**Optional Nice-to-have Stories**

- Search for a product online (price + details) by taking a picture of it (very heavy on training a model to recognize images)
    - A usecase would be if you're in a store and you want more details, or even if you're not in a store, you can ask the app to identify it and give you details on where to purchase it
- Storing item details (images, price, details, etc) that a user would want to buy but decided to save for later
- Item comparison between items of the same type (case: user is shopping for a fridge and they want to compare fridges that they have saved)
- Searching for items based on a user filter (i.e. they want to look for a fridge of a particular size... but this might be a hard thing to do for multiple types of items)
- Sharing an item's details (or a list of items) to someone else to view and comment on or also add other items to look at)
- Recommended items may also list other user's discovered items nearby

### 2. Screen Archetypes

* Landing Page
    - Home page with a stylized logo that will ask the user for a log in or sign up option.
   * [list associated required story here]
   - User will click on login button and will be redirected to a login page.
   - If no account exists, then the user will click on the sign up button and will be redirected to a register page.
   
* Login/Signup
   - User will either be directed to a login or sign up page.
   * [list associated required story here]
   - Database that will check whether or not a specific user exists for the site.
   - If user does not exist, then save the data that is input into the text box after the submit button is pressed. Data will be stored in a database for persistence.

* Bottom Navigation
    - List Feed of items
    - Take a picture

* List Screen / Page of items after Login or Sign Up
    - After login show a feed of saved lists and pictures that were taken previously. 
    - Have functionality in the Login page to click on the camera button to take a new picture.
    - New account will not have a feed of saved items, so just display the layout along with functionality to click camera for a new picture.
    - Click on an item to see its detailed view

* Take a Picture of Item Details Page
    - User can take a picture
    - After the user takes a picture, details will be automatically populated: price, name, brand.
    - Once a picture is taken and then saved, it transitions to a detailed view

* Detailed Item View
    - Additionally there could be a recommended/suggested items list populated by a search based on the item

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* [fill out your first tab]
    - List screen / Home feed
* [fill out your second tab]
    - Take picture tab
* [fill out your third tab]

**Flow Navigation** (Screen to Screen)

* [list first screen here] - Login / sign up screen
   * [list screen navigation here]
   - Includes text box for username and password.
* [list second screen here] - Direct to either a log in page or new account page
   * [list screen navigation here]
   - Once we log in or sign up we get directed to a new respective page.
   - Login page will display information such as a list of the previous pictures of items that were taken
   - New account page will display a blank slate with options to take a new picture.

## Wireframes
[Add picture of your hand sketched wireframes in this section]
<img src="YOUR_WIREFRAME_IMAGE_URL" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models
[Add table of models]
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]