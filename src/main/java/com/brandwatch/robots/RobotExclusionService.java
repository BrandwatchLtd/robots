package com.brandwatch.robots;

import java.net.URI;

public interface RobotExclusionService {

    boolean isAllowed(String crawlerAgentString, URI url);

}
