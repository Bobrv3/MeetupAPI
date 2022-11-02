# MeetupAPI
CRUD Rest API for working with events

## Deployment
1. Clone a repository with a project: 
      git clone https://github.com/Bobrv3/MeetupAPI.git
2. In the root of the project (where the docker-compose file is located), run the command: 
      docker-compose --env-file .env.default up
3. Wait until the application is deployed.
4. To check the success of the application start, run the request:
      http://localhost:8080/api/meetups/1 
      
Default values when starting the application:
+ server port: 8080
+ db user name: test_user
+ db password: 1234
+ db port: 5432
+ db name: meetup_db

To change the default values, you need to change the corresponding variables in the .env.default file, which is located in the root folder of the application
      
## Endpoints
+ GET	    /api/meetups/{id}	  Getting a specific event by id
+ GET	    /api/meetups/	      Getting a list of all events
  parameters (optional):
    - sort_order={asc|desc}   Sort order can be asc or desc
    - sort_by={field_name}    Sort by the specified field. (to sort by several fields, the sort_by parameter is provided for each field)
    - {filterd_field}={operation};{value}   Filter the field by the specified value with <a href=https://github.com/Bobrv3/MeetupAPI/blob/main/MeetupAPI/src/main/java/com/bobrov/meetup/dao/util/FilterOperation.java>operations</a>
+ POST	  /api/meetups/	      Registration (creation) of a new event
+ PUT	    /api/meetups/{id}	  Changing information about an existing event
+ DELETE	/api/meetups/{id}	  Deleting an event

