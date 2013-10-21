package net.sitecore.android.sdk.api;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.os.RemoteException;

import com.android.volley.ExecutorDelivery;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import net.sitecore.android.sdk.api.provider.ScItemsContract;

import static net.sitecore.android.sdk.api.LogUtils.LOGE;

/**
 * Stores successful results in {@link net.sitecore.android.sdk.api.provider.ScItemsProvider} and delivers response
 */
class ContentProviderExecutorDelivery extends ExecutorDelivery {

    private final ContentResolver mResolver;

    public ContentProviderExecutorDelivery(ContentResolver resolver) {
        super(Executors.newSingleThreadExecutor());
        mResolver = resolver;
    }

    @Override
    public void postResponse(Request<?> request, Response<?> response) {
        boolean isCachableRequest = (request instanceof ScRequest) && ((ScRequest)request).shouldCache();
        boolean isScResult = (response.result != null) && (response.result instanceof ScResponse);

        if (isCachableRequest && isScResult) {
            ScResponse scResponse = (ScResponse) response.result;
            ArrayList <ContentProviderOperation> operations = scResponse.toContentProviderOperations();
            if (!operations.isEmpty()) {
                try {
                    mResolver.applyBatch(ScItemsContract.CONTENT_AUTHORITY, operations);
                } catch (RemoteException e) {
                    // Should we invoke error callback on caching error?
                    //super.postError(request, new VolleyError(e));
                    LOGE(e);
                } catch (OperationApplicationException e) {
                    LOGE(e);
                }
            }
        }
        super.postResponse(request, response);
    }
}
