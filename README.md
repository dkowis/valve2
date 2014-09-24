# Valve2: Electric Boogaloo

A prototype of an alternative, much cleaner, much saner Valve implementation for repose.

## Notable features
* No usage of any servlet context to pass around parameters, or application contexts, or anything.
The `ServletContext` is just a thing, and Repose would pay zero attention to it at all. All of the necessary
data can come from Spring values, or autowired beans.

* the Warfile is just a tiny bit of metadata. There's a web.xml and a very simplistic applicationContext. There's also 
a `ReposeInitializer` class, which makes us require a Servlet 3.x application. This also gives us the ability to properly
load spring contexts in the right order, so that core services can be made available to the ReposeFilter as well
as other filters in the chain, without duplicating. Deploying the war file to a standalone container works great.
I didn't validate any system properties setting, but that is a solvable mechanism via spring, or some validation logic
I'm sure.

* valve is a shaded jar containing all the things, in this case including filters. We can load up all the things, before
loading the jetties and the spring contexts, so that is also a solvable problem

## To run the things

* Standalone valve using a war file (Doesn't work yet): `gradle :valveWar:run`
* Standalone valve using shaded jar with all things: `gradle :valve:run`
* Deploy the war file to a jetty: `gradle jettyEclipseRun`




