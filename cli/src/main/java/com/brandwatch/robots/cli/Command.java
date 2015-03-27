package com.brandwatch.robots.cli;

/*
 * #%L
 * Robots (command-line interface)
 * %%
 * Copyright (C) 2015 Brandwatch
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Brandwatch nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

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
        config.setReadTimeoutMillis(arguments.getReadTimeoutMillis());

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
