# run selenoid with browsers.json using Docker images:
docker run -d --name selenoid -p 4444:4444 -v /var/run/docker.sock:/var/run/docker.sock -v /mnt/c/"Program Files"/Selenoid/config/:/etc/selenoid/:ro aerokube/selenoid:latest-release

# run selenoid-ui using Docker images:
docker run -d --name selenoid-ui --link selenoid -p 8080:8080 aerokube/selenoid-ui --selenoid-uri=http://selenoid:4444
