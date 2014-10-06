package com.brandwatch.robots.net;

import java.net.URI;


public interface RobotsSourceFactory {

    RobotsSource createFor(URI url);

}
