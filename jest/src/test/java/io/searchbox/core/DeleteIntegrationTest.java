package io.searchbox.core;

import io.searchbox.client.JestResult;
import io.searchbox.client.JestResultHandler;
import io.searchbox.common.AbstractIntegrationTest;
import org.elasticsearch.test.ElasticsearchIntegrationTest;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author Dogukan Sonmez
 */
@ElasticsearchIntegrationTest.ClusterScope(scope = ElasticsearchIntegrationTest.Scope.SUITE, numDataNodes = 1)
public class DeleteIntegrationTest extends AbstractIntegrationTest {

    private final static Logger log = LoggerFactory.getLogger(DeleteIntegrationTest.class);

    @Test
    public void deleteDocument() throws IOException {
        JestResult result = client.execute(new Delete.Builder("1")
                .index("twitter")
                .type("tweet")
                .build());
        assertFalse(result.isSucceeded());
    }

    @Ignore // async execution disturbs flow of the test suite
    @Test
    public void deleteDocumentAsynchronously() throws InterruptedException, ExecutionException, IOException {
        client.executeAsync(new Delete.Builder("1")
                .index("twitter")
                .type("tweet")
                .build(), new JestResultHandler<JestResult>() {
            @Override
            public void completed(JestResult result) {
                assertFalse(result.isSucceeded());
            }

            @Override
            public void failed(Exception ex) {
                fail("failed during the asynchronous calling");
            }
        });
    }

    @Test
    public void deleteRealDocument() throws IOException {
        Index index = new Index.Builder("{\"user\":\"kimchy\"}").index("cvbank").type("candidate").id("1").refresh(true).build();
        client.execute(index);
        JestResult result = client.execute(new Delete.Builder("1")
                .index("cvbank")
                .type("candidate")
                .build());

        assertTrue(result.getErrorMessage(), result.isSucceeded());
    }

}
