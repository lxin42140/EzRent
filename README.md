# Group number
Group 16


# Youtube link
https://www.youtube.com/watch?v=3rQRXsj-WnQ&ab_channel=Ziyuemon

# Project description

Backend: Java EE stack with GlassFish
Front-end: JSF and Angular

There are a total of 3 front-end applications and 1 common back-end: `EzRent-war` [JSF], `EzRentAdmin` [Angular] and `EzRentDeliveryManagement` [Angular]. 

To deploy the 2 angular applications:
1. Clean build `EzRent` project 
2. Deploy `EzRent` project for the RESTful API
3. Proceed to directory of individual Angular application 
4. Run npm install 
5. Run ng serve to serve web application 
6. username and password for admin portal: admin1, password
7. username and password for delivery portal: company1, password

To deploy the `EzRent-war`:
1. Create database with the name `EzRent`
2. Update the nb project private property
3. Move folder `glassfish-5.1.0-uploadedfiles` to c drive
4. Add in the required jar files to the project i.e. Omnifaces
5. Clean build `EzRent` project
6. Deploy the project
7. username and password for test accounts
  - john_doe99, password (created all listings and request)
  - kimpossible, password (made offers)
  - minho_89, password

