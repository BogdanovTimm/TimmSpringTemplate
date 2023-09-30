# Working with Docker

## Creating a DockerImage of your applicaiton

1. Add a pre-defined `Dockerfile` file to your project root folder.
2. Run `docker build -t %docker_acount_name%/%application_name%:%major_version%.%minor_version%.%patch_version% .` (yes, with dot in the end) in your project's root folder
3. Go to DockerDesktop and Push recently created container to DockerHub
4. Add a pre-defined `docker-compose.yaml`
5. Run `docker-compose up` in your project's root folder
