from flask import Flask, make_response

# Create an instance of the Flask class, passing in the name of the current module
app = Flask(__name__)

# Define a route for the root URL ("/")
@app.route("/")
def index():
    # Function that handles requests to the root URL
    # Return a plain text response
    return "hello world"

# Define a route for the "/no_content" URL
@app.route("/no_content")
def no_content():
    """Return 'no content found' with a status of 204.

    Returns:
        tuple: A tuple containing a dictionary and a status code.
    """
    # Create a dictionary with a message and return it with a 204 No Content status code
    return ({"message": "No content found"}, 204)

# Define a route for the "/exp" URL
@app.route("/exp")
def index_explicit():
    """Return 'Hello World' message with a status code of 200.

    Returns:
        response: A response object containing the message and status code 200.
    """
    # Create a response object with the message "Hello World"
    resp = make_response({"message": "Hello World"})
    # Set the status code of the response to 200
    resp.status_code = 200
    # Return the response object
    return resp
@app.route("/person", methods=['POST'])
def add_by_uuid():
    new_person = request.json
    if not new_person:
        return {"message": "Invalid input parameter"}, 422
    # code to validate new_person ommited
    try:
        data.append(new_person)
    except NameError:
        return {"message": "data not defined"}, 500

    return {"message": f"{new_person['id']}"}, 200

@app.route("/name_search")
def name_search():
    """Find a person in the database based on the provided query parameter.

    Returns:
        json: Person if found, with status of 200
        404: If not found
        400: If the argument 'q' is missing
        422: If the argument 'q' is present but invalid (e.g., empty or numeric)
    """
    # Get the 'q' query parameter from the request URL
    query = request.args.get("q")

    # Check if the query parameter 'q' is missing
    if query is None:
        return {"message": "Query parameter 'q' is missing"}, 400

    # Check if the query parameter is present but invalid (empty or numeric)
    if query.strip() == "" or query.isdigit():
        return {"message": "Invalid input parameter"}, 422

    # Iterate through the 'data' list to search for a matching person
    for person in data:
        # Check if the query string is present in the person's first name (case-insensitive)
        if query.lower() in person["first_name"].lower():
            # Return the matching person as a JSON response with a 200 OK status code
            return person, 200

 # If no matching person is found, return a JSON response with a message and a 404 Not Found
    return {"message": "Person not found"}, 404

@app.route("/count")
def count():
    try:
        # Attempt to return the count of items in 'data' as a JSON response
        return {"data count": len(data)}, 200
    except NameError:
        # Handle the case where 'data' is not defined
        # Return a JSON response with a message and a 500 Internal Server Error status code
        return {"message": "data not defined"}, 500

@app.route("/person/<uuid:id>")
def find_by_uuid(id):
    # Iterate through the 'data' list to search for a person with a matching ID
    for person in data:
        # Check if the 'id' field of the person matches the 'id' parameter
        if person["id"] == str(id):
            # Return the matching person as a JSON response with a 200 OK status code
            return person
    # If no matching person is found, return a JSON response with a message and a 404 Not Found status code
    return {"message": "person not found"}, 404

@app.route("/person/<uuid:id>", methods=['DELETE'])
def delete_by_uuid(id):
    # Iterate through the 'data' list to search for a person with a matching ID
    for person in data:
        # Check if the 'id' field of the person matches the 'id' parameter
        if person["id"] == str(id):
            # Remove the person from the 'data' list
            data.remove(person)
            # Return a JSON response with a message confirming deletion and a 200 OK status code
            return {"message": f"Person with ID {id} deleted"}, 200
    # If no matching person is found, return a JSON response with a message and a 404 Not Found status code
    return {"message": "person not found"}, 404

@app.errorhandler(404)
def api_not_found(error):
    # This function is a custom error handler for 404 Not Found errors
    # It is triggered whenever a 404 error occurs within the Flask application
    return {"message": "API not found"}, 404