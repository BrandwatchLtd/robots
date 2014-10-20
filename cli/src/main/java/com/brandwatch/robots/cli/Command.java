package com.brandwatch.robots.cli;

import com.brandwatch.robots.RobotsConfig;
import com.brandwatch.robots.RobotsFactory;
import com.brandwatch.robots.RobotsService;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

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
        try {
            ImmutableList.Builder<Result> results = ImmutableList.builder();
            for (URI resource : arguments.getResources()) {
                results.add(new Result(resource, service.isAllowed(arguments.getAgent(), resource)));
            }
            return results.build();
        } finally {
            try {
                service.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
