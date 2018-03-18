# Overview
My first vertx trial.
I started the tutorial by following this site: http://vertx.io/blog/my-first-vert-x-3-application/

# Run on IDE
Open Eclipse launcher, set main class `io.vertx.core.Launcher` with program arguments `run com.example.rurocker.vertex.core.MainVerticle -conf conf/first-application-conf.json`.
If you want to debug, add `-Dvertx.options.blockedThreadCheckInterval=2147483647` in the VM arguments to block warning exception in console.

# References
* http://vertx.io/blog/my-first-vert-x-3-application/
* https://stackoverflow.com/questions/24277301/run-vertx-in-an-ide