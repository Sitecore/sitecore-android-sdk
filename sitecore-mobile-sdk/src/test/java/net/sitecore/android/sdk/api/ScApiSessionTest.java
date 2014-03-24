package net.sitecore.android.sdk.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class ScApiSessionTest extends MockedServerAndroidTestCase {

    @Test
    public void testGetAnonymousSession() throws Exception {
        ScApiSession session = ScApiSessionFactory.newAnonymousSession(getBackendUrl());

        assertNotNull(session);
        assertTrue(session.isAnonymous());
    }

}
