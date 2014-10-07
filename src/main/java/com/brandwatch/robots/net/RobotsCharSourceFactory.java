package com.brandwatch.robots.net;

import com.google.common.io.CharSource;

import java.net.URI;


public interface RobotsCharSourceFactory {

    CharSource createFor(URI url);

}
