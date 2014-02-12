package net.sitecore.android.sdk.api.internal;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.os.RemoteException;

import com.android.volley.ExecutorDelivery;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.ArrayList;
import java.util.concurrent.Executor;

import net.sitecore.android.sdk.api.ScRequest;
import net.sitecore.android.sdk.api.ScResponse;
import net.sitecore.android.sdk.api.provider.ScItemsContract;
import net.sitecore.android.sdk.api.provider.ScItemsProvider;

import static net.sitecore.android.sdk.api.internal.LogUtils.LOGE;

/**
 * Stores successful results in {@link ScItemsProvider} and delivers response.
 */
public class ContentProviderExecutorDelivery extends ExecutorDelivery {

    private final ContentResolver mResolver;

    public ContentProviderExecutorDelivery(ContentResolver resolver, Executor executor) {
        super(executor);
        mResolver = resolver;
    }

    @Override
    public void postResponse(Request<?> request, Response<?> response) {
        boolean isCachableRequest = (request instanceof ScRequest) && ((ScRequest) request).shouldCache();
        boolean isScResult = (response.result != null) && (response.result instanceof ScResponse);

        if (isCachableRequest && isScResult) {
            ScResponse scResponse = (ScResponse) response.result;
            ArrayList<ContentProviderOperation> operations = ((ScRequest) request).getBeforeSaveContentProviderOperations();
            operations.addAll(scResponse.toContentProviderOperations());
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
