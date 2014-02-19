package net.sitecore.android.sdk.api;

import java.util.LinkedHashSet;
import java.util.Set;

import net.sitecore.android.sdk.api.model.PayloadType;
import net.sitecore.android.sdk.api.model.RequestScope;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class RequestBuilderTest extends MockedServerAndroidTestCase {

    private ScApiSession mSession;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        mSession = ScApiSessionFactory.newAnonymousSession("http://sample.com");
    }

    @Test
    public void testEmpty() {
        ScRequest request = mSession.readItemsRequest(null, null).build();
        assertEquals("http://sample.com/-/item/v1", request.getUrl());
    }

    @Test
    public void testAllParamsAreUsed() {
        LinkedHashSet<String> fields = new LinkedHashSet<String>();
        fields.add("Field1");
        fields.add("{A60ACD61-A6DB-4182-8329-C957982CEC74}");

        ScRequest request = mSession.readItemsRequest(null, null)
                .fromSite("/sitecore/shell")
                .byItemPath("/content/home/items")
                .apiVersion(2)
                .itemVersion(3)
                .database("extranet")
                .byItemId("{2075CBFF-C330-434D-9E1B-937782E0DE49}")
                .setLanguage("en-UK")
                .setFields(fields)
                .withPayloadType(PayloadType.FULL)
                .withScope(RequestScope.PARENT, RequestScope.SELF)
                .bySitecoreQuery("fast:/Sitecore/Content/*")
                .setPage(3, 5)
                .build();

        assertEquals("http://sample.com/-/item/v2/sitecore/shell/content/home/items" +
                "?sc_itemid=%7B2075CBFF-C330-434D-9E1B-937782E0DE49%7D&sc_itemversion=3&sc_database=extranet" +
                "&language=en-UK&fields=Field1%7C%7BA60ACD61-A6DB-4182-8329-C957982CEC74%7D" +
                "&payload=full&scope=p%7Cs&query=fast%3A%2FSitecore%2FContent%2F*&page=3&pageSize=5",
                request.getUrl());
    }

    @Test
    public void testScope() {
        ScRequest request;

        request = mSession.readItemsRequest(null, null)
                .withScope(RequestScope.SELF, RequestScope.PARENT, RequestScope.CHILDREN)
                .build();
        assertEquals("http://sample.com/-/item/v1?scope=s%7Cp%7Cc", request.getUrl());

        request = mSession.readItemsRequest(null, null)
                .withScope(RequestScope.PARENT, RequestScope.SELF, RequestScope.CHILDREN)
                .build();
        assertEquals("http://sample.com/-/item/v1?scope=p%7Cs%7Cc", request.getUrl());

        request = mSession.readItemsRequest(null, null)
                .withScope(RequestScope.PARENT)
                .build();
        assertEquals("http://sample.com/-/item/v1?scope=p", request.getUrl());

        request = mSession.readItemsRequest(null, null)
                .withScope(RequestScope.SELF)
                .build();
        assertEquals("http://sample.com/-/item/v1?scope=s", request.getUrl());

        request = mSession.readItemsRequest(null, null)
                .withScope(RequestScope.CHILDREN)
                .build();
        assertEquals("http://sample.com/-/item/v1?scope=c", request.getUrl());

        request = mSession.readItemsRequest(null, null)
                .withScope(RequestScope.CHILDREN, RequestScope.CHILDREN)
                .build();
        assertEquals("http://sample.com/-/item/v1?scope=c", request.getUrl());

        request = mSession.readItemsRequest(null, null)
                .withScope(RequestScope.CHILDREN).byItemPath("/sitecore/content/Home")
                .build();
        assertEquals("http://sample.com/-/item/v1/sitecore/content/Home?scope=c", request.getUrl());

        try {
            request = mSession.readItemsRequest(null, null)
                    .withScope(RequestScope.CHILDREN, RequestScope.CHILDREN, RequestScope.SELF, RequestScope.PARENT)
                    .build();
            fail("> 3 scope params must throw exception.");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testFields() {
        ScRequest request;
        Set<String> fields;

        fields = new LinkedHashSet<String>();
        request = mSession.readItemsRequest(null, null)
                .setFields(fields)
                .build();
        assertEquals("http://sample.com/-/item/v1", request.getUrl());

        fields = new LinkedHashSet<String>();
        fields.add("Title");
        request = mSession.readItemsRequest(null, null)
                .setFields(fields)
                .build();
        assertEquals("http://sample.com/-/item/v1?fields=Title", request.getUrl());

        fields = new LinkedHashSet<String>();
        fields.add("Title1");
        fields.add("Title2");
        request = mSession.readItemsRequest(null, null)
                .setFields(fields)
                .build();
        assertEquals("http://sample.com/-/item/v1?fields=Title1%7CTitle2", request.getUrl());

        request = mSession.readItemsRequest(null, null)
                .setFields()
                .build();
        assertEquals("http://sample.com/-/item/v1", request.getUrl());

        request = mSession.readItemsRequest(null, null)
                .setFields("Title", "Name", "Location")
                .build();
        assertEquals("http://sample.com/-/item/v1?fields=Title%7CName%7CLocation", request.getUrl());
    }

    @Test
    public void testDefaultOptions() {
        mSession.setDefaultDatabase("sitecore");
        mSession.setDefaultLanguage("ru");
        mSession.setDefaultSite("/sitecore");

        ScRequest request = mSession.readItemsRequest(null, null).byItemId("id").build();
        assertEquals("http://sample.com/-/item/v1/sitecore?sc_itemid=id&sc_database=sitecore&language=ru",
                request.getUrl());

        request = mSession.readItemsRequest(null, null).byItemId("id").fromSite("/shell/").database("web").
                setLanguage("en").build();

        assertEquals("http://sample.com/-/item/v1/shell/?sc_itemid=id&sc_database=web&language=en",
                request.getUrl());

        mSession.setDefaultDatabase(null);
        mSession.setDefaultLanguage(null);
        mSession.setDefaultSite(null);

        request = mSession.readItemsRequest(null, null).byItemId("id").build();

        assertEquals("http://sample.com/-/item/v1?sc_itemid=id",
                request.getUrl());

        mSession.setDefaultDatabase("web");

        request = mSession.readItemsRequest(null, null).byItemId("id").fromSite("/sitecore/shell/").setLanguage("en")
                .build();

        assertEquals("http://sample.com/-/item/v1/sitecore/shell/?sc_itemid=id&sc_database=web&language=en",
                request.getUrl());
    }

    @Test
    public void testCetRenderingHTML() {
        GetRenderingHtmlRequest request;
        request = mSession.getRenderingHtmlRequest(TestData.Renderings.MasterSampleRendering.ID,
                TestData.Content.LanguageItemVersions.ID, null, null)
                .fromSite(TestData.SHELL_SITE)
                .database("master")
                .setLanguage("da")
                .addRenderingParameter("p1", "value1")
                .addRenderingParameter("p2", "value2")
                .build();
        assertEquals(
                "http://sample.com/-/item/v1/sitecore/shell/-/actions/GetRenderingHtml?renderingId=%7B5FAC342C-AC30-4F74-8B61-6C38B527CF32%7D&sc_itemid=%7B7272BE8E-8C4C-4F2A-8EC8-F04F512B04CB%7D&sc_database=master&language=da&p1=value1&p2=value2",
                request.getUrl());
    }
}
