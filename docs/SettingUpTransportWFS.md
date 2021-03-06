# Setting up transport wfs service (requires postgresql)

1) Install Redis: http://redis.io/ (tested with version 2.6.14 for linux and 2.4.5 for Windows (available in https://github.com/dmajkic/redis))

2) Redis configuration can be found in `oskari-server/servlet-map/src/main/resources/oskari.properties` but using default values nothing needs to be changed:

    # redis
    redis.hostname=localhost
    redis.port=6379
    redis.pool.size=100

3) Startup redis

4) Start the server with `mvn clean install -f servlet-map-pom.xml -Doskari.dropdb=true -Doskari.setup=postgres-mapwfs2`

5) Go to `oskari-server/transport`, compile the transport service with `mvn clean install` and start the jetty service with `mvn jetty:run`. 

With default settings you now have:
* redis running on port 6379
* oskari-server running on port 2373
* transport wfs service running on port 2374

6) Open your browser with http://localhost:2373/?viewId=4 to see a new view loaded from the database.
    You should now see a new map layer of type WFS listed on the maplayers flyout. Add it to map, zoom close to Helsinki for example and you should see points appearing to the map from the wfs service
