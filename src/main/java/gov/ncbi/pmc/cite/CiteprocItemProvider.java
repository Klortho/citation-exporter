package gov.ncbi.pmc.cite;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import de.undercouch.citeproc.ItemDataProvider;
import de.undercouch.citeproc.csl.CSLItemData;
import de.undercouch.citeproc.helper.json.JsonLexer;
import de.undercouch.citeproc.helper.json.JsonParser;

/**
 * This is a superclass for all of the implementations of ItemDataProvider.
 */
public abstract class CiteprocItemProvider implements ItemDataProvider {
    protected Map<String, CSLItemData> item_cache;

    CiteprocItemProvider() {
        item_cache = new HashMap<String, CSLItemData>();
    }

    /**
     * Pre-fetch an item that we're interested in (per request).  This allows us to respond with an
     * informative error message, if there's a problem.  Otherwise, retrieveItem is called from within
     * citeproc-js, and there's no way to pass the error message back out.
     * @param id
     * FIXME:  Should distinguish between bad requests (like, bad id value) and internal
     * server errors.
     */
    public abstract void prefetchItem(String id) throws IOException;

    /**
     * Parse a JSON item, and put it into the cache
     */
    protected void cacheItem(String id, String item_json)
        throws IOException
    {
        // Parse the JSON
        Map<String, Object> m = null;
        //try {
            m = new JsonParser(new JsonLexer(new StringReader(item_json))).parseObject();
        //}
        //catch(Exception e) {
        //    return "Problem parsing JSON: " + e;
        //}
        CSLItemData item = CSLItemData.fromJson(m);
        if (item == null) throw new IOException("Problem creating a CSLItemData object from backend JSON");
        item_cache.put(id, item);
    }

}