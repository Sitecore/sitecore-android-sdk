package net.sitecore.android.sdk.api;

import com.android.volley.toolbox.RequestFuture;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class ScApiSessionTest extends MockedServerAndroidTestCase {

    @Test
    public void testGetSession() throws Exception {
        setMockResponse(TestData.RSA_PUBLIC_KEY);

        RequestFuture<ScApiSession> future = RequestFuture.newFuture();
        ScApiSession.getSession(getContext(), getBackendUrl(), "name", "password", future);
        ScApiSession session = future.get(15, TimeUnit.SECONDS);

        assertNotNull(session);
        assertFalse(session.isAnonymous());
    }

    @Test
    public void testGetAnonymousSession() throws Exception {
        ScApiSession session = ScApiSession.getAnonymousSession(getBackendUrl());

        assertNotNull(session);
        assertTrue(session.isAnonymous());
    }

    @Test
    public void testValidateOk() throws Exception {
        setMockResponse(TestData.RSA_PUBLIC_KEY);
        RequestFuture<ScApiSession> future = RequestFuture.newFuture();
        ScApiSession.getSession(getContext(), getBackendUrl(), "name", "password", future);
        ScApiSession session = future.get(10, TimeUnit.SECONDS);

        setMockResponse(TestData._200_1_item_20_fields);
        RequestFuture<Boolean> future2 = RequestFuture.newFuture();
        session.validate(getContext(), future2);
        Boolean valid = future2.get(10, TimeUnit.SECONDS);
        assertTrue(valid);
    }

    @Test
    public void testValidateFail() throws Exception {
        setMockResponse(TestData.RSA_PUBLIC_KEY);
        RequestFuture<ScApiSession> future = RequestFuture.newFuture();
        ScApiSession.getSession(getContext(), getBackendUrl(), "name", "password", future);
        ScApiSession session = future.get(10, TimeUnit.SECONDS);

        setMockResponse(TestData._401_access_not_granted);
        RequestFuture<Boolean> future2 = RequestFuture.newFuture();
        session.validate(getContext(), future2);
        Boolean notValid = future2.get(10, TimeUnit.SECONDS);
        assertFalse(notValid);
    }

}
