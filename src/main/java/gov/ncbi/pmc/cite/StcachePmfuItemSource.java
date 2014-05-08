package gov.ncbi.pmc.cite;

import gov.ncbi.pmc.Pmfu;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Implementation of ItemSource that gets data from the stcache.
 */

public class StcachePmfuItemSource extends ItemSource {
    private String pmfuImage;
    private Pmfu pmfuStcache;

    public StcachePmfuItemSource(App app) throws Exception
    {
        super(app);
        pmfuImage = System.getProperty("stcache_image");
        pmfuStcache = new Pmfu(pmfuImage);
    }

    public Document retrieveItemNxml(String idType, String id)
        throws IOException
    {
        throw new IOException("Using PMFU data source; can't retrieve NXML data");
    }

    public String byteToHex2(byte b) {
        int i = b >= 0 ? b : 256 + b;
        String hs = Integer.toHexString(i);
        return hs.length() < 2 ? "0" + hs : hs;
    }

    public Document retrieveItemPmfu(String idType, String id)
        throws IOException
    {
        try {
            String key = null;
            if (idType.equals("aiid") || idType.equals("pmid")) {
                key = IdSet.tid(idType, id);
            }
            else {
                throw new IOException("Unrecognized identifier type: " + idType);
            }
            byte[] pmfuBytes = pmfuStcache.get(key);

            if (pmfuBytes == null)
                throw new IOException("Unable to retrieve PMFU data for " + IdSet.tid(idType, id));

          /*
            // Check for bad UTF-8 encoding, compare dumping as string vs dumping byte array
            System.out.println("-----------------------------");
            IOUtils.write(pmfuBytes, System.out);     //

            System.out.println("-----------------------------");
            System.out.println(pmfuBytes);
            for (int i = 0; i < pmfuBytes.length; ++i) {
                System.out.print(byteToHex2(pmfuBytes[i]) + " ");
            }
          */

            return app.newDocumentBuilder().parse(new ByteArrayInputStream(pmfuBytes));

          /*
            Note that this works, but let's not do it this way, as it masks the problem with the
            bad UTF-8 encoding:
            String resultStr = new String(pmfuBytes, "UTF8");
            InputSource is = new InputSource(new StringReader(resultStr));
            return app.newDocumentBuilder().parse(is);
          */
        }
        catch (Exception e) {
            throw new IOException(e);
        }
    }

    protected JsonNode fetchItemJson(String idType, String id)
            throws IOException
    {
        return app.getMapper().readTree(new URL(pmfuImage + idType + "/" + id + ".json"));
    }

}
