package com.github.scribejava.core.extractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import com.github.scribejava.core.exceptions.OAuthParametersMissingException;
import com.github.scribejava.core.model.OAuthConfig;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.github.scribejava.core.ObjectMother;

public class HeaderExtractorTest {

    private HeaderExtractorImpl extractor;
    private OAuthRequest request;

    @Before
    public void setUp() {
        request = ObjectMother.createSampleOAuthRequest();
        extractor = new HeaderExtractorImpl();
    }

    @Test
    public void shouldExtractStandardHeader() {
        final String header = extractor.extract(request);
        assertTrue(header.startsWith("OAuth"));
        assertTrue(header.contains("oauth_callback=\"http%3A%2F%2Fexample%2Fcallback\""));
        assertTrue(header.contains("oauth_signature=\"OAuth-Signature\""));
        assertTrue(header.contains("oauth_consumer_key=\"AS%23%24%5E%2A%40%26\""));
        assertTrue(header.contains("oauth_timestamp=\"123456\""));
        assertEquals(4, header.split(",").length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldExceptionIfRequestIsNull() {
        extractor.extract(null);
    }

    @Test(expected = OAuthParametersMissingException.class)
    public void shouldExceptionIfRequestHasNoOAuthParams() {
        final OAuthRequest emptyRequest = new OAuthRequest(Verb.GET, "http://example.com",
                new OAuth20Service(null, new OAuthConfig("test", "test")));
        extractor.extract(emptyRequest);
    }
}
