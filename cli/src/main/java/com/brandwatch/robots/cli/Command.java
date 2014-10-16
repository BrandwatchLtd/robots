package com.brandwatch.robots.cli;

import com.brandwatch.robots.RobotsConfig;
import com.brandwatch.robots.RobotsFactory;
import com.brandwatch.robots.RobotsService;
import com.google.common.base.Function;

import javax.annotation.Nonnull;
import java.net.URI;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.transform;

public class Command {

    @Nonnull
    private final Arguments arguments;

    public Command(@Nonnull Arguments arguments) {
        this.arguments = checkNotNull(arguments, "argument is null");
    }

    public List<Result> getResults() {

        final RobotsConfig config = new RobotsConfig();
        config.setMaxFileSizeBytes(arguments.getMaxFileSizeBytes());
        config.setMaxRedirectHops(arguments.getMaxRedirectHops());
        config.setDefaultCharset(arguments.getDefaultCharset());

        final RobotsFactory factory = new RobotsFactory(config);

        final RobotsService service = factory.createService();

        return transform(arguments.getResources(), new Function<URI, Result>() {
            @Override
            public Result apply(URI input) {
                return new Result(input, service.isAllowed(arguments.getAgent(), input));
            }
        });
    }

}
