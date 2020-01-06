# Build JKK Package
This script is designed to package JKK for every desidered distro effortlessly.

## Instructions
- Obtain the Docker image for your target:
  
  ```docker pull m0rf30/pacur-<distro>```

- Run the container:
  
  ```docker run --entrypoint=/bin/bash -v $(pwd):/pacur -ti m0rf30/pacur-<distro>```

- Build your package from container shell:
  
  ```pacur build <distro>```

## Troubleshooting
If you have problems to test JKK on RPM-based distros, remember to change the JRE with this command:
  
  ```alternatives --config java```
