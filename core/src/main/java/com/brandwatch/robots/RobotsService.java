package com.brandwatch.robots;

import java.io.Closeable;
import java.net.URI;

public interface RobotsService extends Closeable {

    boolean isAllowed(String crawlerAgentString, URI url);

}
