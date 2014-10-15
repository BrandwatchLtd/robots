package com.brandwatch.robots;

import java.net.URI;

public interface RobotsService {

    boolean isAllowed(String crawlerAgentString, URI url);

}
