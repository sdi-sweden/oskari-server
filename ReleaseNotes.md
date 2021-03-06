# Release Notes

## 1.16

### content-resources

Added upgrade SQLs that can be run to update database structure on version update. See [Upgrade documentation for details](docs/Upgrading.md)

Added a new commandline parameter "-Doskari.addview={file under resources/json/views}" to add a view without setup file

Added setupfiles:
* to initialize an empty base db with -Doskari.setup=create-empty-db
* to register bundles with -Doskari.setup=postgres-register-bundles

These can be used to initialize the database step-by-step. Also added an example setup for parcel application (-Doskari.setup=postgres-parcel).


### service-base / IOHelper

Added debugging methods to ignore SSL certificate errors. This can be used for existing code by adding properties (NOTE! This is global setting for all connections through IOHelper):

	oskari.trustAllCerts=true
	oskari.trustAllHosts=true

Individual connections can use these by calling IOHelper-methods:

    public static void trustAllCerts(final HttpURLConnection connection) throws IOException
    public static void trustAllHosts(final HttpURLConnection connection) throws IOException

### servlet-map

Now reads an oskari-ext.properties file from classpath to override default oskari.properties values.

### control-base GetSotkaData

Action handler has been refactored and indicators/regions listing now uses Redis for caching if available

## 1.15

### User domain object

Now has a convenience method to check if user has any of the roles in given String array (containing rolenames)

### GetWSCapabilities action route

Permitted roles for executing the action is now configurable with property 'actionhandler.GetWSCapabilitiesHandler.roles'
