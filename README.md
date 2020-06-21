# employee-api
version `0.0.1-SNAPSHOT`

## Covers:
- API Security with JWT Oauth2 Token access
- ROLE base API access (Only `/delete` api is accessible by `ADMIN` user)
- h2 Databases
- Swagger2 UI for API specs
- Global Exception Handling
- Unit Test 100% code cooverage
- Integration Test

## To test the application

 ### First you will need the following basic pieces of information:

 * client: testjwtclientid
 * secret: XY7kmzoNzl100
 * **Non-admin** username and password: `koushik.pal` and `jwtpass`
 * **Admin** employee: `admin` and `jwtpass`

 - **1. Generate an access token**

   Use the following generic command to generate an `access token`:
   ```
   $ curl testjwtclientid:XY7kmzoNzl100@localhost:8080/oauth/token -d grant_type=password -d 
   username=john.doe -d password=jwtpass
   ```
    You'll receive a response similar to below

    ```
    {
      "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidGVzdGp3dHJlc291cmNlaWQiXSwidXNlcl9uYW1lIjoiYWRtaW4uYWRtaW4iLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNDk0NDU0MjgyLCJhdXRob3JpdGllcyI6WyJTVEFOREFSRF9VU0VSIiwiQURNSU5fVVNFUiJdLCJqdGkiOiIwYmQ4ZTQ1MC03ZjVjLTQ5ZjMtOTFmMC01Nzc1YjdiY2MwMGYiLCJjbGllbnRfaWQiOiJ0ZXN0and0Y2xpZW50aWQifQ.rvEAa4dIz8hT8uxzfjkEJKG982Ree5PdUW17KtFyeec",
      "token_type": "bearer",
      "expires_in": 43199,
      "scope": "read write",
      "jti": "0bd8e450-7f5c-49f3-91f0-5775b7bcc00f"
    }
    ```

 - **2. Use the token to access resources through your RESTful API**

    Access content available to all authenticated users. Use the generated token  as the value of the Bearer in the Authorization header as follows:
 
    **Request**
    ```
     curl --location --request GET 'http://localhost:8080/employee-api/v1/employees' \
    --header 'Authorization: bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidGVzdGp3dHJlc291cmNlaWQiXSwidXNlcl9uYW1lIjoiYWRtaW4iLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNTkyNzU1MjkxLCJhdXRob3JpdGllcyI6WyJTVEFOREFSRF9VU0VSIiwiQURNSU5fVVNFUiJdLCJqdGkiOiI2MjA4MzE3Mi0yNzljLTQ4YzEtOTRiZi1kMDEyMzI5ZGYwNzciLCJjbGllbnRfaWQiOiJ0ZXN0and0Y2xpZW50aWQifQ.dz4hslXWH_4p_p07wYyHEZLf1GKnKfbkIt_9BciiIqk'
    ```
   **Response**

    ```
            {
              "status": {
                  "code": 0,
                  "message": "Success"
              },
              "data": {
                  "employee_list": [
                      {
                          "id": 1,
                          "username": "koushik.pal",
                          "firstName": "Koushik",
                          "lastName": "Pal",
                          "activeStatus": "active",
                          "registeredDate": "2020-06-21T04:00:58.360+0000",
                          "deletedDate": null,
                          "roles": [
                              {
                                  "id": 1,
                                  "roleName": "STANDARD_USER",
                                  "description": "Standard User - Has no admin rights"
                              }
                          ]
                      },
                      {
                          "id": 2,
                          "username": "admin",
                          "firstName": "Admin",
                          "lastName": "Admin",
                          "activeStatus": "active",
                          "registeredDate": "2020-06-21T04:00:58.364+0000",
                          "deletedDate": null,
                          "roles": [
                              {
                                  "id": 1,
                                  "roleName": "STANDARD_USER",
                                  "description": "Standard User - Has no admin rights"
                              },
                              {
                                  "id": 2,
                                  "roleName": "ADMIN_USER",
                                  "description": "Admin User - Has permission to perform admin tasks"
                              }
                          ]
                      }
                  ]
              }
          }
    ```
  
## API Specs
Please run the application and navigate to Swagger2 UI:
http://localhost:8080/swagger-ui.html#/employee-controller


 
