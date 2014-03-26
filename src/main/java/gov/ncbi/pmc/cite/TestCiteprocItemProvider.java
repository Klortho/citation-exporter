package gov.ncbi.pmc.cite;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import de.undercouch.citeproc.CSL;
import de.undercouch.citeproc.ItemDataProvider;
import de.undercouch.citeproc.csl.CSLItemData;
import de.undercouch.citeproc.helper.json.JsonLexer;
import de.undercouch.citeproc.helper.json.JsonParser;

/**
 * This implementation of the CiteprocItemProvider produces fake item data for testing.
 * This class uses test files that should be stored in webapp/test.
 */
public class TestCiteprocItemProvider extends CiteprocItemProvider {
    private URL base_url;
    private int num_samples = 21;
    private int next_sample = 1;
    // The following, if true, cycles through the samples; if false, random
    private boolean consecutive = true;

    public TestCiteprocItemProvider(URL _base_url) {
        super();
        base_url = _base_url;
    }

    // Implement interface method
    public void prefetchItem(String id) throws IOException
    {
        if (item_cache.get(id) != null) return;

        // Pick the json sample
        int sample_num = consecutive ? next_sample : 1 + (int)(Math.random() * 21);
        if (consecutive) next_sample = (next_sample % num_samples) + 1;

        String sample_filename = "sample" + String.format("%02d", sample_num) + ".json";
        //System.out.println("sample_file = " + sample_filename);


        // Read the JSON from a sample file in webapp/test
        URL test_url = new URL(base_url, sample_filename);
        InputStream test_is = test_url.openStream();
        //System.out.println("resource url is '" + context.getResource(base_path + sample_filename) + "'");
        if (test_is == null) throw new IOException("Problem reading test data!");
        StringWriter test_writer = new StringWriter();
        IOUtils.copy(test_is, test_writer, "utf-8");
        String item_json = test_writer.toString();

        // Replace the id
        item_json = item_json.replace("{$id}", id);
        //System.out.println("JSON: \n" + item_json);

        cacheItem(id, item_json);
    }
}
