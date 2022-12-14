/*
 * Copyright (c) WhatsApp Inc. and its affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package ec.blcode.stickerswapp.CodeAddSticker2Wapp.WhatsAppBasedCode;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;

import ec.blcode.stickerswapp.BuildConfig;


@SuppressWarnings("FieldCanBeLocal")
public class WhitelistCheck {
    private static final String AUTHORITY_QUERY_PARAM = "authority";
    private static final String IDENTIFIER_QUERY_PARAM = "identifier";
    private static String STICKER_APP_AUTHORITY = BuildConfig.CONTENT_PROVIDER_AUTHORITY;
    //private static String CONSUMER_WHATSAPP_PACKAGE_NAME = "com.whatsapp";
    private static String SMB_WHATSAPP_PACKAGE_NAME = "com.whatsapp.w4b";
    private static String CONTENT_PROVIDER = ".provider.sticker_whitelist_check";
    private static String QUERY_PATH = "is_whitelisted";
    private static String QUERY_RESULT_COLUMN_NAME = "result";

    public static boolean isWhitelisted(@NonNull Context context, @NonNull String identifier, String CONSUMER_WHATSAPP_PACKAGE_NAME) {
        try {
            boolean consumerResult = isWhitelistedFromProvider(context, identifier, CONSUMER_WHATSAPP_PACKAGE_NAME);
            boolean smbResult = isWhitelistedFromProvider(context, identifier, SMB_WHATSAPP_PACKAGE_NAME);
            return consumerResult && smbResult;
        } catch (Exception e) {
            return false;
        }
    }
    public static void RemoveStickerPackFromProvider(@NonNull Context context, @NonNull String identifierPack, String whatsappPackageName){
        final String whatsappProviderAuthority = whatsappPackageName + ".provider.metadata_stickerpack";
        final PackageManager packageManager = context.getPackageManager();
        final ProviderInfo providerInfo = packageManager.
                resolveContentProvider(whatsappProviderAuthority, PackageManager.GET_META_DATA);
        if (providerInfo == null) {

        }

    }

    private static boolean isWhitelistedFromProvider(@NonNull Context context, @NonNull String identifier, String whatsappPackageName) {
        final PackageManager packageManager = context.getPackageManager();
        if (isPackageInstalled(whatsappPackageName, packageManager)) {
            final String whatsappProviderAuthority = whatsappPackageName + CONTENT_PROVIDER;
            final ProviderInfo providerInfo = packageManager.
                                                resolveContentProvider(whatsappProviderAuthority, PackageManager.GET_META_DATA);
            //ContentProviderInfo{
            // name=com.gbwhatsapp.provider.sticker_whitelist_check
            // className=com.whatsapp.stickers.WhitelistPackQueryContentProvider}
            // provider is not th   ere.
            if (providerInfo == null) {
                return false;
            }
            final Uri queryUri = new Uri.Builder().scheme(StickerContentProvider.CONTENT_SCHEME).authority(whatsappProviderAuthority).
                                                appendPath(QUERY_PATH).appendQueryParameter(AUTHORITY_QUERY_PARAM, STICKER_APP_AUTHORITY)
                                                .appendQueryParameter(IDENTIFIER_QUERY_PARAM, identifier).build();
            try (final Cursor cursor = context.getContentResolver().
                                                    query(queryUri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    final int whiteListResult = cursor.getInt(cursor.getColumnIndexOrThrow(QUERY_RESULT_COLUMN_NAME));
                    return whiteListResult == 1;
                }
            }
        } else {
            //if app is not installed, then don't need to take into its whitelist info into account.
            return true;
        }
        return false;
    }

    public static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            final ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            //noinspection SimplifiableIfStatement
            if (applicationInfo != null) {
                return applicationInfo.enabled;
            } else {
                return false;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
