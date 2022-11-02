# MeetupAPI
CRUD Rest API for working with events

## Deployment
1. Clone a repository with a project:<br>
      <code>git clone https://github.com/Bobrv3/MeetupAPI.git</code>
2. In the root of the project (where the docker-compose file is located), run the command:<br>
      <code>docker-compose --env-file .env.default up</code>
3. Wait until the application is deployed.
4. To check the success of the application start, run the request:<br>
      <code>http://localhost:8080/api/meetups/1</code>
      
Default values when starting the application:
+ server port: 8080
+ db user name: test_user
+ db password: 1234
+ db port: 5432
+ db name: meetup_db

To change the default values, you need to change the corresponding variables in the <a href=https://github.com/Bobrv3/MeetupAPI/blob/main/.env.default>.env.default</a> file, which is located in the root folder of the application
      
## Endpoints
+ <pre>GET       /api/meetups/{id}	  Getting a specific event by id</pre>
+ <pre>GET       /api/meetups/	      Getting a list of all events</pre>
  <pre>parameters (optional):
    - sort_order={asc|desc}   Sort order can be asc or desc
    - sort_by={field_name}    Sort by the specified field. (to sort by several fields, the sort_by parameter is provided for each field)
    - {filterd_field}={operation};{value}   Filter the field by the specified value with <a href=https://github.com/Bobrv3/MeetupAPI/blob/main/MeetupAPI/src/main/java/com/bobrov/meetup/dao/util/FilterOperation.java>operations</a></pre>
+ <pre>POST      /api/meetups/	      Registration (creation) of a new event</pre>
+ <pre>PUT       /api/meetups/{id}	  Changing information about an existing event</pre>
+ <pre>DELETE    /api/meetups/{id}	  Deleting an event</pre>


