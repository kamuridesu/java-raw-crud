# java-raw-crud

Simple CRUD in pure Java.

## How to run

```bash
bash run.sh
```
## Routes

### GET /?username=USERNAME

Get user by username.

### POST /

Create a new user.

### PUT /

Update a user.

### DELETE /?username=USERNAME

Delete a user.

## Example

### POST /

```json
{
    "name": "kamuri",
    "personal_info": {
        "age": 1,
        "skills": [
            "python",
            "java"
        ],
        "salary": 2.0
    }
}
```
## TODO

- [ ] Add get all users route
- [ ] Add tests
- [x] Add Dockerfile
