package gov.ncbi.pmc.cite;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * This fetches item data in either PMFU or citeproc-json format, given an IdSet.
 * One of these is instantiated per servlet.
 */
public abstract class ItemSource {
    protected App app;

    public ItemSource(App app) throws Exception
    {
        this.app = app;
    }

    /**
     * Get the NXML for a given ID.
     */
    public abstract Document retrieveItemNxml(String idType, String id)
        throws IOException;

    /**
     * Get the PMFU XML, given an ID
     */
    public Document retrieveItemPmfu(String idType, String id)
        throws IOException
    {
        Document nxml = retrieveItemNxml(idType, id);
        return (Document) app.doTransform(nxml, "pmfu");
    }

    /**
     * Get the item as a json object, as defined by citeproc-json
     */
    public JsonNode retrieveItemJson(String idType, String id)
        throws IOException
    {
        Document pmfu = retrieveItemPmfu(idType, id);
        String jsonStr = (String) app.doTransform(pmfu, "pmfu2json");
        ObjectNode json = (ObjectNode) app.getMapper().readTree(jsonStr);
        json.put("id", IdSet.tid(idType, id));
        return json;
    }

    public ObjectMapper getMapper() {
        return app.getMapper();
    }}
