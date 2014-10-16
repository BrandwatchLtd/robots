package com.brandwatch.robots.cli.converters;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

import java.net.*;

import static com.google.common.base.Preconditions.checkNotNull;

public class URIConverter implements IStringConverter<URI> {

    @Override
    public URI convert(String uriString) {
        checkNotNull(uriString, "uriString is null");
        try {
            if (uriString.isEmpty()) {
                return new URI(uriString);
            }
            URL url = new URL(uriString);
            return new URI(
                    url.getProtocol(),
                    url.getUserInfo(),
                    IDN.toASCII(url.getHost()),
                    url.getPort(),
                    url.getPath(),
                    url.getQuery(),
                    url.getRef()).normalize();
        } catch (MalformedURLException e) {
            throw new ParameterException("Malformed URL: " + e.getMessage());
        } catch (URISyntaxException e) {
            throw new ParameterException("Malformed URI: " + e.getMessage());
        }
    }


}
