package com.brandwatch.robots.net;

import com.brandwatch.robots.domain.Robots;
import com.google.common.io.CharSource;

public interface RobotsDownloader {

    Robots load(CharSource robotsSource);

}
