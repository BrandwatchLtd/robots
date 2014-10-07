package com.brandwatch.robots;

import com.brandwatch.robots.domain.Robots;
import com.google.common.io.CharSource;

public interface RobotsDownloader {

    Robots load(CharSource robotsSource);

}
