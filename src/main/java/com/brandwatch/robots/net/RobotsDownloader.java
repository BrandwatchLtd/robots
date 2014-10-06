package com.brandwatch.robots.net;

import com.brandwatch.robots.domain.Robots;

public interface RobotsDownloader {

    Robots load(RobotsSource robotsSource);

}
