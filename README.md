# java-raw-crud

Simple CRUD in pure Java.

## How to run

### Building

The `run.sh` script will build the project and test it. The script supports the following options:

- `-p | --package` - Packages the project into a .jar file.
- `-r | --run` - Runs the project.
- `-s | --skipTests` - Skips the tests.
- `-d | --skipDeps` - Skips the dependencies.
- `-b | --skipBuild` - Skips the build.

### Example

```bash
bash run.sh --package --run
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

- [x] Add get all users route
- [x] Add tests
- [x] Add Dockerfile
- [x] Build .jar file
